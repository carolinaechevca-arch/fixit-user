package com.fixit.user.application.usecase;

import com.fixit.user.application.port.in.IUserServicePort;
import com.fixit.user.application.port.out.IPasswordEncoderPersistencePort;
import com.fixit.user.application.port.out.ITaskFeignClientPort;
import com.fixit.user.application.port.out.IUserPersistencePort;
import com.fixit.user.domain.enums.TechnicianCategory;
import com.fixit.user.domain.model.Task;
import com.fixit.user.domain.model.Technician;
import com.fixit.user.domain.model.TechnicianUpdate;
import com.fixit.user.domain.model.TechnicianWorkload;
import com.fixit.user.domain.service.TechnicianDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class UserUseCase implements IUserServicePort {

    private final IUserPersistencePort userPersistencePort;
    private final IPasswordEncoderPersistencePort passwordEncoderPort;
    private final TechnicianDomainService technicianDomainService;
    private final ITaskFeignClientPort taskFeignClientPort;
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


    @Override
    public List<Technician> getTechniciansByCategory(TechnicianCategory category) {
        return userPersistencePort.findTechniciansByCategory(category);

    }

    @Override
    @Transactional
    public Technician updateTechnician(Long id, TechnicianUpdate technicianData) {
        Technician existing = userPersistencePort.findById(id);
        log.info("Técnico encontrado para actualización: {}", existing);
        Technician updated = existing.toBuilder()
                .category(technicianData.getCategory() != null ? technicianData.getCategory() : existing.getCategory())
                .status(technicianData.getStatus() != null ? technicianData.getStatus() : existing.getStatus())
                .taskCount(technicianData.getTaskCount() != null ? technicianData.getTaskCount() : existing.getTaskCount())
                .currentPoints(technicianData.getCurrentPoints() != null ? technicianData.getCurrentPoints() : existing.getCurrentPoints())
                .build();

        log.info("Técnico actualizado antes de persistir: {}", updated);
        return userPersistencePort.updateTechnician(updated);
    }

    @Override
    public TechnicianWorkload getTechnicianWorkload(Long id){
        Technician technician = technicianDomainService.validateTechnicianExists(userPersistencePort.findById(id), id);

        List<Task> tasks = taskFeignClientPort.findByTechnicianId(id);

        return TechnicianWorkload.createNew(technician, tasks);
    }

    @Override
        public Technician getTechnicianById(Long id) {
            return technicianDomainService.validateTechnicianExists(userPersistencePort.findById(id), id);
        }

}