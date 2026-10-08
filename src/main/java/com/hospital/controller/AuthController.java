package com.hospital.controller;

import com.hospital.dto.Dto.*;
import com.hospital.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/auth") @RequiredArgsConstructor
public class AuthController {
    private final AuthService service;

    @PostMapping("/register") public AuthResponse register(@Valid @RequestBody Register r) { return service.register(r); }
    @PostMapping("/login") public AuthResponse login(@Valid @RequestBody Login l) { return service.login(l); }
}
