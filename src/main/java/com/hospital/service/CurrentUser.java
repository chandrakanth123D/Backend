package com.hospital.service;

import com.hospital.model.*;
import com.hospital.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

/** Resolves the logged-in account (and its patient/doctor profile) from the JWT. */
@Component @RequiredArgsConstructor
public class CurrentUser {
    private final UserRepository users;
    private final PatientRepository patients;
    private final DoctorRepository doctors;

    public User user() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return users.findByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Please sign in again"));
    }
    public Patient patient() {
        return patients.findByUserId(user().getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Patient profile not found"));
    }
    public Doctor doctor() {
        return doctors.findByUserId(user().getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Doctor profile not found"));
    }
    public boolean is(User.Role role) { return user().getRole() == role; }
}
