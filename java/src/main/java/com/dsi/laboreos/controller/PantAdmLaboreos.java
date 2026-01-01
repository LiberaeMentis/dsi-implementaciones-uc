package com.dsi.laboreos.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dsi.laboreos.dto.CampoResponse;
import com.dsi.laboreos.dto.EmpleadoResponse;
import com.dsi.laboreos.dto.FechaHoraPorLoteRequest;
import com.dsi.laboreos.dto.LoteInfoResponse;
import com.dsi.laboreos.dto.LoteResponse;
import com.dsi.laboreos.dto.SeleccionarCampoRequest;
import com.dsi.laboreos.dto.SeleccionarEmpleadoPorLaboreoRequest;
import com.dsi.laboreos.dto.SeleccionarLaboreosRequest;
import com.dsi.laboreos.dto.SeleccionarLotesRequest;
import com.dsi.laboreos.service.GestorLaboreos;

@RestController
@RequestMapping("/laboreos")
public class PantAdmLaboreos {

    private final GestorLaboreos gestorLaboreos;

    public PantAdmLaboreos(GestorLaboreos gestorLaboreos) {
        this.gestorLaboreos = gestorLaboreos;
    }

    @PostMapping("/iniciar")
    public ResponseEntity<List<CampoResponse>> opcionRegLaboreoLote() {
        List<CampoResponse> campos = habilitarVentana();
        return ResponseEntity.ok(campos);
    }

    private List<CampoResponse> habilitarVentana() {
        return gestorLaboreos.nuevoLaboreo();
    }

    @PostMapping("/seleccionar-campo")
    public ResponseEntity<List<LoteResponse>> tomarSeleccionCampo(@RequestBody SeleccionarCampoRequest request) {
        List<LoteResponse> lotes = gestorLaboreos.tomarSeleccionCampo(request.getNombreCampo());
        if (lotes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(lotes);
    }

    @PostMapping("/seleccionar-lotes")
    public ResponseEntity<List<LoteInfoResponse>> tomarSeleccionLotes(@RequestBody SeleccionarLotesRequest request) {
        List<LoteInfoResponse> lotesInfo = gestorLaboreos.tomarSeleccionLotes(request.getNumerosLote());
        if (lotesInfo.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(lotesInfo);
    }

    @PostMapping("/seleccionar-laboreo")
    public ResponseEntity<Map<String, String>> tomarSeleccLaboreo(@RequestBody SeleccionarLaboreosRequest request) {
        gestorLaboreos.tomarSeleccLaboreo(request.getLaboreosPorLote());
        return ResponseEntity.ok(Map.of("mensaje", "Órdenes de laboreo seleccionadas correctamente"));
    }

    // Nota: se unifica en un único método para tomar la fecha y hora de inicio y
    // fin del laboreo para cada combinación de lote + laboreo.
    @PostMapping("/fecha-hora")
    public ResponseEntity<?> tomarFechaHoraInicioFin(@RequestBody FechaHoraPorLoteRequest request) {
        List<EmpleadoResponse> empleados = gestorLaboreos.tomarDuracionLaboreo(
                request.getFechasPorLaboreo());

        if (empleados.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error",
                            "Las fechas no son válidas o faltan fechas para algún laboreo. La fecha de inicio debe ser anterior a la fecha de fin y ambas deben ser anteriores a la fecha actual."));
        }

        return ResponseEntity.ok(empleados);
    }

    @PostMapping("/seleccionar-empleado")
    public ResponseEntity<Map<String, String>> tomarSeleccionEmpleado(@RequestBody SeleccionarEmpleadoPorLaboreoRequest request) {
        gestorLaboreos.tomarEmpleado(request.getEmpleadosPorLaboreo());
        return ResponseEntity.ok(Map.of("mensaje", "Empleados seleccionados correctamente"));
    }

    @PostMapping("/confirmar-registro")
    public ResponseEntity<Map<String, Boolean>> tomarConfirmacion() {
        Boolean esValido = gestorLaboreos.tomarConfirmacion();
        return ResponseEntity.ok(Map.of("esValido", esValido));
    }

    @PostMapping("/finalizar")
    public ResponseEntity<Map<String, String>> tomarSeleccionFinalizar() {
        gestorLaboreos.tomarOpcionFinalizar();
        return ResponseEntity.ok(Map.of("mensaje", "Registro de laboreos finalizado"));
    }
}
