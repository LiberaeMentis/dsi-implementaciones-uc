package com.dsi.laboreos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dsi.laboreos.model.Campo;

@Repository
public interface ICampoRepository extends JpaRepository<Campo, String> {
    List<Campo> findByHabilitado(Boolean habilitado);
}

