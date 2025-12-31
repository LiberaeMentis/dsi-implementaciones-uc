package com.dsi.laboreos.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "laboreos")
@IdClass(LaboreoId.class)
public class Laboreo {
    
    @Id
    @Column(nullable = false)
    private LocalDate fechaInicio;
    
    @Id
    @Column(nullable = false)
    private LocalDate fechaFin;
    
    @Id
    @Column(nullable = false)
    private LocalTime horaInicio;
    
    @Id
    @Column(nullable = false)
    private LocalTime horaFin;
    
    @Id
    @Column(name = "empleado_nombre", nullable = false, length = 100)
    private String empleadoNombre;
    
    @Id
    @Column(name = "empleado_apellido", nullable = false, length = 100)
    private String empleadoApellido;
    
    @Id
    @Column(name = "orden_orden", nullable = false)
    private Integer ordenOrden;
    
    @Id
    @Column(name = "orden_tipo_laboreo_nombre", nullable = false, length = 100)
    private String ordenTipoLaboreoNombre;
    
    @Id
    @Column(name = "orden_momento_laboreo_nombre", nullable = false, length = 100)
    private String ordenMomentoLaboreoNombre;
    
    // RELACIÓN UNIDIRECCIONAL: Laboreo -> Empleado (1)
    // Empleado NO conoce sus Laboreos
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumns({
        @JoinColumn(name = "empleado_nombre", referencedColumnName = "nombre", nullable = false, insertable = false, updatable = false),
        @JoinColumn(name = "empleado_apellido", referencedColumnName = "apellido", nullable = false, insertable = false, updatable = false)
    })
    private Empleado empleado;
    
    // RELACIÓN UNIDIRECCIONAL: Laboreo -> OrdenDeLaboreo (1)
    // OrdenDeLaboreo NO conoce sus Laboreos
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumns({
        @JoinColumn(name = "orden_orden", referencedColumnName = "orden", nullable = false, insertable = false, updatable = false),
        @JoinColumn(name = "orden_tipo_laboreo_nombre", referencedColumnName = "tipo_laboreo_nombre", nullable = false, insertable = false, updatable = false),
        @JoinColumn(name = "orden_momento_laboreo_nombre", referencedColumnName = "momento_laboreo_nombre", nullable = false, insertable = false, updatable = false)
    })
    private OrdenDeLaboreo ordenLaboreo;

    public Laboreo() {
    }

    public Laboreo(LocalDate fechaInicio, LocalDate fechaFin, LocalTime horaInicio, LocalTime horaFin, Empleado empleado, OrdenDeLaboreo ordenLaboreo) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.empleadoNombre = empleado.getNombre();
        this.empleadoApellido = empleado.getApellido();
        this.ordenOrden = ordenLaboreo.getOrden();
        this.ordenTipoLaboreoNombre = ordenLaboreo.getTipoLaboreoNombre();
        this.ordenMomentoLaboreoNombre = ordenLaboreo.getMomentoLaboreoNombre();
        this.empleado = empleado;
        this.ordenLaboreo = ordenLaboreo;
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

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
        if (empleado != null) {
            this.empleadoNombre = empleado.getNombre();
            this.empleadoApellido = empleado.getApellido();
        }
    }

    public String getEmpleadoNombre() {
        return empleadoNombre;
    }

    public void setEmpleadoNombre(String empleadoNombre) {
        this.empleadoNombre = empleadoNombre;
    }

    public String getEmpleadoApellido() {
        return empleadoApellido;
    }

    public void setEmpleadoApellido(String empleadoApellido) {
        this.empleadoApellido = empleadoApellido;
    }

    public Integer getOrdenOrden() {
        return ordenOrden;
    }

    public void setOrdenOrden(Integer ordenOrden) {
        this.ordenOrden = ordenOrden;
    }

    public String getOrdenTipoLaboreoNombre() {
        return ordenTipoLaboreoNombre;
    }

    public void setOrdenTipoLaboreoNombre(String ordenTipoLaboreoNombre) {
        this.ordenTipoLaboreoNombre = ordenTipoLaboreoNombre;
    }

    public String getOrdenMomentoLaboreoNombre() {
        return ordenMomentoLaboreoNombre;
    }

    public void setOrdenMomentoLaboreoNombre(String ordenMomentoLaboreoNombre) {
        this.ordenMomentoLaboreoNombre = ordenMomentoLaboreoNombre;
    }

    public OrdenDeLaboreo getOrdenLaboreo() {
        return ordenLaboreo;
    }

    public void setOrdenLaboreo(OrdenDeLaboreo ordenLaboreo) {
        this.ordenLaboreo = ordenLaboreo;
        if (ordenLaboreo != null) {
            this.ordenOrden = ordenLaboreo.getOrden();
            this.ordenTipoLaboreoNombre = ordenLaboreo.getTipoLaboreoNombre();
            this.ordenMomentoLaboreoNombre = ordenLaboreo.getMomentoLaboreoNombre();
        }
    }

    public Empleado conocerEmpleado() {
        return empleado;
    }

    public OrdenDeLaboreo conocerOrdenLaboreo() {
        return ordenLaboreo;
    }

    public Map<String, LocalDateTime> mostrarLaboreo() {
        LocalDateTime fechaCompleta = LocalDateTime.of(fechaFin, horaFin);
        Map<String, LocalDateTime> info = new HashMap<>();
        String nombreTipoLaboreo = ordenLaboreo.mostrarLaboreoParaCultivo()[0];
        info.put(nombreTipoLaboreo, fechaCompleta);
        return info;
    }
}

