package com.hospital.model;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "doctors")
@Getter @Setter @NoArgsConstructor
public class Doctor {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "user_id", unique = true)
    private User user;
    @Column(nullable = false)
    private String specialization;
    private String qualification;
    private String phone;
    private String availability;
    private Double fee;
}
