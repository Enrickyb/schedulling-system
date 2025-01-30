package com.example.scheduling.controllers;


import com.example.scheduling.models.User;
import com.example.scheduling.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
