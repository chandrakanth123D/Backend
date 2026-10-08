package com.hospital.repository;

import com.hospital.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.*;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByPatientIdOrderByDateDescTimeDesc(Long patientId);
    List<Appointment> findByDoctorIdOrderByDateAscTimeAsc(Long doctorId);
    List<Appointment> findAllByOrderByDateDescTimeDesc();
    List<Appointment> findByDoctorIdAndDateAndStatus(Long doctorId, LocalDate date, Appointment.Status status);
    boolean existsByDoctorIdAndPatientId(Long doctorId, Long patientId);
    long countByDate(LocalDate date);
}
