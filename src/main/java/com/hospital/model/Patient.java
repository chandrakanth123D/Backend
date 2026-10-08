package com.hospital.model;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "patients")
@Getter @Setter @NoArgsConstructor
public class Patient {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "user_id", unique = true)
    private User user;
    private Integer age;
    private String gender;
    private String phone;
    private String address;
    private String bloodGroup;
}
