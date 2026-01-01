package com.dsi.laboreos.model;

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

    public List<String[]> buscarTipoLaboreosParaCultivo(Lote lote) {
        return lote != null ? lote.buscarLaboreosParaCultivo() : new ArrayList<>();
    }

    /**
     * Recibe laboreos individuales por lote como arrays de Object.
     * Cada array contiene: [fechaInicio, fechaFin, empleado, orden]
     * Hace el cast a los tipos correctos y delega al lote la creación.
     * 
     * @param laboreosPorLote Mapa donde cada clave es un Lote y el valor es una lista
     *                        de arrays Object[] con la información de cada laboreo a crear
     */
    public void crearLaboreosParaProyecto(Map<Lote, List<Object[]>> laboreosPorLote) {
        // CICLO EXTERNO: Iterar sobre cada lote y su lista de laboreos
        laboreosPorLote.forEach((lote, laboreos) -> {
            // CICLO INTERNO: Para cada lote, procesar todos sus laboreos
            // Cada laboreo viene como un Object[] que necesita ser casteado a sus tipos correctos
            for (Object[] laboreoArray : laboreos) {
                // Extraer y castear cada elemento del array a su tipo correcto
                // El array tiene la estructura: [fechaInicio, fechaFin, empleado, orden]
                LocalDateTime fechaInicio = (LocalDateTime) laboreoArray[0];  // Posición 0: fecha de inicio
                LocalDateTime fechaFin = (LocalDateTime) laboreoArray[1];     // Posición 1: fecha de fin
                Empleado empleado = (Empleado) laboreoArray[2];               // Posición 2: empleado asignado
                OrdenDeLaboreo orden = (OrdenDeLaboreo) laboreoArray[3];      // Posición 3: orden de laboreo
                
                // Pasar los datos ya casteados al lote para que delegue la creación del laboreo
                lote.crearLaboreos(fechaInicio, fechaFin, empleado, orden);
            }
        });
    }
}

