package com.hospital.service;

import com.hospital.config.JwtUtil;
import com.hospital.dto.Dto.*;
import com.hospital.model.*;
import com.hospital.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service @RequiredArgsConstructor
public class AuthService {
    private final UserRepository users;
    private final PatientRepository patients;
    private final DoctorRepository doctors;
    private final PatientService patientService;
    private final PasswordEncoder encoder;
    private final JwtUtil jwt;

    /** Self sign-up is for patients only. Doctors and admins are created by an admin. */
    public AuthResponse register(Register r) { return build(patientService.create(r).getUser()); }

    public AuthResponse login(Login l) {
        User u = users.findByEmail(l.email().trim().toLowerCase())
                .filter(x -> encoder.matches(l.password(), x.getPassword()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Incorrect email or password"));
        return build(u);
    }

    private AuthResponse build(User u) {
        Long profile = switch (u.getRole()) {
            case PATIENT -> patients.findByUserId(u.getId()).map(Patient::getId).orElse(null);
            case DOCTOR -> doctors.findByUserId(u.getId()).map(Doctor::getId).orElse(null);
            default -> null;
        };
        return new AuthResponse(jwt.generate(u.getEmail(), u.getRole().name()), u.getRole().name(), u.getName(), u.getId(), profile);
    }
}
