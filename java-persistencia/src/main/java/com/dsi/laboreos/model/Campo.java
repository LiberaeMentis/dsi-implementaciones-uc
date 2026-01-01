package com.dsi.laboreos.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "campos")
public class Campo {
    
    @Id
    @Column(nullable = false, unique = true, length = 100)
    private String nombre;
    
    @Column(nullable = false)
    private Double cantidadHectareas;
    
    @Column(nullable = false)
    private Boolean habilitado;
    
    // RELACIÓN UNIDIRECCIONAL: Campo -> Lote (0..*)
    // Lote NO conoce su Campo
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "campo_nombre")
    private List<Lote> lotes = new ArrayList<>();

    public Campo() {
    }

    public Campo(String nombre, Double cantidadHectareas, Boolean habilitado, List<Lote> lotes) {
        this.nombre = nombre;
        this.cantidadHectareas = cantidadHectareas;
        this.habilitado = habilitado;
        this.lotes = lotes != null ? lotes : new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Double getCantidadHectareas() {
        return cantidadHectareas;
    }

    public void setCantidadHectareas(Double cantidadHectareas) {
        this.cantidadHectareas = cantidadHectareas;
    }

    public Boolean getHabilitado() {
        return habilitado;
    }

    public void setHabilitado(Boolean habilitado) {
        this.habilitado = habilitado;
    }

    public List<Lote> getLotes() {
        return lotes;
    }

    public void setLotes(List<Lote> lotes) {
        this.lotes = lotes;
    }

    public Boolean estaHabilitado() {
        return habilitado;
    }

    public List<Lote> buscarLotes() {
        return lotes;
    }

    public List<Lote> buscarLotesProyCultivo() {
        return lotes.stream()
                .filter(Lote::tieneProyectoCultivoVigente)
                .collect(Collectors.toList());
    }

    public void mostrarLotes() {
    }

    public String mostrarCultivo(Lote lote) {
        return lote != null ? lote.mostrarCultivo() : null;
    }

    public Lote buscarLote(Integer numeroLote) {
        return buscarLotes().stream()
                .filter(l -> l.getNumero().equals(numeroLote))
                .findFirst()
                .orElse(null);
    }



    public List<Map<String, LocalDateTime>> buscarLaboreosRealizados(Lote lote) {
        return lote != null ? lote.buscarLaboreosRealizados() : new ArrayList<>();
    }

    public List<String[]> buscarTiposLaboreoParaCultivo(Lote lote) {
        return lote != null ? lote.buscarLaboreosParaCultivo() : new ArrayList<>();
    }

    // fechasPorLaboreo: clave = "numeroLote|tipoLaboreo|momentoLaboreo", valor = [fechaHoraInicio, fechaHoraFin]
    // empleadosPorLaboreo: clave = "numeroLote|tipoLaboreo|momentoLaboreo", valor = Empleado
    public void crearLaboreosParaProyecto(Map<String, LocalDateTime[]> fechasPorLaboreo, Map<String, Empleado> empleadosPorLaboreo, Map<Lote, List<OrdenDeLaboreo>> ordenesLaboreoPorLote) {
        ordenesLaboreoPorLote.forEach((lote, ordenesLaboreo) -> {
            Integer numeroLote = lote.getNumero();
            
            // Agrupar órdenes por sus fechas y empleado (pueden tener fechas y empleados diferentes)
            Map<String, List<OrdenDeLaboreo>> ordenesPorFechaYEmpleado = new HashMap<>();
            Map<String, LocalDateTime[]> fechasPorGrupo = new HashMap<>();
            Map<String, Empleado> empleadosPorGrupo = new HashMap<>();
            
            for (OrdenDeLaboreo orden : ordenesLaboreo) {
                String clave = numeroLote + "|" + orden.conocerTipoLaboreo().getNombre() + "|" + orden.conocerMomentoLaboreo().getNombre();
                LocalDateTime[] fechas = fechasPorLaboreo.get(clave);
                Empleado empleado = empleadosPorLaboreo.get(clave);
                if (fechas != null && fechas.length == 2 && empleado != null) {
                    // Crear clave para agrupar por fechas y empleado (inicio, fin y empleado)
                    String claveFechaYEmpleado = fechas[0].toString() + "|" + fechas[1].toString() + "|" + empleado.getNombre() + "|" + empleado.getApellido();
                    ordenesPorFechaYEmpleado.computeIfAbsent(claveFechaYEmpleado, k -> new ArrayList<>()).add(orden);
                    fechasPorGrupo.putIfAbsent(claveFechaYEmpleado, fechas);
                    empleadosPorGrupo.putIfAbsent(claveFechaYEmpleado, empleado);
                }
            }
            
            // Crear laboreos agrupados por fechas y empleado usando el método existente
            ordenesPorFechaYEmpleado.forEach((claveFechaYEmpleado, ordenesGrupo) -> {
                LocalDateTime[] fechas = fechasPorGrupo.get(claveFechaYEmpleado);
                Empleado empleado = empleadosPorGrupo.get(claveFechaYEmpleado);
                lote.crearLaboreos(fechas[0], fechas[1], empleado, ordenesGrupo);
            });
        });
    }
}

