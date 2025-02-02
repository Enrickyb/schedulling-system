package com.example.scheduling.controllers;

import com.example.scheduling.dto.GuestUserDTO;
import com.example.scheduling.models.Business;
import com.example.scheduling.models.GuestUser;
import com.example.scheduling.services.BusinessService;
import com.example.scheduling.services.GuestUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/guest")
public class GuestUserController {
    private final BusinessService businessService;
    private final GuestUserService guestUserService;

    public GuestUserController(BusinessService businessService, GuestUserService guestUserService) {
        this.businessService = businessService;
        this.guestUserService = guestUserService;
    }

    //create guest_user, get all guest_users, get guest_user by id, update guest_user by id, delete guest_user by id

    @PostMapping
    public ResponseEntity<?> createGuestUser(@RequestBody GuestUserDTO guestUserDTO) {

        if(guestUserDTO.name() == null || guestUserDTO.email() == null || guestUserDTO.phone() == null) {
            throw new IllegalArgumentException("Name, email and phone are required.");
        }

        if (guestUserDTO.created_by() == null) {
            return ResponseEntity.badRequest().body("The ID of the business creator (created_by) is required.");
        }

        GuestUser isExisting = guestUserService.getGuestUserByEmail(guestUserDTO.email());

        if (isExisting != null) {
            return ResponseEntity.badRequest().body("User already exists.");
        }

        Business business = businessService.getBusinessById(guestUserDTO.created_by());

        GuestUser guestUser = GuestUser.builder()
                .name(guestUserDTO.name())
                .email(guestUserDTO.email())
                .address(guestUserDTO.address())
                .cpf(guestUserDTO.cpf())
                .createdBy(business)
                .phone(guestUserDTO.phone())
                .build();

        return ResponseEntity.ok(guestUserService.registerGuestUser(guestUser));



    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllGuestUsers() {
        return ResponseEntity.ok(guestUserService.getAllGuestUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getGuestUserById(UUID id) {
        return ResponseEntity.ok(guestUserService.getGuestUserById(id));
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateGuestUserById(UUID id, GuestUserDTO guestUserDTO) {
        return ResponseEntity.ok(guestUserService.updateGuestUser(id, guestUserDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGuestUserById(UUID id) {
        return ResponseEntity.ok(guestUserService.deleteGuestUser(id));
    }

    @GetMapping("/business/{business_id}")
    public ResponseEntity<?> getGuestUserByBusinessId(@PathVariable UUID business_id) {
        return ResponseEntity.ok(guestUserService.getGuestUserByBusiness(business_id));
    }


}
