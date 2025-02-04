package com.example.scheduling.repositories;

import com.example.scheduling.models.Business;
import com.example.scheduling.models.GuestUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface GuestUserRepository extends JpaRepository<GuestUser, UUID> {
    Optional<GuestUser> findByEmail(String email);

    Optional<GuestUser> findByCreatedBy(Business business);


}
