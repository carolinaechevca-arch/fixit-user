package com.fixit.user.infraestructure.adapters.driving.rest.controller;

import com.fixit.user.application.port.in.IUserServicePort;
import com.fixit.user.domain.model.Technician;
import com.fixit.user.infraestructure.adapters.driving.rest.dto.request.UserRequest;
import com.fixit.user.infraestructure.adapters.driving.rest.dto.response.user.TechnicianResponse;
import com.fixit.user.infraestructure.adapters.driving.rest.mapper.IUserRestMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "Endpoints for registering and managing users")
public class TechnicianController {

    private final IUserServicePort userServicePort;
    private final IUserRestMapper userRestMapper;

    @PostMapping("/technician")
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
}