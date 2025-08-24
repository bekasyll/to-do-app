package com.bekassyl.todoapp.service;

import com.bekassyl.todoapp.model.AppUser;
import com.bekassyl.todoapp.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class AppUserService {
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AppUserService(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<AppUser> getByUsername(String username) {
        return appUserRepository.findByUsername(username);
    }

    @Transactional
    public void registerAppUser(AppUser appUser, String jwt) {
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        appUser.setRole("ROLE_USER");
        appUser.setActiveToken(jwt);

        appUserRepository.save(appUser);
    }

    @Transactional
    public void updateJWTToken(AppUser appUser, String jwtToken) {
        appUser.setActiveToken(jwtToken);

        appUserRepository.save(appUser);
    }
}
