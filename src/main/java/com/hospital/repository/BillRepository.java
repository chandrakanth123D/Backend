package com.hospital.repository;

import com.hospital.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface BillRepository extends JpaRepository<Bill, Long> {
    List<Bill> findByPatientIdOrderByIdDesc(Long patientId);
    List<Bill> findAllByOrderByIdDesc();
    boolean existsByAppointmentId(Long appointmentId);
    long countByStatus(Bill.Status status);
}
