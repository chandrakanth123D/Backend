package com.hospital.controller;

import com.hospital.dto.Dto.ConsultReq;
import com.hospital.model.*;
import com.hospital.service.RecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/api") @RequiredArgsConstructor
public class RecordController {
    private final RecordService service;

    @GetMapping("/records/my") @PreAuthorize("hasRole('PATIENT')") public List<MedicalRecord> myRecords() { return service.mine(); }
    @GetMapping("/prescriptions/my") @PreAuthorize("hasRole('PATIENT')") public List<Prescription> myPrescriptions() { return service.myPrescriptions(); }

    @GetMapping("/records/patient/{id}") public List<MedicalRecord> records(@PathVariable Long id) { return service.forPatient(id); }
    @GetMapping("/prescriptions/patient/{id}") public List<Prescription> prescriptions(@PathVariable Long id) { return service.prescriptionsForPatient(id); }

    @PostMapping("/consultations") @PreAuthorize("hasRole('DOCTOR')")
    public MedicalRecord consult(@Valid @RequestBody ConsultReq r) { return service.consult(r); }

    @PutMapping("/records/{id}") @PreAuthorize("hasRole('DOCTOR')")
    public MedicalRecord update(@PathVariable Long id, @Valid @RequestBody ConsultReq r) { return service.update(id, r); }
}
