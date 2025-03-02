package com.example.scheduling.repositories;

import com.example.scheduling.enums.ServiceStatus;
import com.example.scheduling.models.Services;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ServicesRepository extends JpaRepository<Services, UUID> {

    // BUSCAR os serviços que não foram deletados (isDeleted = 0)
    @Query("SELECT s FROM Services s WHERE s.isDeleted = 0")
    List<Services> findAllNonDeleted();

    // BUSCAR um serviço que não foi deletado (isDeleted = 0)
    @Query("SELECT s FROM Services s WHERE s.id = :id AND s.isDeleted = 0")
    Optional<Services> findNonDeletedById(UUID id);

    // BUSCAR serviços por businessId que não foram deletados (isDeleted = 0)
    @Query("SELECT s FROM Services s WHERE s.business.id = :businessId AND s.isDeleted = 0")
    List<Services> findByBusinessId(UUID businessId);

    // BUSCAR serviços por status
    List<Services> findByStatus(ServiceStatus status);

    // "Deletar" um serviço (soft delete) - setar isDeleted = 1 e deletedAt = now()
    @Modifying
    @Transactional
    @Query("UPDATE Services s SET s.isDeleted = 1, s.deletedAt = CURRENT_TIMESTAMP WHERE s.id = :id")
    void softDeleteById(UUID id);
}