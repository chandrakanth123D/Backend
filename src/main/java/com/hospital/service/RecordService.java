package com.hospital.service;

import com.hospital.dto.Dto.ConsultReq;
import com.hospital.model.*;
import com.hospital.model.User.Role;
import com.hospital.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service @RequiredArgsConstructor
public class RecordService {
    private final MedicalRecordRepository records;
    private final PrescriptionRepository prescriptions;
    private final AppointmentRepository appointmentRepo;
    private final AppointmentService appointments;
    private final BillService bills;
    private final CurrentUser me;

    public List<MedicalRecord> mine() { return records.findByPatientIdOrderByDateDescIdDesc(me.patient().getId()); }
    public List<Prescription> myPrescriptions() { return prescriptions.findByPatientIdOrderByDateDescIdDesc(me.patient().getId()); }

    public List<MedicalRecord> forPatient(Long patientId) { assertCanView(patientId); return records.findByPatientIdOrderByDateDescIdDesc(patientId); }
    public List<Prescription> prescriptionsForPatient(Long patientId) { assertCanView(patientId); return prescriptions.findByPatientIdOrderByDateDescIdDesc(patientId); }

    /** A doctor can open the history of patients they have an appointment with. */
    private void assertCanView(Long patientId) {
        if (me.is(Role.ADMIN)) return;
        if (me.is(Role.PATIENT) && me.patient().getId().equals(patientId)) return;
        if (me.is(Role.DOCTOR) && appointmentRepo.existsByDoctorIdAndPatientId(me.doctor().getId(), patientId)) return;
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only view history of your own patients");
    }

    /** Consultation: saves diagnosis + prescription, completes the appointment and raises the bill. */
    @Transactional
    public MedicalRecord consult(ConsultReq r) {
        Doctor doctor = me.doctor();
        Appointment a = appointments.get(r.appointmentId());
        if (!a.getDoctor().getId().equals(doctor.getId())) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "This is not your appointment");
        if (a.getStatus() != Appointment.Status.BOOKED) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This appointment is already closed");

        MedicalRecord m = new MedicalRecord();
        m.setPatient(a.getPatient()); m.setDoctor(doctor); m.setAppointmentId(a.getId());
        m.setDiagnosis(r.diagnosis()); m.setNotes(r.notes()); m.setFollowUp(r.followUp());
        records.save(m);

        List<PrescriptionItem> items = r.items() == null ? List.of()
                : r.items().stream().filter(i -> i.getMedicine() != null && !i.getMedicine().isBlank()).toList();
        if (!items.isEmpty()) {
            Prescription p = new Prescription();
            p.setPatient(a.getPatient()); p.setDoctor(doctor); p.setAppointmentId(a.getId()); p.setItems(new java.util.ArrayList<>(items));
            prescriptions.save(p);
        }
        a.setStatus(Appointment.Status.COMPLETED);
        appointments.save(a);
        bills.createForAppointment(a);
        return m;
    }

    public MedicalRecord update(Long id, ConsultReq r) {
        MedicalRecord m = records.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Record not found"));
        if (!m.getDoctor().getId().equals(me.doctor().getId())) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only the treating doctor can edit this record");
        m.setDiagnosis(r.diagnosis()); m.setNotes(r.notes()); m.setFollowUp(r.followUp());
        return records.save(m);
    }
}
