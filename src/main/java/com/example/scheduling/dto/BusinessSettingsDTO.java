package com.example.scheduling.dto;

import com.example.scheduling.models.BusinessSettings;
import java.time.LocalTime;
import java.util.UUID;

public record BusinessSettingsDTO(
        UUID id,
        LocalTime openingTime,
        LocalTime closingTime,
        int cancellationNoticeHours,
        boolean requireApproval
) {
    public static BusinessSettingsDTO fromEntity(BusinessSettings settings) {
        return new BusinessSettingsDTO(
                settings.getId(),
                settings.getOpeningTime(),
                settings.getClosingTime(),
                settings.getCancellationNoticeHours(),
                settings.isRequireApproval()
        );
    }
}
