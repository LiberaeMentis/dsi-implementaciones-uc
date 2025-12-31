package com.dsi.laboreos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dsi.laboreos.model.OrdenDeLaboreo;
import com.dsi.laboreos.model.OrdenDeLaboreoId;

@Repository
public interface IOrdenDeLaboreoRepository extends JpaRepository<OrdenDeLaboreo, OrdenDeLaboreoId> {
}

