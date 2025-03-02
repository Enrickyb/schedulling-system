package com.example.scheduling.repositories;

import com.example.scheduling.dto.UserProjection;
import com.example.scheduling.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);



    @Query("SELECT u FROM User u INNER JOIN Appointment ap ON u.id = ap.customer.id WHERE ap.business.id = :businessId")
    List<UserProjection> findByBusinessId(UUID businessId);

}
