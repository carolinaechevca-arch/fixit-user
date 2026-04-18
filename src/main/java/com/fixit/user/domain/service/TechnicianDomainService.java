package com.fixit.user.domain.service;

import com.fixit.user.domain.enums.Role;
import com.fixit.user.domain.enums.TechnicianCategory;
import com.fixit.user.domain.enums.TechnicianStatus;
import com.fixit.user.domain.exceptions.*;
import com.fixit.user.domain.model.Technician;
import com.fixit.user.domain.util.constants.DomainConstants;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class TechnicianDomainService {

    public Technician prepareTechnicianForRegistration(Technician technician, String encodedPassword) {
       log.info( "Preparing technician {} for registration with email {}", technician.getUser().getName(), technician.getUser().getEmail());
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
    public Technician validateTechnicianExists(Technician technicianOptional, Long id) {
        return Optional.ofNullable(technicianOptional).orElseThrow(() ->
                new TechnicianNotFoundException(
                        String.format(DomainConstants.TECHNICIAN_NOT_FOUND_MESSAGE, id)
                )
        );
    }

    public void validateTechnicianCanChangeCategory(Technician technician, TechnicianCategory newCategory) {
        if (technician.getStatus() != TechnicianStatus.AVAILABLE || technician.getTaskCount() > 0) {
            throw new TechnicianBusyException(
                    String.format(DomainConstants.TECHNICIAN_BUSY_MESSAGE,
                            technician.getUser().getName())
            );
        }
        if (technician.getCategory().equals(newCategory)) {
            throw new TechnicianSameCategoryException(
                    String.format(DomainConstants.TECHNICIAN_SAME_CATEGORY_MESSAGE,
                            technician.getUser().getId(), technician.getCategory())
            );
        }
    }
    public void validateTechnicianCanBeDeleted(Technician technician) {
        if (technician.getStatus() != TechnicianStatus.AVAILABLE  || technician.getTaskCount() > 0) {
            throw new TechnicianCannotBeDeletedException(
                    String.format(DomainConstants.TECHNICIAN_CANNOT_BE_DELETED_MESSAGE, technician.getUser().getId(), technician.getStatus()));
        }
    }
}