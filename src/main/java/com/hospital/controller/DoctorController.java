package com.hospital.controller;

import com.hospital.dto.Dto.DoctorReq;
import com.hospital.model.Doctor;
import com.hospital.service.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/api/doctors") @RequiredArgsConstructor
public class DoctorController {
    private final DoctorService service;

    @GetMapping public List<Doctor> all() { return service.all(); }
    @PostMapping @PreAuthorize("hasRole('ADMIN')") public Doctor create(@Valid @RequestBody DoctorReq r) { return service.create(r); }
    @PutMapping("/{id}") @PreAuthorize("hasRole('ADMIN')") public Doctor update(@PathVariable Long id, @Valid @RequestBody DoctorReq r) { return service.update(id, r); }
    @DeleteMapping("/{id}") @PreAuthorize("hasRole('ADMIN')") public void delete(@PathVariable Long id) { service.delete(id); }
}
