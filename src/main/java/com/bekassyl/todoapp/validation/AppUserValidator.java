package com.bekassyl.todoapp.validation;

import com.bekassyl.todoapp.model.AppUser;
import com.bekassyl.todoapp.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class AppUserValidator implements Validator {
    private final AppUserService appUserService;

    @Autowired
    public AppUserValidator(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return AppUser.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        AppUser appUser = (AppUser) target;

        if (appUserService.getByUsername(appUser.getUsername()).isPresent()) {
            errors.rejectValue("username", "", "This username is already taken!");
        }
    }
}
