package com.example.scheduling.services;

import com.example.scheduling.dto.BusinessSettingsDTO;
import com.example.scheduling.models.Business;
import com.example.scheduling.models.BusinessSettings;
import com.example.scheduling.repositories.BusinessSettingsRepository;
import com.example.scheduling.repositories.BusinessRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BusinessSettingsService {
    private final BusinessSettingsRepository settingsRepository;
    private final BusinessRepository businessRepository;

    public void createDefaultSettings(Business business) {
        if (settingsRepository.findByBusinessId(business.getId()).isEmpty()) {
            BusinessSettings settings = BusinessSettings.builder()
                    .business(business)
                    .openingTime(LocalTime.of(8, 0))   // 🕗 08:00
                    .closingTime(LocalTime.of(18, 0))  // 🕕 18:00
                    .cancellationNoticeHours(24)  // ⏳ 24h de antecedência para cancelamento
                    .requireApproval(false)  // ✅ Não requer aprovação manual
                    .build();

            settingsRepository.save(settings);
        }
    }

    public BusinessSettingsDTO getSettingsByBusinessId(UUID businessId) {
        BusinessSettings settings = settingsRepository.findByBusinessId(businessId)
                .orElseThrow(() -> new EntityNotFoundException("Configurações não encontradas para este negócio"));
        return BusinessSettingsDTO.fromEntity(settings);
    }

    public BusinessSettingsDTO updateSettings(UUID businessId, BusinessSettingsDTO settingsDTO) {
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new EntityNotFoundException("Negócio não encontrado"));

        BusinessSettings settings = settingsRepository.findByBusinessId(businessId)
                .orElse(new BusinessSettings());

        settings.setBusiness(business);
        settings.setOpeningTime(settingsDTO.openingTime());
        settings.setClosingTime(settingsDTO.closingTime());
        settings.setCancellationNoticeHours(settingsDTO.cancellationNoticeHours());
        settings.setRequireApproval(settingsDTO.requireApproval());

        settingsRepository.save(settings);
        return BusinessSettingsDTO.fromEntity(settings);
    }
}
