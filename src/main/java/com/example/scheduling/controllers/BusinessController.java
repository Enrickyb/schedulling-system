package com.example.scheduling.controllers;

import com.example.scheduling.dto.BusinessDTO;
import com.example.scheduling.dto.BusinessResponseDTO;
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
                .cnpj(businessDTO.cnpj())
                .email(businessDTO.email())
                .build();

        return ResponseEntity.ok(businessService.createBusiness(business));
    }


    @GetMapping
    public ResponseEntity<List<BusinessResponseDTO>> getAllBusinesses() {
        List<Business> business = businessService.getAllBusinesses();
        return ResponseEntity.ok(business.stream().map(BusinessResponseDTO::fromEntity).toList());
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<BusinessResponseDTO>> getBusinessesByOwner(@PathVariable UUID ownerId) {
        List<Business> businesses = businessService.getBusinessesByOwner(ownerId);
        return ResponseEntity.ok(businesses.stream().map(BusinessResponseDTO::fromEntity).toList());
    }

    @GetMapping("/{businessId}")
    public ResponseEntity<BusinessResponseDTO> getBusinessById(@PathVariable UUID businessId) {
        Business business = businessService.getBusinessById(businessId);

        return Optional.ofNullable(business)
                .map(BusinessResponseDTO::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }




}
