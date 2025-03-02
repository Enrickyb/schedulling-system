package com.example.scheduling.controllers;


import com.example.scheduling.dto.UserDTO;
import com.example.scheduling.models.User;
import com.example.scheduling.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")

public class UserController {
    private final UserService userService;

    public UserController(UserService userService) { // Construtor manual
        this.userService = userService;
    }

    @GetMapping("/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        System.out.println("Buscando usuário com email: " + email);

        User user = userService.getUserByEmail(email);

        if (user == null) {
            System.out.println("Usuário não encontrado.");
            return ResponseEntity.status(404).body(null);
        }

        System.out.println("Usuário encontrado: " + user);
        return ResponseEntity.ok(user);
    }

    //catch users who have made an appointment with a company based on their ID
    @GetMapping("/business/{businessId}")
    public ResponseEntity<?> getUsersByBusinessId(@PathVariable UUID businessId) {
        System.out.println("Buscando usuários com businessId: " + businessId);

        List<UserDTO> users = userService.getUserByBusinessId(businessId);


        if (users == null || users.isEmpty()) {
            System.out.println("Usuários não encontrados.");
            return ResponseEntity.status(404).body(null);
        }

        System.out.println("Usuários encontrados: " + users);


        return ResponseEntity.ok(users);
    }
}
