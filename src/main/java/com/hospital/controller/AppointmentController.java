package com.hospital.controller;

import com.hospital.dto.Dto.*;
import com.hospital.model.Appointment;
import com.hospital.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController @RequestMapping("/api/appointments") @RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentService service;

    /** Patient: own appointments. Doctor: appointments assigned to them. Admin: all. */
    @GetMapping public List<Appointment> list() { return service.list(); }
    @GetMapping("/{id}") public Appointment get(@PathVariable Long id) { return service.get(id); }

    @GetMapping("/slots")
    public List<String> slots(@RequestParam Long doctorId, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return service.bookedSlots(doctorId, date);
    }

    @PostMapping @PreAuthorize("hasAnyRole('PATIENT','ADMIN')")
    public Appointment book(@Valid @RequestBody AppointmentReq r) { return service.book(r); }

    @PutMapping("/{id}/reschedule") @PreAuthorize("hasAnyRole('PATIENT','ADMIN')")
    public Appointment reschedule(@PathVariable Long id, @Valid @RequestBody Reschedule r) { return service.reschedule(id, r); }

    @PutMapping("/{id}/cancel")
    public Appointment cancel(@PathVariable Long id) { return service.cancel(id); }
}
