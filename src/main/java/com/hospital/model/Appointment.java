package com.hospital.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.*;

@Entity @Table(name = "appointments")
@Getter @Setter @NoArgsConstructor
public class Appointment {
    public enum Status { BOOKED, COMPLETED, CANCELLED }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Patient patient;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Doctor doctor;
    @Column(name = "appointment_date", nullable = false)
    private LocalDate date;
    @Column(name = "appointment_time", nullable = false)
    private LocalTime time;
    private String reason;
    private String mode;               // IN_PERSON or ONLINE
    @Enumerated(EnumType.STRING)
    private Status status = Status.BOOKED;
    private LocalDateTime createdAt = LocalDateTime.now();
}
