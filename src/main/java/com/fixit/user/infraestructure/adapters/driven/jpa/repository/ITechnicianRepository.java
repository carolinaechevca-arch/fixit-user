package com.fixit.user.infraestructure.adapters.driven.jpa.repository;

import com.fixit.user.infraestructure.adapters.driven.jpa.entity.TechnicianEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ITechnicianRepository extends JpaRepository<TechnicianEntity, Long> {
}