package com.hospital.dto;

import com.hospital.model.PrescriptionItem;
import jakarta.validation.constraints.*;
import java.time.*;
import java.util.List;

public class Dto {
    public record Register(@NotBlank String name, @NotBlank @Email String email,
                           @Size(min = 6, message = "Password must be at least 6 characters") String password,
                           Integer age, String gender, String phone, String address, String bloodGroup) {}
    public record Login(@NotBlank String email, @NotBlank String password) {}
    public record AuthResponse(String token, String role, String name, Long userId, Long profileId) {}
    public record DoctorReq(@NotBlank String name, @NotBlank @Email String email,
                            @Size(min = 6, message = "Password must be at least 6 characters") String password,
                            @NotBlank String specialization, String qualification, String phone, String availability, Double fee) {}
    public record AppointmentReq(@NotNull Long doctorId, Long patientId, @NotNull LocalDate date, @NotNull LocalTime time, String reason, String mode) {}
    public record Reschedule(@NotNull LocalDate date, @NotNull LocalTime time) {}
    public record ConsultReq(@NotNull Long appointmentId, @NotBlank String diagnosis, String notes, String followUp, List<PrescriptionItem> items) {}
    public record BillReq(@NotNull Long patientId, Long appointmentId, String description, @NotNull @Positive Double amount) {}
    public record PayReq(@NotBlank String paymentMode) {}
    public record PurchaseReq(@NotNull @Positive Integer quantity) {}
}
