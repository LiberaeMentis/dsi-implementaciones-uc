package com.dsi.laboreos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dsi.laboreos.model.Estado;

@Repository
public interface IEstadoRepository extends JpaRepository<Estado, String> {
}

