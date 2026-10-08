package com.hospital.repository;

import com.hospital.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {
    List<MedicalRecord> findByPatientIdOrderByDateDescIdDesc(Long patientId);
}
