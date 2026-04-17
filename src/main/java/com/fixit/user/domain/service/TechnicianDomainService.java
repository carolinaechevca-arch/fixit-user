package com.fixit.user.domain.service;

import com.fixit.user.domain.enums.Role;
import com.fixit.user.domain.enums.TechnicianStatus;
import com.fixit.user.domain.exceptions.TechnicianAlreadyExistsException;
import com.fixit.user.domain.model.Technician;

public class TechnicianDomainService {

    public Technician prepareTechnicianForRegistration(Technician technician, String encodedPassword) {
        return technician.toBuilder()
                .user(technician.getUser().toBuilder()
                        .password(encodedPassword)
                        .role(Role.TECHNICIAN)
                        .build())
                .status(TechnicianStatus.AVAILABLE)
                .taskCount(0)
                .currentPoints(0)
                .build();
    }

    public void validateUniqueness(boolean emailExists, boolean dniExists) {
        if (emailExists) {
            throw new TechnicianAlreadyExistsException("Email already in use");
        }
        if (dniExists) {
            throw new TechnicianAlreadyExistsException("DNI already in use");
        }
    }
}