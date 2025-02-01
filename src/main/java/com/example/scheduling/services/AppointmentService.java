package com.example.scheduling.services;


import com.example.scheduling.enums.AppointmentStatus;
import com.example.scheduling.enums.UserRole;
import com.example.scheduling.exceptions.InvalidAppointmentException;
import com.example.scheduling.models.Appointment;

import com.example.scheduling.repositories.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;

    public AppointmentService(AppointmentRepository appointmentRepository) { // Construtor manual
        this.appointmentRepository = appointmentRepository;
    }


    public Appointment scheduleAppointment(Appointment appointment) {
        if (appointment.getAppointmentTime().isBefore(LocalDateTime.now())) {
            throw new InvalidAppointmentException("A data do agendamento deve ser no futuro.");
        }
        appointment.setStatus(AppointmentStatus.SCHEDULED);
        return appointmentRepository.save(appointment);
    }

    public List<Appointment> getAppointmentsByBusiness(UUID businessId) {
        return appointmentRepository.findByBusinessId(businessId);
    }

    public List<Appointment> getAppointmentsByCustomer(UUID customerId) {
        return appointmentRepository.findByCustomerId(customerId);
    }

    public void cancelAppointment(UUID appointmentId, UserRole canceledBy, String cancelationReason) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado!"));

        if (appointment.getAppointmentTime().isBefore(LocalDateTime.now().plusHours(48))) {
            System.out.println(appointment.getAppointmentTime());
            throw new RuntimeException("Cancelamento permitido apenas com 48h de antecedência! agendado:" + appointment.getAppointmentTime() + " local: " + LocalDateTime.now());
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointment.setCanceled_by(canceledBy);
        appointment.setCancelation_reason(cancelationReason);
        appointmentRepository.save(appointment);
    }

    public void completeAppointment(UUID appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado!"));

        appointment.setStatus(AppointmentStatus.COMPLETED);
        appointmentRepository.save(appointment);
    }

    public void rescheduleAppointment(UUID appointmentId, LocalDateTime newAppointmentTime) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado!"));

        // 🔹 Só pode reagendar se o status for SCHEDULED
        if (appointment.getStatus() != AppointmentStatus.SCHEDULED) {
            throw new RuntimeException("Somente agendamentos com status 'SCHEDULED' podem ser reagendados!");
        }

        // 🔹 Nova data deve ser no futuro
        if (newAppointmentTime.isBefore(LocalDateTime.now())) {
            throw new RuntimeException("A nova data do agendamento deve ser no futuro!");
        }

        // 🔹 O reagendamento deve ser feito com pelo menos 48 horas de antecedência
        if (appointment.getAppointmentTime().isBefore(LocalDateTime.now().plusHours(48))) {
            throw new RuntimeException("Reagendamento permitido apenas com 48h de antecedência!");
        }

        appointment.setAppointmentTime(newAppointmentTime);
        appointmentRepository.save(appointment);
    }


    public Appointment getAppointmentById(UUID appointmentId) {
        return appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado!"));

    }

}
