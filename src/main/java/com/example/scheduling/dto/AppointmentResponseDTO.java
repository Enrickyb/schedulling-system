package com.example.scheduling.dto;

import com.example.scheduling.enums.AppointmentStatus;
import com.example.scheduling.enums.UserRole;
import com.example.scheduling.models.Appointment;

import java.time.LocalDateTime;
import java.util.UUID;

public record AppointmentResponseDTO(
        UUID id,
        UUID businessId,
        UUID customerId,
        UUID serviceId,
        LocalDateTime appointmentTime,
        AppointmentStatus status,
        UserRole canceledBy,
        String cancelationReason
) {
    public static AppointmentResponseDTO fromEntity(Appointment appointment) {
        return new AppointmentResponseDTO(
                appointment.getId(),
                appointment.getBusiness().getId(),
                appointment.getCustomer().getId(),
                appointment.getService().getId(),
                appointment.getAppointmentTime(),
                appointment.getStatus(),
                appointment.getCanceledBy(),
                appointment.getCancelationReason()
        );
    }
}
