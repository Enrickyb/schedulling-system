package com.example.scheduling.services;


import com.example.scheduling.enums.AppointmentStatus;
import com.example.scheduling.enums.UserRole;
import com.example.scheduling.exceptions.InvalidAppointmentException;
import com.example.scheduling.models.Appointment;

import com.example.scheduling.models.BusinessSettings;
import com.example.scheduling.repositories.AppointmentRepository;
import com.example.scheduling.repositories.BusinessSettingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final BusinessSettingsRepository businessSettingsRepository;

    public AppointmentService(AppointmentRepository appointmentRepository, BusinessSettingsRepository businessSettingsRepository) { // Construtor manual
        this.appointmentRepository = appointmentRepository;
        this.businessSettingsRepository = businessSettingsRepository;
    }


    public Appointment scheduleAppointment(Appointment appointment) {
        BusinessSettings settings = businessSettingsRepository.findByBusinessId(appointment.getBusiness().getId())
                .orElseThrow(() -> new RuntimeException("Configurações do negócio não encontradas"));

        // 🔹 Verificar horário de funcionamento
        LocalTime appointmentTime = appointment.getAppointmentTime().toLocalTime();
        if (appointmentTime.isBefore(settings.getOpeningTime()) || appointmentTime.isAfter(settings.getClosingTime())) {
            throw new RuntimeException("Agendamento fora do horário de funcionamento da empresa.");
        }


        // 🔹 Marcar como "PENDENTE" caso precise de aprovação
        if (settings.isRequireApproval()) {
            appointment.setStatus(AppointmentStatus.PENDING);
        } else {
            appointment.setStatus(AppointmentStatus.SCHEDULED);
        }

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

        //verificar se o agendamento ja foi concluido ou cancelado
        if (appointment.getStatus() == AppointmentStatus.COMPLETED || appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new RuntimeException("Agendamento já foi concluído ou cancelado!");
        }



        if (appointment.getAppointmentTime().isBefore(LocalDateTime.now().plusHours(24))) {
            System.out.println(appointment.getAppointmentTime());
            throw new RuntimeException("Cancelamento permitido apenas com 48h de antecedência! agendado:" + appointment.getAppointmentTime() + " local: " + LocalDateTime.now());
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointment.setCanceledBy(canceledBy);
        appointment.setCancelationReason(cancelationReason);
        appointmentRepository.save(appointment);
    }

    public void completeAppointment(UUID appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado!"));

        // 🔹 Só pode completar se o status for SCHEDULED
        if (appointment.getStatus() != AppointmentStatus.SCHEDULED) {
            throw new RuntimeException("Agendamento já concluido ou cancelado!");
        }

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

        // 🔹 O reagendamento deve ser feito com pelo menos 24 horas de antecedência
        if (appointment.getAppointmentTime().isBefore(LocalDateTime.now().plusHours(24))) {
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
