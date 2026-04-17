package com.fixit.user.application.port.in;

import com.fixit.user.domain.model.Technician;

import java.util.List;

public interface IUserServicePort {
    Technician createTechnician(Technician technicianInfo);
    List<Technician> getAll();

}
