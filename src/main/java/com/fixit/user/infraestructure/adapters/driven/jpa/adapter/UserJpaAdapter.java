package com.fixit.user.infraestructure.adapters.driven.jpa.adapter;

import com.fixit.user.application.port.out.IUserPersistencePort;
import com.fixit.user.domain.model.Technician;
import com.fixit.user.domain.model.User;
import com.fixit.user.infraestructure.adapters.driven.jpa.entity.TechnicianEntity;
import com.fixit.user.infraestructure.adapters.driven.jpa.entity.UserEntity;
import com.fixit.user.infraestructure.adapters.driven.jpa.mapper.IUserEntityMapper;
import com.fixit.user.infraestructure.adapters.driven.jpa.repository.ITechnicianRepository;
import com.fixit.user.infraestructure.adapters.driven.jpa.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserJpaAdapter implements IUserPersistencePort {

    private final IUserRepository userRepository;
    private final ITechnicianRepository technicianRepository;
    private final IUserEntityMapper mapper;

    @Override
    @Transactional
    public Technician saveTechnician(Technician technician) {
        UserEntity userEntity = mapper.toUserEntity(technician.getUser());
        UserEntity savedUser = userRepository.save(userEntity);

        TechnicianEntity techEntity = mapper.toTechEntity(technician);

        techEntity.setUser(savedUser);

        TechnicianEntity savedTech = technicianRepository.save(techEntity);

        return mapper.toTechDomain(savedTech);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByDni(String dni) {
        return userRepository.existsByDni(dni);
    }

    @Override
    public User findByEmail(String email) {
        return mapper.toUserDomain(userRepository.findByEmail(email));
    }

    @Override
    public List<Technician> findAllTechnicians() {
        return technicianRepository.findAll().stream()
                .map(mapper::toTechDomain)
                .toList();
    }
}