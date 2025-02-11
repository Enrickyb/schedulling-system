package com.example.scheduling.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
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
}

