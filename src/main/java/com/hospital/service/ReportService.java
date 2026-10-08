package com.hospital.service;

import com.hospital.model.*;
import com.hospital.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
public class ReportService {
    private final PatientRepository patients;
    private final DoctorRepository doctors;
    private final AppointmentRepository appointments;
    private final BillRepository bills;
    private final MedicineRepository medicines;

    public Map<String, Object> stats() {
        List<Bill> paid = bills.findAll().stream().filter(b -> b.getStatus() == Bill.Status.PAID).toList();
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("patients", patients.count());
        m.put("doctors", doctors.count());
        m.put("appointments", appointments.count());
        m.put("todayAppointments", appointments.countByDate(LocalDate.now()));
        m.put("pendingBills", bills.countByStatus(Bill.Status.PENDING));
        m.put("revenue", paid.stream().mapToDouble(Bill::getAmount).sum());
        m.put("lowStock", medicines.findAll().stream().filter(x -> x.getStock() != null && x.getStock() < 20).count());
        m.put("expiringSoon", medicines.findAll().stream()
                .filter(x -> x.getExpiryDate() != null && x.getExpiryDate().isBefore(LocalDate.now().plusMonths(3))).count());
        return m;
    }

    public Map<String, Object> reports() {
        List<Appointment> apps = appointments.findAll();
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("appointmentsByStatus", apps.stream().collect(Collectors.groupingBy(a -> a.getStatus().name(), TreeMap::new, Collectors.counting())));
        m.put("appointmentsByDoctor", apps.stream().collect(Collectors.groupingBy(a -> a.getDoctor().getUser().getName(), TreeMap::new, Collectors.counting())));
        m.put("patientsByGender", patients.findAll().stream().collect(
                Collectors.groupingBy(p -> p.getGender() == null || p.getGender().isBlank() ? "Not stated" : p.getGender(), TreeMap::new, Collectors.counting())));
        m.put("revenueByMonth", bills.findAll().stream().filter(b -> b.getStatus() == Bill.Status.PAID && b.getPaidDate() != null)
                .collect(Collectors.groupingBy(b -> YearMonth.from(b.getPaidDate()).toString(), TreeMap::new, Collectors.summingDouble(Bill::getAmount))));
        m.put("lowStock", medicines.findAll().stream().filter(x -> x.getStock() != null && x.getStock() < 20).toList());
        return m;
    }
}
