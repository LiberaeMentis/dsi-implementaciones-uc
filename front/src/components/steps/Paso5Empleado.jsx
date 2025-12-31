import { useState, useEffect } from 'react';
import {
  Card,
  CardContent,
  Typography,
  FormControl,
  RadioGroup,
  FormControlLabel,
  Radio,
  Button,
  Box,
  Alert,
  Avatar,
  Accordion,
  AccordionSummary,
  AccordionDetails,
  Chip,
} from '@mui/material';
import { ArrowForward, ArrowBack, Person, ExpandMore } from '@mui/icons-material';
import { useLaboreo } from '../../context/LaboreoContext';
import { seleccionarEmpleado } from '../../services/laboreoService';

const Paso5Empleado = () => {
  const {
    laboreosSeleccionados,
    empleadosPorLaboreo: empleadosPorLaboreoContext,
    setEmpleadosPorLaboreo: setEmpleadosPorLaboreoContext,
    empleados,
    nextStep,
    prevStep,
    setError,
    setLoading,
    loading,
  } = useLaboreo();

  // Estado local para almacenar empleado por cada laboreo
  const [empleadosPorLaboreo, setEmpleadosPorLaboreo] = useState(() => {
    // Convertir del formato del contexto al formato local
    const empleadosLocal = {};
    if (empleadosPorLaboreoContext) {
      Object.keys(empleadosPorLaboreoContext).forEach((clave) => {
        const empleado = empleadosPorLaboreoContext[clave];
        empleadosLocal[clave] = {
          numeroLote: parseInt(clave.split('|')[0]),
          laboreo: [clave.split('|')[1], clave.split('|')[2]],
          empleado: `${empleado.nombre}_${empleado.apellido}`,
        };
      });
    }
    return empleadosLocal;
  });
  const [localError, setLocalError] = useState('');

  // Sincronizar con el contexto cuando cambia
  useEffect(() => {
    if (empleadosPorLaboreoContext) {
      const empleadosLocal = {};
      Object.keys(empleadosPorLaboreoContext).forEach((clave) => {
        const empleado = empleadosPorLaboreoContext[clave];
        empleadosLocal[clave] = {
          numeroLote: parseInt(clave.split('|')[0]),
          laboreo: [clave.split('|')[1], clave.split('|')[2]],
          empleado: `${empleado.nombre}_${empleado.apellido}`,
        };
      });
      setEmpleadosPorLaboreo(empleadosLocal);
    }
  }, [empleadosPorLaboreoContext]);

  // Inicializar empleados vacíos para cada laboreo seleccionado
  useEffect(() => {
    setEmpleadosPorLaboreo((prev) => {
      const empleadosIniciales = { ...prev };
      let hayCambios = false;
      
      laboreosSeleccionados.forEach((laboreo) => {
        const clave = `${laboreo.numeroLote}|${laboreo.laboreo[0]}|${laboreo.laboreo[1]}`;
        if (!empleadosIniciales[clave]) {
          empleadosIniciales[clave] = {
            numeroLote: laboreo.numeroLote,
            laboreo: laboreo.laboreo,
            empleado: null,
          };
          hayCambios = true;
        }
      });
      
      return hayCambios ? empleadosIniciales : prev;
    });
  }, [laboreosSeleccionados]);

  const handleEmpleadoChange = (clave, empleadoKey) => {
    setEmpleadosPorLaboreo((prev) => ({
      ...prev,
      [clave]: {
        ...prev[clave],
        empleado: empleadoKey,
      },
    }));
    setLocalError('');
  };

  const validarEmpleados = () => {
    if (laboreosSeleccionados.length === 0) {
      setLocalError('No hay laboreos seleccionados');
      return false;
    }

    for (const laboreo of laboreosSeleccionados) {
      const clave = `${laboreo.numeroLote}|${laboreo.laboreo[0]}|${laboreo.laboreo[1]}`;
      const empleadoData = empleadosPorLaboreo[clave];

      if (!empleadoData || !empleadoData.empleado) {
        setLocalError(`Por favor, selecciona un empleado para todos los laboreos seleccionados`);
        return false;
      }
    }

    return true;
  };

  const handleNext = async () => {
    setLocalError('');

    if (!validarEmpleados()) {
      return;
    }

    setLoading(true);

    // Convertir empleadosPorLaboreo al formato esperado por el backend
    const empleadosParaEnviar = laboreosSeleccionados.map((laboreo) => {
      const clave = `${laboreo.numeroLote}|${laboreo.laboreo[0]}|${laboreo.laboreo[1]}`;
      const empleadoData = empleadosPorLaboreo[clave];
      const [nombre, apellido] = empleadoData.empleado.split('_');
      return {
        numeroLote: laboreo.numeroLote,
        laboreo: laboreo.laboreo,
        nombreEmpleado: nombre,
        apellidoEmpleado: apellido,
      };
    });

    const result = await seleccionarEmpleado(empleadosParaEnviar);

    if (result.success) {
      // Guardar empleados en el contexto (convertir a formato de contexto)
      const empleadosParaContexto = {};
      empleadosParaEnviar.forEach((item) => {
        const clave = `${item.numeroLote}|${item.laboreo[0]}|${item.laboreo[1]}`;
        empleadosParaContexto[clave] = {
          nombre: item.nombreEmpleado,
          apellido: item.apellidoEmpleado,
        };
      });
      setEmpleadosPorLaboreoContext(empleadosParaContexto);
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
          Seleccionar Empleado
        </Typography>
        <Typography variant="body2" color="text.secondary" sx={{ mb: 4 }}>
          Selecciona el empleado que realizará cada laboreo
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
                      const empleadoData = empleadosPorLaboreo[clave] || { empleado: null };

                      return (
                        <Box key={idx} sx={{ p: 2, border: '1px solid', borderColor: 'divider', borderRadius: 2 }}>
                          <Typography variant="subtitle1" fontWeight="bold" sx={{ mb: 2 }}>
                            {laboreo.laboreo[0]} - {laboreo.laboreo[1]}
                          </Typography>
                          <FormControl component="fieldset" fullWidth>
                            <RadioGroup
                              value={empleadoData.empleado || ''}
                              onChange={(e) => handleEmpleadoChange(clave, e.target.value)}
                            >
                              {empleados.map((empleado) => {
                                const empleadoKey = `${empleado.nombre}_${empleado.apellido}`;
                                return (
                                  <FormControlLabel
                                    key={empleadoKey}
                                    value={empleadoKey}
                                    control={<Radio />}
                                    label={
                                      <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
                                        <Avatar sx={{ bgcolor: 'secondary.main' }}>
                                          <Person />
                                        </Avatar>
                                        <Typography variant="body1" fontWeight={500}>
                                          {empleado.apellido}, {empleado.nombre}
                                        </Typography>
                                      </Box>
                                    }
                                    sx={{
                                      mb: 1,
                                      p: 1,
                                      borderRadius: 1,
                                      border: '1px solid',
                                      borderColor: empleadoData.empleado === empleadoKey ? 'primary.main' : 'divider',
                                      bgcolor: empleadoData.empleado === empleadoKey ? 'primary.light' : 'transparent',
                                      '&:hover': {
                                        bgcolor: empleadoData.empleado === empleadoKey ? 'primary.light' : 'action.hover',
                                      },
                                    }}
                                  />
                                );
                              })}
                            </RadioGroup>
                          </FormControl>
                        </Box>
                      );
                    })}
                  </Box>
                </AccordionDetails>
              </Accordion>
            ))}
          </Box>
        )}

        <Alert severity="info" sx={{ mt: 3 }}>
          Selecciona un empleado para cada laboreo seleccionado
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

export default Paso5Empleado;
