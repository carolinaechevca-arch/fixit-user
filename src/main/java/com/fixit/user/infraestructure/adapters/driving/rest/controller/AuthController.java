package com.fixit.user.infraestructure.adapters.driving.rest.controller;

import com.fixit.user.application.port.in.IAuthServicePort;
import com.fixit.user.domain.model.AuthResponseModel;
import com.fixit.user.infraestructure.adapters.driving.rest.dto.request.auth.AuthRequestDto;
import com.fixit.user.infraestructure.adapters.driving.rest.dto.response.auth.AuthResponseDto;
import com.fixit.user.infraestructure.adapters.driving.rest.mapper.IAuthDtoMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints para autenticación de usuarios")
public class AuthController {

    private final IAuthServicePort authServicePort;
    private final IAuthDtoMapper authDtoMapper;

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión", description = "Retorna un JWT si las credenciales son válidas")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody AuthRequestDto authRequestDto) {

        AuthResponseModel responseModel = authServicePort.authenticate(authDtoMapper.toModel(authRequestDto)
        );

        return ResponseEntity.ok(authDtoMapper.toAuthResponseDto(responseModel));
    }


}