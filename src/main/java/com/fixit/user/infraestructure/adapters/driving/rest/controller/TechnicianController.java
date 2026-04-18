package com.fixit.user.infraestructure.adapters.driving.rest.controller;

import com.fixit.user.application.port.in.IUserServicePort;
import com.fixit.user.domain.enums.TechnicianCategory;
import com.fixit.user.domain.model.Technician;
import com.fixit.user.domain.model.TechnicianUpdate;
import com.fixit.user.domain.model.TechnicianWorkload;
import com.fixit.user.infraestructure.adapters.driving.rest.dto.request.TechnicianUpdatePutRequest;
import com.fixit.user.infraestructure.adapters.driving.rest.dto.request.UserRequest;
import com.fixit.user.infraestructure.adapters.driving.rest.dto.response.technician.TechnicianResponse;
import com.fixit.user.infraestructure.adapters.driving.rest.dto.response.technician.TechnicianWorkloadResponse;
import com.fixit.user.infraestructure.adapters.driving.rest.mapper.IUserRestMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/technicians")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "Endpoints for registering and managing users")
public class TechnicianController {

    private final IUserServicePort userServicePort;
    private final IUserRestMapper userRestMapper;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<TechnicianResponse> createTechnician(@Valid @RequestBody UserRequest request) {
        Technician technicianDomain = userRestMapper.toTechnician(request);
        Technician savedTechnician = userServicePort.createTechnician(technicianDomain);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userRestMapper.toTechnicianResponse(savedTechnician));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all technicians", description = "Only accessible by ADMIN role.")
    public ResponseEntity<List<TechnicianResponse>> getAllTechnicians() {
        return ResponseEntity.ok(
                userServicePort.getAll()
                        .stream()
                        .map(userRestMapper::toTechnicianResponse)
                        .toList()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get technician by ID", description = "Only accessible by ADMIN role.")
    public ResponseEntity<TechnicianResponse> getTechnicianById(@PathVariable Long id){
        Technician technician = userServicePort.getTechnicianById(id);
        return ResponseEntity.ok(userRestMapper.toTechnicianResponse(technician));
    }

    @GetMapping("/category/{category}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get technicians by category", description = "Only accessible by ADMIN role.")
    public ResponseEntity<List<TechnicianResponse>> getTechniciansByCategory(@PathVariable TechnicianCategory category) {
        return ResponseEntity.ok(
                userServicePort.getTechniciansByCategory(category)
                        .stream()
                        .map(userRestMapper::toTechnicianResponse)
                        .toList()
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TECHNICIAN')")
    public ResponseEntity<TechnicianResponse> updateTechnician(
            @PathVariable Long id,
            @RequestBody TechnicianUpdatePutRequest request) { // Usamos el DTO de entrada

        TechnicianUpdate technician = userRestMapper.toTechnicianUpdate(request);
        Technician updated = userServicePort.updateTechnician(id, technician);

        return ResponseEntity.ok(userRestMapper.toTechnicianResponse(updated));
    }

    @GetMapping("/{id}/workload")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TECHNICIAN')") // Permite acceso a ADMIN y TECHNICIAN
    @Operation(summary = "Get technician workload and capacity")
    public ResponseEntity<TechnicianWorkloadResponse> getTechnicianWorkload(@PathVariable Long id) {
        TechnicianWorkload workload = userServicePort.getTechnicianWorkload(id);
        return ResponseEntity.ok(userRestMapper.toWorkloadResponse(workload));

    }

}