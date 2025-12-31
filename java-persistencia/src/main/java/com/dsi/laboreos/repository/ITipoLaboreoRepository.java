package com.dsi.laboreos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dsi.laboreos.model.TipoLaboreo;

@Repository
public interface ITipoLaboreoRepository extends JpaRepository<TipoLaboreo, String> {
}

