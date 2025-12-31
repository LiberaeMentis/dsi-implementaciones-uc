package com.dsi.laboreos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dsi.laboreos.model.Lote;

@Repository
public interface ILoteRepository extends JpaRepository<Lote, Integer> {
}

