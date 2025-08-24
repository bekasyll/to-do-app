package com.bekassyl.todoapp.controller;

import com.bekassyl.todoapp.dto.AppUserRequestDTO;
import com.bekassyl.todoapp.dto.AuthenticationDTO;
import com.bekassyl.todoapp.model.AppUser;
import com.bekassyl.todoapp.security.JWTUtil;
import com.bekassyl.todoapp.service.AppUserService;
import com.bekassyl.todoapp.util.AppUserNotFoundException;
import com.bekassyl.todoapp.validation.AppUserValidator;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AppUserController {
    private final AppUserService appUserService;
    private final ModelMapper modelMapper;
    private final JWTUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final AppUserValidator appUserValidator;

    @Autowired
    public AppUserController(AppUserService appUserService, ModelMapper modelMapper, JWTUtil jwtUtil, AuthenticationManager authenticationManager, AppUserValidator appUserValidator) {
        this.appUserService = appUserService;
        this.modelMapper = modelMapper;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.appUserValidator = appUserValidator;
    }

    private AppUser convertToAppUser(AppUserRequestDTO appUserRequestDTO) {
        return modelMapper.map(appUserRequestDTO, AppUser.class);
    }

    @PostMapping("/registration")
    public Map<String, String> registration(@RequestBody @Valid AppUserRequestDTO appUserRequestDTO, BindingResult bindingResult) {
        AppUser appUser = convertToAppUser(appUserRequestDTO);

        appUserValidator.validate(appUser, bindingResult);

        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();

            for (FieldError error: errors) {
                errorMsg.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("; ");
            }

            throw new AppUserNotFoundException(errorMsg.toString());
        }

        String jwt = jwtUtil.generateToken(appUserRequestDTO.getUsername());
        appUserService.registerAppUser(appUser, jwt);

        return Map.of("jwt-token", jwt);
    }

    @PostMapping("/login")
    public Map<String, String> performLogin(@RequestBody @Valid AuthenticationDTO authenticationDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();

            for (FieldError error: errors) {
                errorMsg.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("; ");
            }

            throw new AppUserNotFoundException(errorMsg.toString());
        }

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(
                        authenticationDTO.getUsername(), authenticationDTO.getPassword()
                );
        try {
            authenticationManager.authenticate(authToken);
        } catch (BadCredentialsException e) {
            return Map.of("message", "Incorrect credentials!");
        }

        String jwt = jwtUtil.generateToken(authenticationDTO.getUsername());

        Optional<AppUser> appUser = appUserService.getByUsername(authenticationDTO.getUsername());
        if (appUser.isEmpty()) {
            return Map.of("message", "Incorrect credentials!");
        }
        appUserService.updateJWTToken(appUser.get(), jwt);

        return Map.of("jwt-token", jwt);
    }
}
