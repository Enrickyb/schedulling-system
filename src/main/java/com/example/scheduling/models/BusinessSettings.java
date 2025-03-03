package com.example.scheduling.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "business_settings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessSettings {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "business_id", nullable = false, unique = true)
    private Business business;

    @Column(nullable = false)
    private LocalTime openingTime;  // ⏰ Horário de abertura

    @Column(nullable = false)
    private LocalTime closingTime;  // ⏰ Horário de fechamento

    @Column(nullable = false)
    private int cancellationNoticeHours = 24;  // ⏳ Tempo mínimo para cancelar/reagendar (horas)

    @Column(nullable = false)
    private boolean requireApproval = false;  // ✅ Se precisa de aprovação antes de confirmar um agendamento

    //logo de perfil da empresa
    @Column(nullable = true)
    private String logo_url;

    //cor de primaria da empresa
    @Column(nullable = true)
    private String primary_color;

    //cor de secundaria da empresa
    @Column(nullable = true)
    private String secondary_color;

    //cor de terciaria da empresa
    @Column(nullable = true)
    private String tertiary_color;


    public List<LocalDateTime> getAvailableTimes(LocalDateTime openingTime, LocalDateTime closingTime, int serviceDuration, List<Appointment> appointments) {
        List<LocalDateTime> availableTimes = new ArrayList<>();
        LocalDateTime currentTime = openingTime;

        while (currentTime.plusMinutes(serviceDuration).isBefore(closingTime) || currentTime.plusMinutes(serviceDuration).isEqual(closingTime)) {
            boolean isAvailable = true;

            for (Appointment appointment : appointments) {
                LocalDateTime appointmentStart = appointment.getAppointmentTime();
                LocalDateTime appointmentEnd = appointmentStart.plusMinutes(appointment.getService().getDuration());

                // Verifica se há sobreposição de horários
                if (currentTime.isBefore(appointmentEnd) && currentTime.plusMinutes(serviceDuration).isAfter(appointmentStart)) {
                    isAvailable = false;
                    break;
                }
            }

            if (isAvailable) {
                availableTimes.add(currentTime);
            }

            currentTime = currentTime.plusMinutes(30); // Incrementa em 30 minutos
        }

        return availableTimes;
    }
}

