package com.example.scheduling.controllers;

import com.example.scheduling.dto.BusinessDTO;
import com.example.scheduling.models.Business;
import com.example.scheduling.models.User;
import com.example.scheduling.services.BusinessService;
import com.example.scheduling.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/businesses")

public class BusinessController {
    private final BusinessService businessService;
    private final UserService userService;

    public BusinessController(BusinessService businessService, UserService userService) { // Construtor manual
        this.businessService = businessService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> createBusiness(@RequestBody BusinessDTO businessDTO) {
        if (businessDTO.ownerId() == null) {
            return ResponseEntity.badRequest().body("O ID do proprietário (ownerId) é obrigatório.");
        }

        User owner = userService.getUserById(businessDTO.ownerId());

        if (owner == null) {
            return ResponseEntity.badRequest().body("Usuário dono do negócio não encontrado.");
        }

        Business business = Business.builder()
                .owner(owner) // Agora garantimos que o owner tem um ID válido
                .name(businessDTO.name())
                .description(businessDTO.description())
                .phone(businessDTO.phone())
                .address(businessDTO.address())
                .build();

        return ResponseEntity.ok(businessService.createBusiness(business));
    }


    @GetMapping
    public ResponseEntity<List<Business>> getAllBusinesses() {
        return ResponseEntity.ok(businessService.getAllBusinesses());
    }

    @GetMapping("/{businessId}")
    public ResponseEntity<Business> getBusinessById(@PathVariable UUID businessId) {
        Business business = businessService.getBusinessById(businessId);

        return ResponseEntity.ok(business);
    }
}
