package com.example.scheduling.models;


import com.example.scheduling.enums.AppointmentStatus;
import com.example.scheduling.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "appointments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;


    @Column(nullable = false)
    private LocalDateTime appointmentTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private UserRole canceledBy;

    @Column(nullable = true)
    private String cancelationReason;


    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false) // 🔹 Um Appointment tem apenas UM Service
    private Services service;


    //created_at
    @Column(name = "created_at", nullable = true)
    @CreationTimestamp
    private LocalDateTime created_at;

    //updated_at
    @Column(name = "updated_at", nullable = true)
    @UpdateTimestamp
    private LocalDateTime updated_at;



    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", business=" + business +
                ", customer=" + customer +
                ", appointmentTime=" + appointmentTime +
                ", status=" + status +
                ", canceled_by=" + canceledBy +
                ", cancelation_reason='" + cancelationReason + '\'' +
                ", created_at=" + created_at +
                ", updated_at=" + updated_at +
                '}';
    }
}
