package com.hospital.service;

import com.hospital.dto.Dto.*;
import com.hospital.model.*;
import com.hospital.model.Appointment.Status;
import com.hospital.model.User.Role;
import com.hospital.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.time.*;
import java.util.List;

@Service @RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepository repo;
    private final DoctorService doctorService;
    private final PatientService patientService;
    private final CurrentUser me;

    public List<Appointment> list() {
        return switch (me.user().getRole()) {
            case PATIENT -> repo.findByPatientIdOrderByDateDescTimeDesc(me.patient().getId());
            case DOCTOR -> repo.findByDoctorIdOrderByDateAscTimeAsc(me.doctor().getId());
            case ADMIN -> repo.findAllByOrderByDateDescTimeDesc();
        };
    }

    public Appointment get(Long id) {
        Appointment a = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Appointment not found"));
        assertParticipant(a);
        return a;
    }

    public List<String> bookedSlots(Long doctorId, LocalDate date) {
        return repo.findByDoctorIdAndDateAndStatus(doctorId, date, Status.BOOKED).stream()
                .map(a -> a.getTime().toString().substring(0, 5)).toList();
    }

    public Appointment book(AppointmentReq r) {
        Patient patient = me.is(Role.ADMIN) ? patientService.get(r.patientId()) : me.patient();
        Doctor doctor = doctorService.get(r.doctorId());
        checkSlot(doctor.getId(), r.date(), r.time(), null);
        Appointment a = new Appointment();
        a.setPatient(patient); a.setDoctor(doctor); a.setDate(r.date()); a.setTime(r.time());
        a.setReason(r.reason()); a.setMode("ONLINE".equals(r.mode()) ? "ONLINE" : "IN_PERSON");
        return repo.save(a);
    }

    public Appointment reschedule(Long id, Reschedule r) {
        Appointment a = get(id);
        if (me.is(Role.DOCTOR)) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only patients and admins can reschedule");
        requireBooked(a);
        checkSlot(a.getDoctor().getId(), r.date(), r.time(), a.getId());
        a.setDate(r.date()); a.setTime(r.time());
        return repo.save(a);
    }

    public Appointment cancel(Long id) {
        Appointment a = get(id);
        requireBooked(a);
        a.setStatus(Status.CANCELLED);
        return repo.save(a);
    }

    private void requireBooked(Appointment a) {
        if (a.getStatus() != Status.BOOKED) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only booked appointments can be changed");
    }

    private void checkSlot(Long doctorId, LocalDate date, LocalTime time, Long ignoreId) {
        if (LocalDateTime.of(date, time).isBefore(LocalDateTime.now()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Choose a date and time in the future");
        boolean taken = repo.findByDoctorIdAndDateAndStatus(doctorId, date, Status.BOOKED).stream()
                .anyMatch(x -> x.getTime().equals(time) && !x.getId().equals(ignoreId));
        if (taken) throw new ResponseStatusException(HttpStatus.CONFLICT, "That time is already booked. Pick another slot");
    }

    private void assertParticipant(Appointment a) {
        User u = me.user();
        boolean ok = u.getRole() == Role.ADMIN
                || (u.getRole() == Role.PATIENT && a.getPatient().getUser().getId().equals(u.getId()))
                || (u.getRole() == Role.DOCTOR && a.getDoctor().getUser().getId().equals(u.getId()));
        if (!ok) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "This appointment belongs to someone else");
    }

    public Appointment save(Appointment a) { return repo.save(a); }
}
