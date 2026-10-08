package com.hospital.controller;

import com.hospital.dto.Dto.PurchaseReq;
import com.hospital.model.Medicine;
import com.hospital.service.MedicineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/api/medicines") @RequiredArgsConstructor
public class MedicineController {
    private final MedicineService service;

    @GetMapping @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')") public List<Medicine> all() { return service.all(); }
    @PostMapping @PreAuthorize("hasRole('ADMIN')") public Medicine create(@RequestBody Medicine m) { return service.create(m); }
    @PutMapping("/{id}") @PreAuthorize("hasRole('ADMIN')") public Medicine update(@PathVariable Long id, @RequestBody Medicine m) { return service.update(id, m); }
    @PutMapping("/{id}/purchase") @PreAuthorize("hasRole('ADMIN')") public Medicine purchase(@PathVariable Long id, @Valid @RequestBody PurchaseReq r) { return service.purchase(id, r.quantity()); }
    @DeleteMapping("/{id}") @PreAuthorize("hasRole('ADMIN')") public void delete(@PathVariable Long id) { service.delete(id); }
}
