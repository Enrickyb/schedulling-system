package com.example.scheduling.services;

import com.example.scheduling.enums.AppointmentStatus;
import com.example.scheduling.enums.UserRole;
import com.example.scheduling.exceptions.InvalidAppointmentException;
import com.example.scheduling.models.Appointment;
import com.example.scheduling.models.Business;
import com.example.scheduling.models.BusinessSettings;
import com.example.scheduling.repositories.AppointmentRepository;
import com.example.scheduling.repositories.BusinessSettingsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private BusinessSettingsRepository businessSettingsRepository;

    @InjectMocks
    private AppointmentService appointmentService;

    private Appointment appointment;
    private BusinessSettings businessSettings;
    private Business business;
    private UUID appointmentId;
    private UUID businessId;

    @BeforeEach
    void setUp() {
        businessId = UUID.randomUUID();
        business = new Business();
        business.setId(businessId);

        businessSettings = new BusinessSettings();
        businessSettings.setOpeningTime(LocalTime.of(8, 0));
        businessSettings.setClosingTime(LocalTime.of(18, 0));
        businessSettings.setRequireApproval(false);

        appointmentId = UUID.randomUUID();
        appointment = new Appointment();
        appointment.setId(appointmentId);
        appointment.setBusiness(business);
        appointment.setAppointmentTime(LocalDateTime.now().plusDays(2));
    }

    @Test
    void shouldScheduleAppointmentSuccessfully() {
        when(businessSettingsRepository.findByBusinessId(any())).thenReturn(Optional.of(businessSettings));
        when(appointmentRepository.save(any())).thenReturn(appointment);

        Appointment scheduledAppointment = appointmentService.scheduleAppointment(appointment);

        assertNotNull(scheduledAppointment);
        assertEquals(AppointmentStatus.SCHEDULED, scheduledAppointment.getStatus());
    }

    @Test
    void shouldThrowExceptionWhenSchedulingOutsideBusinessHours() {
        businessSettings.setOpeningTime(LocalTime.of(9, 0));
        businessSettings.setClosingTime(LocalTime.of(17, 0));

        appointment.setAppointmentTime(LocalDateTime.now().plusDays(1).withHour(8)); // Fora do horário
        when(businessSettingsRepository.findByBusinessId(any())).thenReturn(Optional.of(businessSettings));

        assertThrows(RuntimeException.class, () -> appointmentService.scheduleAppointment(appointment));
    }

    @Test
    void shouldThrowExceptionWhenSchedulingInPast() {
        appointment.setAppointmentTime(LocalDateTime.now().minusDays(1));
        when(businessSettingsRepository.findByBusinessId(any())).thenReturn(Optional.of(businessSettings));

        assertThrows(InvalidAppointmentException.class, () -> appointmentService.scheduleAppointment(appointment));
    }

    @Test
    void shouldCancelAppointmentSuccessfully() {
        when(appointmentRepository.findById(any())).thenReturn(Optional.of(appointment));

        appointmentService.cancelAppointment(appointmentId, UserRole.ADMIN, "Cliente cancelou");

        assertEquals(AppointmentStatus.CANCELLED, appointment.getStatus());
        assertEquals(UserRole.ADMIN, appointment.getCanceledBy());
    }

    @Test
    void shouldThrowExceptionWhenCancelingWithin24Hours() {
        appointment.setAppointmentTime(LocalDateTime.now().plusHours(12));
        when(appointmentRepository.findById(any())).thenReturn(Optional.of(appointment));

        assertThrows(RuntimeException.class, () -> appointmentService.cancelAppointment(appointmentId, UserRole.ADMIN, "Muito tarde"));
    }

    @Test
    void shouldCompleteAppointmentSuccessfully() {
        appointment.setStatus(AppointmentStatus.SCHEDULED);
        when(appointmentRepository.findById(any())).thenReturn(Optional.of(appointment));

        appointmentService.completeAppointment(appointmentId);

        assertEquals(AppointmentStatus.COMPLETED, appointment.getStatus());
    }

    @Test
    void shouldThrowExceptionWhenCompletingInvalidAppointment() {
        appointment.setStatus(AppointmentStatus.CANCELLED);
        when(appointmentRepository.findById(any())).thenReturn(Optional.of(appointment));

        assertThrows(RuntimeException.class, () -> appointmentService.completeAppointment(appointmentId));
    }

    @Test
    void shouldRescheduleAppointmentSuccessfully() {
        appointment.setStatus(AppointmentStatus.SCHEDULED);
        when(appointmentRepository.findById(any())).thenReturn(Optional.of(appointment));

        LocalDateTime newTime = LocalDateTime.now().plusDays(3);
        appointmentService.rescheduleAppointment(appointmentId, newTime);

        assertEquals(newTime, appointment.getAppointmentTime());
    }

    @Test
    void shouldThrowExceptionWhenReschedulingInPast() {
        when(appointmentRepository.findById(any())).thenReturn(Optional.of(appointment));

        LocalDateTime pastTime = LocalDateTime.now().minusDays(1);
        assertThrows(RuntimeException.class, () -> appointmentService.rescheduleAppointment(appointmentId, pastTime));
    }
}
