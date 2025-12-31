import { useState } from 'react';
import {
  Card,
  CardContent,
  Typography,
  Button,
  Box,
  Alert,
  Paper,
  List,
  ListItem,
  ListItemText,
  Divider,
  Chip,
  Accordion,
  AccordionSummary,
  AccordionDetails,
} from '@mui/material';
import { ArrowBack, CheckCircle, ExpandMore } from '@mui/icons-material';
import dayjs from 'dayjs';
import 'dayjs/locale/es';
import { useLaboreo } from '../../context/LaboreoContext';
import { confirmarRegistro } from '../../services/laboreoService';

const Paso6Confirmacion = () => {
  const {
    campoSeleccionado,
    lotesInfo,
    laboreosSeleccionados,
    fechasPorLaboreo,
    empleadosPorLaboreo,
    setResultadoValidacion,
    nextStep,
    prevStep,
    setError,
    setLoading,
    loading,
  } = useLaboreo();

  const [localError, setLocalError] = useState('');

  const handleConfirm = async () => {
    setLoading(true);
    setLocalError('');

    const result = await confirmarRegistro();
    
    if (result.success) {
      setResultadoValidacion(result.data.esValido);
      nextStep();
    } else {
      setError(result.message);
      setLocalError(result.message);
    }
    
    setLoading(false);
  };

  // Agrupar laboreos por lote con sus fechas y empleados
  const laboreosConInfoPorLote = laboreosSeleccionados.reduce((acc, laboreo) => {
    const clave = `${laboreo.numeroLote}|${laboreo.laboreo[0]}|${laboreo.laboreo[1]}`;
    const fechas = fechasPorLaboreo[clave];
    const empleado = empleadosPorLaboreo[clave];
    
    if (!acc[laboreo.numeroLote]) {
      acc[laboreo.numeroLote] = {
        numeroLote: laboreo.numeroLote,
        laboreos: [],
      };
    }
    
    acc[laboreo.numeroLote].laboreos.push({
      laboreo: laboreo.laboreo,
      fechas: fechas,
      empleado: empleado,
    });
    
    return acc;
  }, {});

  return (
    <Card>
      <CardContent sx={{ p: 4 }}>
        <Typography variant="h5" gutterBottom color="primary" fontWeight="bold">
          Confirmar Registro
        </Typography>
        <Typography variant="body2" color="text.secondary" sx={{ mb: 4 }}>
          Verifica que toda la información sea correcta antes de confirmar
        </Typography>

        {localError && (
          <Alert severity="error" sx={{ mb: 3 }}>
            {localError}
          </Alert>
        )}

        <Paper variant="outlined" sx={{ p: 3, mb: 3 }}>
          <Typography variant="h6" gutterBottom color="secondary" fontWeight="bold">
            Resumen del Registro
          </Typography>

          <List>
            <ListItem>
              <ListItemText
                primary="Campo Seleccionado"
                secondary={`${campoSeleccionado?.nombre} (${campoSeleccionado?.cantidadHectareas} hectáreas)`}
                primaryTypographyProps={{ fontWeight: 600 }}
              />
            </ListItem>
            <Divider />

            <ListItem sx={{ flexDirection: 'column', alignItems: 'flex-start' }}>
              <Typography variant="body1" fontWeight={600} sx={{ mb: 2, width: '100%' }}>
                Laboreos a Registrar
              </Typography>
              {Object.values(laboreosConInfoPorLote).map((loteData) => {
                const loteInfo = lotesInfo[loteData.numeroLote - 1];
                return (
                  <Accordion key={loteData.numeroLote} sx={{ width: '100%', mb: 2 }}>
                    <AccordionSummary expandIcon={<ExpandMore />}>
                      <Typography variant="subtitle1" fontWeight={600}>
                        Lote Nº {loteData.numeroLote} - {loteInfo?.cultivoNombre}
                      </Typography>
                    </AccordionSummary>
                    <AccordionDetails>
                      <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
                        {loteData.laboreos.map((item, idx) => {
                          const fechas = item.fechas;
                          const empleado = item.empleado;
                          
                          return (
                            <Box
                              key={idx}
                              sx={{
                                p: 2,
                                border: '1px solid',
                                borderColor: 'divider',
                                borderRadius: 2,
                                bgcolor: 'background.paper',
                              }}
                            >
                              <Box sx={{ display: 'flex', alignItems: 'center', gap: 1, mb: 2 }}>
                                <Chip
                                  label={`${item.laboreo[0]} - ${item.laboreo[1]}`}
                                  color="primary"
                                  size="small"
                                />
                              </Box>
                              
                              <Box sx={{ pl: 2 }}>
                                <Typography variant="body2" sx={{ mb: 1 }}>
                                  <strong>Empleado:</strong>{' '}
                                  {empleado
                                    ? `${empleado.apellido}, ${empleado.nombre}`
                                    : 'No asignado'}
                                </Typography>
                                
                                <Typography variant="body2" sx={{ mb: 0.5 }}>
                                  <strong>Fecha y Hora:</strong>
                                </Typography>
                                <Box sx={{ pl: 2 }}>
                                  <Typography variant="body2" color="text.secondary">
                                    <strong>Inicio:</strong>{' '}
                                    {fechas?.inicio
                                      ? dayjs(fechas.inicio).locale('es').format('DD/MM/YYYY [a las] HH:mm')
                                      : 'No definida'}
                                  </Typography>
                                  <Typography variant="body2" color="text.secondary">
                                    <strong>Fin:</strong>{' '}
                                    {fechas?.fin
                                      ? dayjs(fechas.fin).locale('es').format('DD/MM/YYYY [a las] HH:mm')
                                      : 'No definida'}
                                  </Typography>
                                </Box>
                              </Box>
                            </Box>
                          );
                        })}
                      </Box>
                    </AccordionDetails>
                  </Accordion>
                );
              })}
            </ListItem>
          </List>
        </Paper>

        <Alert severity="warning" sx={{ mb: 3 }}>
          <strong>Nota:</strong> Los laboreos de tipo "Siembra" y "Cosecha" no son válidos en este registro.
          Si alguno de los laboreos seleccionados es de estos tipos, se mostrará un mensaje en el siguiente paso.
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
            color="success"
            startIcon={<CheckCircle />}
            onClick={handleConfirm}
            disabled={loading}
            size="large"
          >
            Confirmar Registro
          </Button>
        </Box>
      </CardContent>
    </Card>
  );
};

export default Paso6Confirmacion;
