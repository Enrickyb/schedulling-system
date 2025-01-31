package com.example.scheduling.services;


import com.example.scheduling.enums.AppointmentStatus;
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

    public void cancelAppointment(UUID appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado!"));

        if (appointment.getAppointmentTime().isBefore(LocalDateTime.now().plusHours(48))) {
            System.out.println(appointment.getAppointmentTime());
            throw new RuntimeException("Cancelamento permitido apenas com 48h de antecedência! agendado:" + appointment.getAppointmentTime() + " local: " + LocalDateTime.now());
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);
    }

    public void completeAppointment(UUID appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado!"));

        appointment.setStatus(AppointmentStatus.COMPLETED);
        appointmentRepository.save(appointment);
    }
}
