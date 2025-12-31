package com.dsi.laboreos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dsi.laboreos.model.Laboreo;
import com.dsi.laboreos.model.LaboreoId;

@Repository
public interface ILaboreoRepository extends JpaRepository<Laboreo, LaboreoId> {
}

