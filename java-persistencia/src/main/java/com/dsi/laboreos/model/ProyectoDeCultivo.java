package com.dsi.laboreos.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "proyectos_cultivo")
public class ProyectoDeCultivo {
    
    @Id
    @Column(nullable = false)
    private Integer numero;
    
    // RELACIÓN UNIDIRECCIONAL: ProyectoDeCultivo -> Cultivo (1)
    // Cultivo NO conoce sus ProyectosDeCultivo
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cultivo_nombre", nullable = false)
    private Cultivo cultivo;
    
    // RELACIÓN UNIDIRECCIONAL: ProyectoDeCultivo -> Estado (1)
    // Estado NO conoce sus ProyectosDeCultivo
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "estado_nombre", nullable = false)
    private Estado estado;
    
    @Column(nullable = false)
    private LocalDate fechaInicio;
    
    @Column
    private LocalDate fechaFin;
    
    @Column(length = 1000)
    private String observaciones;
    
    // RELACIÓN UNIDIRECCIONAL: ProyectoDeCultivo -> Laboreo (0..*)
    // Laboreo NO conoce su ProyectoDeCultivo
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "proyecto_cultivo_numero")
    private List<Laboreo> laboreos = new ArrayList<>();

    public ProyectoDeCultivo() {
    }

    public ProyectoDeCultivo(Integer numero, Cultivo cultivo, Estado estado, LocalDate fechaInicio, LocalDate fechaFin, String observaciones) {
        this.numero = numero;
        this.cultivo = cultivo;
        this.estado = estado;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.observaciones = observaciones;
        this.laboreos = new ArrayList<>();
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Cultivo getCultivo() {
        return cultivo;
    }

    public void setCultivo(Cultivo cultivo) {
        this.cultivo = cultivo;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public List<Laboreo> getLaboreos() {
        return laboreos;
    }

    public void setLaboreos(List<Laboreo> laboreos) {
        this.laboreos = laboreos;
    }

    public Estado conocerEstado() {
        return estado;
    }

    public Boolean estaVigente() {
        return estado != null && !estado.esFinal();
    }

    public List<Map<String, LocalDateTime>> buscarLaboreosRealizados() {
        return laboreos.stream()
                .map(Laboreo::mostrarLaboreo)
                .collect(Collectors.toList());
    }

    public List<OrdenDeLaboreo> buscarLaboreosPosibles() {
        return cultivo.conocerOrdenLaboreo();
    }

    public List<String[]> buscarLaboreosParaCultivo() {
        return cultivo.buscarTiposLaboreo();
    }

    public void agregarLaboreo(Laboreo laboreo) {
        this.laboreos.add(laboreo);
    }

    public String mostrarCultivo() {
        return cultivo.getNombre();
    }

    // Recibe los datos de un laboreo ya casteados y crea el laboreo
    public void crearLaboreos(LocalDateTime fechaInicio, LocalDateTime fechaFin, Empleado empleado, OrdenDeLaboreo orden) {
        Laboreo nuevoLaboreo = new Laboreo(
                fechaInicio.toLocalDate(),
                fechaFin.toLocalDate(),
                fechaInicio.toLocalTime(),
                fechaFin.toLocalTime(),
                empleado,
                orden
        );
        this.laboreos.add(nuevoLaboreo);
    }
}

