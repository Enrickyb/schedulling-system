package com.example.scheduling.repositories;


import com.example.scheduling.enums.AppointmentStatus;
import com.example.scheduling.models.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {
    List<Appointment> findByBusinessId(UUID businessId);
    List<Appointment> findByCustomerId(UUID customerId);

    //findByBusinessIdAndServiceIdAndStatusAndAppointmentTimeAfter
    List<Appointment> findByBusinessIdAndServiceIdAndStatusAndAppointmentTimeAfter(UUID businessId, UUID serviceId, AppointmentStatus status, LocalDateTime appointmentTime);

}

