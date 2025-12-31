package com.dsi.laboreos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dsi.laboreos.model.MomentoLaboreo;

@Repository
public interface IMomentoLaboreoRepository extends JpaRepository<MomentoLaboreo, String> {
}

