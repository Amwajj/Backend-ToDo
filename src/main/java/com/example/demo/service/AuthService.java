package com.example.demo.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.model.entity.Role;
import com.example.demo.model.entity.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;

import jakarta.annotation.PostConstruct;

@Service
public class AuthService {
    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder encoder;

    public AuthService(UserRepository u, RoleRepository r, PasswordEncoder e) {
        this.userRepo = u;
        this.roleRepo = r;
        this.encoder = e;
    }

    @PostConstruct 
    public void initRoles() {
        if (roleRepo.findByName("ROLE_USER").isEmpty())
            roleRepo.save(new Role(null, "ROLE_USER"));
        if (roleRepo.findByName("ROLE_ADMIN").isEmpty())
            roleRepo.save(new Role(null, "ROLE_ADMIN"));
    }

    public void register(String username, String rawPassword, String roleName) {
        if (userRepo.findByUsername(username).isPresent())
            throw new RuntimeException("Username taken");
        User u = new User();
        u.setUsername(username);
        u.setPassword(encoder.encode(rawPassword));
        Role r = roleRepo.findByName(roleName)
                   .orElseThrow(() -> new RuntimeException("Role not found"));
        u.getRoles().add(r);
        userRepo.save(u);
    }
}