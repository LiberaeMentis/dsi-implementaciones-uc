package com.dsi.laboreos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dsi.laboreos.model.Cultivo;

@Repository
public interface ICultivoRepository extends JpaRepository<Cultivo, String> {
}

