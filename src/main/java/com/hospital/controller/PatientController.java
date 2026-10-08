package com.hospital.controller;

import com.hospital.dto.Dto.Register;
import com.hospital.model.Patient;
import com.hospital.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/api/patients") @RequiredArgsConstructor
public class PatientController {
    private final PatientService service;
    private final CurrentUser me;

    @GetMapping @PreAuthorize("hasRole('ADMIN')") public List<Patient> all() { return service.all(); }
    @GetMapping("/me") @PreAuthorize("hasRole('PATIENT')") public Patient mine() { return me.patient(); }
    @PutMapping("/me") @PreAuthorize("hasRole('PATIENT')") public Patient updateMe(@Valid @RequestBody Register r) { return service.updateMe(r); }
    @PostMapping @PreAuthorize("hasRole('ADMIN')") public Patient create(@Valid @RequestBody Register r) { return service.create(r); }
    @PutMapping("/{id}") @PreAuthorize("hasRole('ADMIN')") public Patient update(@PathVariable Long id, @Valid @RequestBody Register r) { return service.update(id, r); }
    @DeleteMapping("/{id}") @PreAuthorize("hasRole('ADMIN')") public void delete(@PathVariable Long id) { service.delete(id); }
}
