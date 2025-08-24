package com.bekassyl.todoapp.service;

import com.bekassyl.todoapp.model.AppUser;
import com.bekassyl.todoapp.security.AppUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AppUserDetailsService implements UserDetailsService {
    private final AppUserService appUserService;

    @Autowired
    public AppUserDetailsService(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<AppUser> appUser = appUserService.getByUsername(username);

        if (appUser.isEmpty()) {
            throw new UsernameNotFoundException("User not found!");
        }

        return new AppUserDetails(appUser.get());
    }
}

