package com.example.scheduling.repositories;

import com.example.scheduling.dto.AppointmentDTO;
import com.example.scheduling.dto.AuthDTO;
import com.example.scheduling.dto.UserProjection;
import com.example.scheduling.models.Appointment;
import com.example.scheduling.models.Business;
import com.example.scheduling.models.User;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    UserRepository userRepository;

    private User createUser(AuthDTO data) {
        User user = new User(data);
        entityManager.persist(user);
        return user;

    }



    @Test
    @DisplayName("Should find user by email")
    void findByEmailSuccess() {
        AuthDTO data = AuthDTO.builder()
                .email("user@gmail.com")
                .name("User")
                .password("123456")
                .phone("123456789")
                .address("Rua 1")
                .build();

        this.createUser(data);

        Optional<User> user = this.userRepository.findByEmail("user@gmail.com");

        assertThat(user.isPresent()).isTrue();

    }

    @Test
    @DisplayName("Should not find user by email")
    void findByEmailNotFound() {

        Optional<User> user = this.userRepository.findByEmail("user@gmail.com");

        assertThat(user.isEmpty()).isTrue();

    }


    @Test
    @DisplayName("Should find user by business id")
    void findByBusinessId() {
        //SELECT u FROM User u INNER JOIN Appointment ap ON u.id = ap.customer.id WHERE ap.business.id = :businessId

        AuthDTO data = AuthDTO.builder()
                .email("user@gmail.com")
                .name("User")
                .password("123456")
                .phone("123456789")
                .address("Rua 1")
                .build();

        User user = this.createUser(data);



        Appointment appointment = new Appointment();
        appointment.setCustomer(user);
        entityManager.persist(appointment);

        Business business = new Business();
        entityManager.persist(business);

        appointment.setBusiness(business);

        entityManager.persist(appointment);

        List<UserProjection> users = this.userRepository.findByBusinessId(business.getId());

        assertThat(users.size()).isEqualTo(1);
        assertThat(users.get(0).getId()).isEqualTo(user.getId());

    }


}