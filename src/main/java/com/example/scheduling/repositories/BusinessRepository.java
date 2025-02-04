package com.example.scheduling.repositories;



import com.example.scheduling.models.Business;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BusinessRepository extends JpaRepository<Business, UUID> {
    Optional<Business> findByName(String name);
    Optional<List<Business>> findByOwnerId(UUID ownerId);
    @Query("SELECT b FROM Business b LEFT JOIN FETCH b.services WHERE b.id = :businessId")
    Optional<Business> findByIdWithServices(@Param("businessId") UUID businessId);
}
