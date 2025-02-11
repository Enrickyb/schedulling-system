package com.example.scheduling.controllers;

import com.example.scheduling.dto.BusinessSettingsDTO;
import com.example.scheduling.services.BusinessSettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/business-settings")
@RequiredArgsConstructor
public class BusinessSettingsController {
    private final BusinessSettingsService settingsService;

    @GetMapping("/{businessId}")
    public ResponseEntity<BusinessSettingsDTO> getSettings(@PathVariable UUID businessId) {
        return ResponseEntity.ok(settingsService.getSettingsByBusinessId(businessId));
    }

    @PutMapping("/{businessId}")
    public ResponseEntity<BusinessSettingsDTO> updateSettings(
            @PathVariable UUID businessId,
            @RequestBody BusinessSettingsDTO settingsDTO
    ) {
        return ResponseEntity.ok(settingsService.updateSettings(businessId, settingsDTO));
    }
}
