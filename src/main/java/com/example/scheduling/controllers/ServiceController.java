package com.example.scheduling.controllers;

import com.example.scheduling.dto.ServiceDTO;
import com.example.scheduling.models.Services;
import com.example.scheduling.services.ServicesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
public class ServiceController {

    private final ServicesService servicesService;

    @GetMapping
    public ResponseEntity<List<ServiceDTO>> getAllServices() {
        List<Services> services = servicesService.getAllServices();
        return ResponseEntity.ok(services.stream().map(ServiceDTO::fromEntity).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceDTO> getServiceById(@PathVariable UUID id) {
        Services service = servicesService.getServiceById(id);
        return ResponseEntity.ok(ServiceDTO.fromEntity(service));
    }

    @GetMapping("/active")
    public ResponseEntity<List<ServiceDTO>> getActiveServices() {
        List<Services> services = servicesService.getActiveServices();
        return ResponseEntity.ok(services.stream().map(ServiceDTO::fromEntity).toList());
    }

    @PostMapping("/{businessId}")
    public ResponseEntity<ServiceDTO> createService(
            @PathVariable UUID businessId,
            @RequestBody Services service) {
        Services services = servicesService.createService(businessId, service);
        return ResponseEntity.ok(ServiceDTO.fromEntity(services));

    }

    @PutMapping("/{id}")
    public ResponseEntity<Services> updateService(@PathVariable UUID id, @RequestBody Services updatedService) {
        return ResponseEntity.ok(servicesService.updateService(id, updatedService));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteService(@PathVariable UUID id) {
        servicesService.deleteService(id);
        return ResponseEntity.ok("Serviço excluído com sucesso!");
    }

    @PostMapping("assign/{businessId}")
    public ResponseEntity<String> assignServiceToBusiness( @PathVariable UUID businessId,
                                                           @RequestBody List<UUID> serviceIds) {
        servicesService.assignServicesToBusiness(serviceIds, businessId);
        return ResponseEntity.ok("Serviços atribuídos à empresa com sucesso!");
    }


    @GetMapping("/business/{businessId}")
    public ResponseEntity<List<ServiceDTO>> getServicesByBusiness(@PathVariable UUID businessId) {
        List<Services> services = servicesService.getServicesByBusiness(businessId);
        return ResponseEntity.ok(services.stream().map(ServiceDTO::fromEntity).toList());
    }
}
