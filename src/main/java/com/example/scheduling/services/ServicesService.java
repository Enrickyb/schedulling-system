package com.example.scheduling.services;

import com.example.scheduling.enums.ServiceStatus;
import com.example.scheduling.models.Appointment;
import com.example.scheduling.models.Business;
import com.example.scheduling.models.Services;
import com.example.scheduling.repositories.AppointmentRepository;
import com.example.scheduling.repositories.BusinessRepository;
import com.example.scheduling.repositories.ServicesRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServicesService {

    private final ServicesRepository servicesRepository;
    private final BusinessRepository businessRepository;
    private final AppointmentRepository appointmentRepository;

    public List<Services> getAllServices() {
        return servicesRepository.findAllNonDeleted();
    }

    public Services getServiceById(UUID id) {
        return servicesRepository.findNonDeletedById(id)
                .orElseThrow(() -> new EntityNotFoundException("Serviço não encontrado!"));
    }

    public List<Services> getActiveServices() {
        return servicesRepository.findByStatus(ServiceStatus.ACTIVE);
    }

    public Services createService(UUID businessId, Services service) {
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada!"));

        service.setBusiness(business);  // 🔹 Associa o serviço à empresa
        return servicesRepository.save(service);
    }

    public Services updateService(UUID id, Services updatedService) {
        Services existingService = getServiceById(id);

        existingService.setName(updatedService.getName());
        existingService.setDescription(updatedService.getDescription());
        existingService.setPrice(updatedService.getPrice());
        existingService.setDuration(updatedService.getDuration());
        existingService.setType(updatedService.getType());
        existingService.setStatus(updatedService.getStatus());

        return servicesRepository.save(existingService);
    }

    public void deleteService(UUID id) {

        servicesRepository.softDeleteById(id);
    }



    public void assignServicesToBusiness(List<UUID> serviceIds, UUID businessId) {
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada!"));

        Set<Services> services = serviceIds.stream()
                .map(id -> servicesRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Serviço com ID " + id + " não encontrado!")))
                .collect(Collectors.toSet());

        business.getServices().addAll(services);
        businessRepository.save(business);
    }

    public List<Services> getServicesByBusiness(UUID businessId) {
        return servicesRepository.findByBusinessId(businessId);
    }




}
