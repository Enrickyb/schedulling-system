package com.example.scheduling.controllers;

import com.example.scheduling.dto.AppointmentDTO;
import com.example.scheduling.dto.CancelAppointmentDTO;
import com.example.scheduling.dto.RescheduleAppointmentDTO;
import com.example.scheduling.enums.AppointmentStatus;
import com.example.scheduling.enums.UserRole;
import com.example.scheduling.models.Appointment;
import com.example.scheduling.models.Business;
import com.example.scheduling.models.User;
import com.example.scheduling.services.AppointmentService;
import com.example.scheduling.services.BusinessService;
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

    public AppointmentController(AppointmentService appointmentService, UserService userService, BusinessService businessService) { // Construtor manual
        this.appointmentService = appointmentService;
        this.userService = userService;
        this.businessService = businessService;
    }

    @PostMapping
    public ResponseEntity<?> scheduleAppointment(@RequestBody AppointmentDTO appointmentDTO) {
        User customer = userService.getUserById(appointmentDTO.customerId());
        Business business = businessService.getBusinessById(appointmentDTO.businessId());

        if (customer == null) {
            return ResponseEntity.badRequest().body("Cliente não encontrado.");
        }

        if (business == null) {
            return ResponseEntity.badRequest().body("Negócio não encontrado.");
        }

        Appointment appointment = Appointment.builder()
                .customer(customer)
                .business(business)
                .service(appointmentDTO.service())
                .appointmentTime(appointmentDTO.appointmentTime())
                .status(AppointmentStatus.SCHEDULED)
                .build();

        return ResponseEntity.ok(appointmentService.scheduleAppointment(appointment));
    }

    @GetMapping("/business/{businessId}")
    public ResponseEntity<List<Appointment>> getAppointmentsByBusiness(@PathVariable UUID businessId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByBusiness(businessId));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Appointment>> getAppointmentsByCustomer(@PathVariable UUID customerId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByCustomer(customerId));
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
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable UUID appointmentId) {
        Appointment appointment = appointmentService.getAppointmentById(appointmentId);

        return ResponseEntity.ok(appointment);
    }

}
