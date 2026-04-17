package com.fixit.user.application.usecase;

import com.fixit.user.application.port.in.IUserServicePort;
import com.fixit.user.application.port.out.IPasswordEncoderPersistencePort;
import com.fixit.user.application.port.out.IUserPersistencePort;
import com.fixit.user.domain.model.Technician;
import com.fixit.user.domain.service.TechnicianDomainService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class UserUseCase implements IUserServicePort {

    private final IUserPersistencePort userPersistencePort;
    private final IPasswordEncoderPersistencePort passwordEncoderPort;
    private final TechnicianDomainService technicianDomainService;
    @Override
    public Technician createTechnician(Technician technician) {
        boolean emailExists = userPersistencePort.existsByEmail(technician.getUser().getEmail());
        boolean dniExists = userPersistencePort.existsByDni(technician.getUser().getDni());

        technicianDomainService.validateUniqueness(emailExists, dniExists);

        String encodedPassword = passwordEncoderPort.encodePassword(technician.getUser().getPassword());
        Technician technicianToSave = technicianDomainService.prepareTechnicianForRegistration(technician, encodedPassword);

        return userPersistencePort.saveTechnician(technicianToSave);
    }

    @Override
    public List<Technician> getAll() {
        return userPersistencePort.findAllTechnicians();
    }
}