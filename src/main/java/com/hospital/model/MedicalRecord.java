package com.hospital.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity @Table(name = "medical_records")
@Getter @Setter @NoArgsConstructor
public class MedicalRecord {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Patient patient;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Doctor doctor;
    private Long appointmentId;
    @Column(name = "record_date")
    private LocalDate date = LocalDate.now();
    @Column(nullable = false)
    private String diagnosis;
    @Column(length = 4000)
    private String notes;
    private String followUp;
}
