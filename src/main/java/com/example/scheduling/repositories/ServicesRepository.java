package com.example.scheduling.repositories;

import com.example.scheduling.enums.ServiceStatus;
import com.example.scheduling.enums.ServiceType;
import com.example.scheduling.models.Business;
import com.example.scheduling.models.Services;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface ServicesRepository extends JpaRepository<Services, UUID> {
    List<Services> findByBusinessId(UUID businessId);
    List<Services> findByStatus(ServiceStatus status);


}
