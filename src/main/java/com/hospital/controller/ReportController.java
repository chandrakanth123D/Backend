package com.hospital.controller;

import com.hospital.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController @RequestMapping("/api/admin") @PreAuthorize("hasRole('ADMIN')") @RequiredArgsConstructor
public class ReportController {
    private final ReportService service;

    @GetMapping("/stats") public Map<String, Object> stats() { return service.stats(); }
    @GetMapping("/reports") public Map<String, Object> reports() { return service.reports(); }
}
