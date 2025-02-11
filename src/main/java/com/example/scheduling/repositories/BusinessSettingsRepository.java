package com.example.scheduling.repositories;

import com.example.scheduling.models.BusinessSettings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BusinessSettingsRepository extends JpaRepository<BusinessSettings, UUID> {
    Optional<BusinessSettings> findByBusinessId(UUID businessId);
}
