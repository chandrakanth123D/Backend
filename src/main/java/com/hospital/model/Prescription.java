package com.hospital.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.*;

@Entity @Table(name = "prescriptions")
@Getter @Setter @NoArgsConstructor
public class Prescription {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Patient patient;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Doctor doctor;
    private Long appointmentId;
    @Column(name = "prescription_date")
    private LocalDate date = LocalDate.now();
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "prescription_items", joinColumns = @JoinColumn(name = "prescription_id"))
    private List<PrescriptionItem> items = new ArrayList<>();
}
