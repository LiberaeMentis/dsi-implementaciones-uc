package com.dsi.laboreos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dsi.laboreos.model.Empleado;
import com.dsi.laboreos.model.EmpleadoId;

@Repository
public interface IEmpleadoRepository extends JpaRepository<Empleado, EmpleadoId> {
}

