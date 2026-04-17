package com.fixit.user.application.port.out;

import com.fixit.user.domain.model.Technician;
import com.fixit.user.domain.model.User;

import java.util.List;

public interface IUserPersistencePort {
    Technician saveTechnician(Technician technician);

    boolean existsByEmail(String email);

    boolean existsByDni(String dni);

    User findByEmail(String email);

    List<Technician> findAllTechnicians();
}