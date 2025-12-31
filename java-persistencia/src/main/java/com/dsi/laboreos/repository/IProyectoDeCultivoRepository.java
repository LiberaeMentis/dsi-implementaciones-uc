package com.dsi.laboreos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dsi.laboreos.model.ProyectoDeCultivo;

@Repository
public interface IProyectoDeCultivoRepository extends JpaRepository<ProyectoDeCultivo, Integer> {
}

