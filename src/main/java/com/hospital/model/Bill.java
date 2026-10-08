package com.hospital.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity @Table(name = "bills")
@Getter @Setter @NoArgsConstructor
public class Bill {
    public enum Status { PENDING, PAID }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Patient patient;
    private Long appointmentId;
    private String description;
    @Column(nullable = false)
    private Double amount;
    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;
    private String paymentMode;        // ONLINE or CASH
    private LocalDate billDate = LocalDate.now();
    private LocalDate paidDate;
    private String receiptNo;
}
