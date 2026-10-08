package com.hospital.service;

import com.hospital.dto.Dto.BillReq;
import com.hospital.model.*;
import com.hospital.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service @RequiredArgsConstructor
public class BillService {
    private final BillRepository repo;
    private final PatientService patientService;
    private final CurrentUser me;

    public List<Bill> mine() { return repo.findByPatientIdOrderByIdDesc(me.patient().getId()); }
    public List<Bill> all() { return repo.findAllByOrderByIdDesc(); }

    public Bill create(BillReq r) {
        Bill b = new Bill();
        b.setPatient(patientService.get(r.patientId()));
        b.setAppointmentId(r.appointmentId());
        b.setDescription(r.description() == null || r.description().isBlank() ? "Hospital charges" : r.description());
        b.setAmount(r.amount());
        return repo.save(b);
    }

    /** Called when a consultation is completed: bills the doctor's consultation fee once. */
    public void createForAppointment(Appointment a) {
        if (repo.existsByAppointmentId(a.getId())) return;
        double fee = a.getDoctor().getFee() == null ? 0 : a.getDoctor().getFee();
        if (fee <= 0) return;
        Bill b = new Bill();
        b.setPatient(a.getPatient()); b.setAppointmentId(a.getId()); b.setAmount(fee);
        b.setDescription("Consultation with " + a.getDoctor().getUser().getName());
        repo.save(b);
    }

    public Bill pay(Long id, String mode) {
        Bill b = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bill not found"));
        if (me.is(User.Role.PATIENT) && !b.getPatient().getId().equals(me.patient().getId()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "This bill belongs to another patient");
        if (b.getStatus() == Bill.Status.PAID) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This bill is already paid");
        if (!mode.equals("ONLINE") && !mode.equals("CASH")) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Payment mode must be ONLINE or CASH");
        b.setStatus(Bill.Status.PAID); b.setPaymentMode(mode); b.setPaidDate(LocalDate.now());
        b.setReceiptNo("RCP-" + LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE) + "-" + String.format("%04d", b.getId()));
        return repo.save(b);
    }
}
