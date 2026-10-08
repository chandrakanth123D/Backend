package com.hospital.controller;

import com.hospital.dto.Dto.*;
import com.hospital.model.Bill;
import com.hospital.service.BillService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/api/bills") @RequiredArgsConstructor
public class BillController {
    private final BillService service;

    @GetMapping("/my") @PreAuthorize("hasRole('PATIENT')") public List<Bill> mine() { return service.mine(); }
    @GetMapping @PreAuthorize("hasRole('ADMIN')") public List<Bill> all() { return service.all(); }
    @PostMapping @PreAuthorize("hasRole('ADMIN')") public Bill create(@Valid @RequestBody BillReq r) { return service.create(r); }
    @PutMapping("/{id}/pay") @PreAuthorize("hasAnyRole('PATIENT','ADMIN')") public Bill pay(@PathVariable Long id, @Valid @RequestBody PayReq r) { return service.pay(id, r.paymentMode()); }
}
