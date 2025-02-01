package com.example.scheduling.dto;

import com.example.scheduling.enums.UserRole;

public record CancelAppointmentDTO(UserRole canceledBy, String cancelationReason) {}