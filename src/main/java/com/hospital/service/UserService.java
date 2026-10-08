package com.hospital.service;

import com.hospital.model.User;
import com.hospital.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service @RequiredArgsConstructor
public class UserService {
    private final UserRepository users;
    private final PasswordEncoder encoder;

    public User create(String name, String email, String password, User.Role role) {
        String mail = email.trim().toLowerCase();
        if (users.existsByEmail(mail)) throw new ResponseStatusException(HttpStatus.CONFLICT, "An account with this email already exists");
        if (password == null || password.isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password is required");
        User u = new User();
        u.setName(name.trim()); u.setEmail(mail); u.setPassword(encoder.encode(password)); u.setRole(role);
        return u;
    }

    public void update(User u, String name, String email, String password) {
        String mail = email.trim().toLowerCase();
        if (!mail.equals(u.getEmail()) && users.existsByEmail(mail))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "An account with this email already exists");
        u.setName(name.trim()); u.setEmail(mail);
        if (password != null && !password.isBlank()) u.setPassword(encoder.encode(password));
    }
}
