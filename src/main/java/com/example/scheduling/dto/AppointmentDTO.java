package com.example.scheduling.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record AppointmentDTO(
        UUID businessId,
        UUID customerId,
        UUID serviceId,
        LocalDateTime appointmentTime
) {

}
