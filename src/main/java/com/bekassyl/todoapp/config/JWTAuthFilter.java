package com.bekassyl.todoapp.config;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.bekassyl.todoapp.model.AppUser;
import com.bekassyl.todoapp.security.AppUserDetails;
import com.bekassyl.todoapp.security.JWTUtil;
import com.bekassyl.todoapp.service.AppUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTAuthFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil;
    private final AppUserDetailsService appUserDetailsService;

    @Autowired
    public JWTAuthFilter(JWTUtil jwtUtil, AppUserDetailsService appUserDetailsService) {
        this.jwtUtil = jwtUtil;
        this.appUserDetailsService = appUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && !authHeader.isBlank() && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);

            if (jwt.isBlank()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JWT Token in Bearer Header!");
                return;
            } else {
                try {
                    String username = jwtUtil.validateTokenAndRetrieveClaim(jwt);
                    UserDetails userDetails = appUserDetailsService.loadUserByUsername(username);

                    AppUser appUser = ((AppUserDetails) userDetails).getAppUser();

                    if (!jwt.equals(appUser.getActiveToken())) {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Token is not active!");
                        return;
                    }

                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());

                    if (SecurityContextHolder.getContext().getAuthentication() == null) {
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                } catch (JWTVerificationException e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JWT Token!");
                    return;
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
