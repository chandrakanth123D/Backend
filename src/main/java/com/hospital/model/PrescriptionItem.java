package com.hospital.model;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter @Setter @NoArgsConstructor
public class PrescriptionItem {
    private String medicine;
    private String dosage;
    private String duration;
}
