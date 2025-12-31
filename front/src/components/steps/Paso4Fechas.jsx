import { useState, useEffect } from 'react';
import dayjs from 'dayjs';
import {
  Card,
  CardContent,
  Typography,
  Button,
  Box,
  Alert,
  Accordion,
  AccordionSummary,
  AccordionDetails,
  Chip,
} from '@mui/material';
import { ArrowForward, ArrowBack, ExpandMore } from '@mui/icons-material';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { DateTimePicker } from '@mui/x-date-pickers/DateTimePicker';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import 'dayjs/locale/es';
import { useLaboreo } from '../../context/LaboreoContext';
import { enviarFechaHora } from '../../services/laboreoService';

const Paso4Fechas = () => {
  const {
    laboreosSeleccionados,
    fechasPorLaboreo: fechasPorLaboreoContext,
    setFechasPorLaboreo: setFechasPorLaboreoContext,
    setEmpleados,
    nextStep,
    prevStep,
    setError,
    setLoading,
    loading,
  } = useLaboreo();

  // Estado local para almacenar fechas por cada laboreo
  const [fechasPorLaboreo, setFechasPorLaboreo] = useState(fechasPorLaboreoContext || {});
  const [localError, setLocalError] = useState('');

  // Sincronizar con el contexto cuando cambia
  useEffect(() => {
    if (fechasPorLaboreoContext) {
      setFechasPorLaboreo(fechasPorLaboreoContext);
    }
  }, [fechasPorLaboreoContext]);

  // Inicializar fechas vacías para cada laboreo seleccionado
  useEffect(() => {
    setFechasPorLaboreo((prev) => {
      const fechasIniciales = { ...prev };
      let hayCambios = false;
      
      laboreosSeleccionados.forEach((laboreo) => {
        const clave = `${laboreo.numeroLote}|${laboreo.laboreo[0]}|${laboreo.laboreo[1]}`;
        if (!fechasIniciales[clave]) {
          fechasIniciales[clave] = {
            numeroLote: laboreo.numeroLote,
            laboreo: laboreo.laboreo,
            inicio: null,
            fin: null,
          };
          hayCambios = true;
        }
      });
      
      return hayCambios ? fechasIniciales : prev;
    });
  }, [laboreosSeleccionados]);

  const validarFechas = () => {
    if (laboreosSeleccionados.length === 0) {
      setLocalError('No hay laboreos seleccionados');
      return false;
    }

    const ahora = dayjs();
    
    for (const laboreo of laboreosSeleccionados) {
      const clave = `${laboreo.numeroLote}|${laboreo.laboreo[0]}|${laboreo.laboreo[1]}`;
      const fechas = fechasPorLaboreo[clave];

      if (!fechas || !fechas.inicio || !fechas.fin) {
        setLocalError(`Por favor, completa las fechas para todos los laboreos seleccionados`);
      return false;
    }

      if (fechas.inicio.isAfter(fechas.fin) || fechas.inicio.isSame(fechas.fin)) {
        setLocalError(`La fecha de inicio debe ser anterior a la fecha de fin para el laboreo: ${laboreo.laboreo[0]} - ${laboreo.laboreo[1]} (Lote ${laboreo.numeroLote})`);
      return false;
      }

      if (fechas.inicio.isAfter(ahora) || fechas.inicio.isSame(ahora) || fechas.fin.isAfter(ahora) || fechas.fin.isSame(ahora)) {
        setLocalError(`Las fechas deben ser anteriores a la fecha actual para el laboreo: ${laboreo.laboreo[0]} - ${laboreo.laboreo[1]} (Lote ${laboreo.numeroLote})`);
        return false;
      }
    }

    return true;
  };

  const handleFechaChange = (clave, tipo, value) => {
    setFechasPorLaboreo((prev) => ({
      ...prev,
      [clave]: {
        ...prev[clave],
        [tipo]: value,
      },
    }));
    setLocalError('');
  };

  const handleNext = async () => {
    setLocalError('');

    if (!validarFechas()) {
      return;
    }

    setLoading(true);

    // Convertir fechasPorLaboreo al formato esperado por el backend
    const fechasParaEnviar = laboreosSeleccionados.map((laboreo) => {
      const clave = `${laboreo.numeroLote}|${laboreo.laboreo[0]}|${laboreo.laboreo[1]}`;
      const fechas = fechasPorLaboreo[clave];
      return {
        numeroLote: laboreo.numeroLote,
        laboreo: laboreo.laboreo,
        fechaHoraInicio: fechas.inicio.toDate(),
        fechaHoraFin: fechas.fin.toDate(),
      };
    });

    const result = await enviarFechaHora(fechasParaEnviar);

    if (result.success) {
      // Guardar fechas en el contexto
      setFechasPorLaboreoContext(fechasPorLaboreo);
      setEmpleados(result.data);
      nextStep();
    } else {
      setError(result.message);
      setLocalError(result.message);
    }

    setLoading(false);
  };

  // Agrupar laboreos por lote para mostrar en acordeones
  const laboreosPorLote = {};
  laboreosSeleccionados.forEach((laboreo) => {
    if (!laboreosPorLote[laboreo.numeroLote]) {
      laboreosPorLote[laboreo.numeroLote] = [];
    }
    laboreosPorLote[laboreo.numeroLote].push(laboreo);
  });

  return (
    <Card>
      <CardContent sx={{ p: 4 }}>
        <Typography variant="h5" gutterBottom color="primary" fontWeight="bold">
          Fecha y Hora del Laboreo
        </Typography>
        <Typography variant="body2" color="text.secondary" sx={{ mb: 4 }}>
          Ingresa la fecha y hora de inicio y fin para cada laboreo seleccionado
        </Typography>

        {localError && (
          <Alert severity="error" sx={{ mb: 3 }}>
            {localError}
          </Alert>
        )}

        {laboreosSeleccionados.length === 0 ? (
          <Alert severity="warning" sx={{ mb: 3 }}>
            No hay laboreos seleccionados. Por favor, vuelve al paso anterior.
          </Alert>
        ) : (
        <LocalizationProvider dateAdapter={AdapterDayjs} adapterLocale="es">
            <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
              {Object.entries(laboreosPorLote).map(([numeroLote, laboreos], index) => (
                <Accordion key={numeroLote} defaultExpanded={index === 0}>
                  <AccordionSummary expandIcon={<ExpandMore />}>
                    <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
                      <Typography variant="h6">Lote Nº {numeroLote}</Typography>
                      <Chip label={`${laboreos.length} laboreo${laboreos.length > 1 ? 's' : ''}`} color="secondary" />
                    </Box>
                  </AccordionSummary>
                  <AccordionDetails>
          <Box sx={{ display: 'flex', flexDirection: 'column', gap: 3 }}>
                      {laboreos.map((laboreo, idx) => {
                        const clave = `${laboreo.numeroLote}|${laboreo.laboreo[0]}|${laboreo.laboreo[1]}`;
                        const fechas = fechasPorLaboreo[clave] || { inicio: null, fin: null };

                        return (
                          <Box key={idx} sx={{ p: 2, border: '1px solid', borderColor: 'divider', borderRadius: 2 }}>
                            <Typography variant="subtitle1" fontWeight="bold" sx={{ mb: 2 }}>
                              {laboreo.laboreo[0]} - {laboreo.laboreo[1]}
                            </Typography>
                            <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
            <DateTimePicker
              label="Fecha y Hora de Inicio"
                                value={fechas.inicio}
                                onChange={(newValue) => handleFechaChange(clave, 'inicio', newValue)}
              maxDateTime={dayjs()}
              slotProps={{
                textField: {
                  fullWidth: true,
                  variant: 'outlined',
                },
              }}
            />

            <DateTimePicker
              label="Fecha y Hora de Fin"
                                value={fechas.fin}
                                onChange={(newValue) => handleFechaChange(clave, 'fin', newValue)}
              maxDateTime={dayjs()}
                                minDateTime={fechas.inicio || undefined}
              slotProps={{
                textField: {
                  fullWidth: true,
                  variant: 'outlined',
                },
              }}
            />
                            </Box>
                          </Box>
                        );
                      })}
                    </Box>
                  </AccordionDetails>
                </Accordion>
              ))}
          </Box>
        </LocalizationProvider>
        )}

        <Alert severity="info" sx={{ mt: 3 }}>
          Las fechas deben ser anteriores a la fecha actual y la fecha de inicio debe ser anterior a la fecha de fin
        </Alert>

        <Box sx={{ display: 'flex', justifyContent: 'space-between', mt: 4 }}>
          <Button
            variant="outlined"
            startIcon={<ArrowBack />}
            onClick={prevStep}
            size="large"
          >
            Anterior
          </Button>
          <Button
            variant="contained"
            endIcon={<ArrowForward />}
            onClick={handleNext}
            disabled={laboreosSeleccionados.length === 0 || loading}
            size="large"
          >
            Siguiente
          </Button>
        </Box>
      </CardContent>
    </Card>
  );
};

export default Paso4Fechas;
