package com.example.scheduling.controllers;

import com.example.scheduling.dto.AppointmentDTO;
import com.example.scheduling.dto.AppointmentResponseDTO;
import com.example.scheduling.dto.CancelAppointmentDTO;
import com.example.scheduling.dto.RescheduleAppointmentDTO;
import com.example.scheduling.enums.AppointmentStatus;
import com.example.scheduling.enums.UserRole;
import com.example.scheduling.models.Appointment;
import com.example.scheduling.models.Business;
import com.example.scheduling.models.Services;
import com.example.scheduling.models.User;
import com.example.scheduling.services.AppointmentService;
import com.example.scheduling.services.BusinessService;
import com.example.scheduling.services.ServicesService;
import com.example.scheduling.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {
    private final AppointmentService appointmentService;
    private final UserService userService;
    private final BusinessService businessService;
    private final ServicesService servicesService;

    public AppointmentController(AppointmentService appointmentService, UserService userService, BusinessService businessService, ServicesService servicesService) { // Construtor manual
        this.appointmentService = appointmentService;
        this.userService = userService;
        this.businessService = businessService;
        this.servicesService = servicesService;
    }

    @PostMapping
    public ResponseEntity<?> scheduleAppointment(@RequestBody AppointmentDTO appointmentDTO) {
        User customer = userService.getUserById(appointmentDTO.customerId());
        Business business = businessService.getBusinessById(appointmentDTO.businessId());
        Services service = servicesService.getServiceById(appointmentDTO.serviceId());

        if (customer == null) {
            return ResponseEntity.badRequest().body("Cliente não encontrado.");
        }

        if (business == null) {
            return ResponseEntity.badRequest().body("Negócio não encontrado.");
        }

        if (service == null) {
            return ResponseEntity.badRequest().body("Serviço não encontrado.");
        }

        Appointment appointment = Appointment.builder()
                .customer(customer)
                .business(business)
                .service(service)
                .appointmentTime(appointmentDTO.appointmentTime())
                .status(AppointmentStatus.SCHEDULED)
                .build();

        appointment = appointmentService.scheduleAppointment(appointment);

        return ResponseEntity.ok(AppointmentResponseDTO.fromEntity(appointment));
    }

    @GetMapping("/business/{businessId}")
    public ResponseEntity<List<AppointmentResponseDTO>> getAppointmentsByBusiness(@PathVariable UUID businessId) {
        List<Appointment> appointment = appointmentService.getAppointmentsByBusiness(businessId);
        return ResponseEntity.ok(appointment.stream()
                .map(AppointmentResponseDTO::fromEntity)
                .toList());
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<AppointmentResponseDTO>> getAppointmentsByCustomer(@PathVariable UUID customerId) {
        List<Appointment> appointment = appointmentService.getAppointmentsByCustomer(customerId);
        return ResponseEntity.ok(appointment.stream().map(AppointmentResponseDTO::fromEntity).toList());
    }

    @PutMapping("/cancel/{appointmentId}")
    public ResponseEntity<String> cancelAppointment(@PathVariable UUID appointmentId,  @RequestBody CancelAppointmentDTO request) {
        try {
            appointmentService.cancelAppointment(appointmentId, request.canceledBy(), request.cancelationReason());
            return ResponseEntity.ok("Agendamento cancelado com sucesso!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/reschedule/{appointmentId}")
    public ResponseEntity<String> rescheduleAppointment(
            @PathVariable UUID appointmentId,
            @RequestBody RescheduleAppointmentDTO request
    ) {
        appointmentService.rescheduleAppointment(appointmentId, request.newAppointmentTime());
        return ResponseEntity.ok("Agendamento reagendado com sucesso!");
    }


    @PutMapping("/complete/{appointmentId}")
    public ResponseEntity<String> completeAppointment(@PathVariable UUID appointmentId) {
        try {
            appointmentService.completeAppointment(appointmentId);
            return ResponseEntity.ok("Agendamento concluído com sucesso!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{appointmentId}")
    public ResponseEntity<AppointmentResponseDTO> getAppointmentById(@PathVariable UUID appointmentId) {
        Appointment appointment = appointmentService.getAppointmentById(appointmentId);

        return ResponseEntity.ok(AppointmentResponseDTO.fromEntity(appointment));
    }



}
