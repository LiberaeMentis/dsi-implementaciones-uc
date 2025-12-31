package com.dsi.laboreos.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dsi.laboreos.model.TipoSuelo;

@Repository
public interface ITipoSueloRepository extends JpaRepository<TipoSuelo, Integer> {
    Optional<TipoSuelo> findByNombre(String nombre);
}

