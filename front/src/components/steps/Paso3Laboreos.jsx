import { useState } from 'react';
import {
  Card,
  CardContent,
  Typography,
  Accordion,
  AccordionSummary,
  AccordionDetails,
  FormGroup,
  FormControlLabel,
  Checkbox,
  Button,
  Box,
  Alert,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  Chip,
} from '@mui/material';
import { ArrowForward, ArrowBack, ExpandMore } from '@mui/icons-material';
import dayjs from 'dayjs';
import 'dayjs/locale/es';
import { useLaboreo } from '../../context/LaboreoContext';
import { seleccionarLaboreos } from '../../services/laboreoService';

const Paso3Laboreos = () => {
  const {
    lotesInfo,
    laboreosSeleccionados,
    setLaboreosSeleccionados,
    nextStep,
    prevStep,
    setError,
    setLoading,
    loading,
  } = useLaboreo();

  const [selectedLaboreos, setSelectedLaboreos] = useState(
    laboreosSeleccionados.length > 0 ? laboreosSeleccionados : []
  );
  const [localError, setLocalError] = useState('');

  const handleToggleLaboreo = (numeroLote, laboreo) => {
    setSelectedLaboreos((prev) => {
      const exists = prev.find(
        (item) =>
          item.numeroLote === numeroLote &&
          item.laboreo[0] === laboreo[0] &&
          item.laboreo[1] === laboreo[1]
      );

      if (exists) {
        return prev.filter(
          (item) =>
            !(
              item.numeroLote === numeroLote &&
              item.laboreo[0] === laboreo[0] &&
              item.laboreo[1] === laboreo[1]
            )
        );
      } else {
        return [...prev, { numeroLote, laboreo }];
      }
    });
  };

  const isLaboreoSelected = (numeroLote, laboreo) => {
    return selectedLaboreos.some(
      (item) =>
        item.numeroLote === numeroLote &&
        item.laboreo[0] === laboreo[0] &&
        item.laboreo[1] === laboreo[1]
    );
  };

  const handleNext = async () => {
    if (selectedLaboreos.length === 0) {
      setLocalError('Por favor, selecciona al menos un laboreo');
      return;
    }

    setLoading(true);
    setLocalError('');

    const result = await seleccionarLaboreos(selectedLaboreos);
    
    if (result.success) {
      setLaboreosSeleccionados(selectedLaboreos);
      nextStep();
    } else {
      setError(result.message);
      setLocalError(result.message);
    }
    
    setLoading(false);
  };

  return (
    <Card>
      <CardContent sx={{ p: 4 }}>
        <Typography variant="h5" gutterBottom color="primary" fontWeight="bold">
          Seleccionar Laboreos por Lote
        </Typography>
        <Typography variant="body2" color="text.secondary" sx={{ mb: 4 }}>
          Para cada lote, selecciona los laboreos que deseas registrar
        </Typography>

        {localError && (
          <Alert severity="error" sx={{ mb: 3 }}>
            {localError}
          </Alert>
        )}

        {lotesInfo && lotesInfo.length > 0 ? lotesInfo.map((loteInfo, index) => {
          const numeroLote = index + 1;
          
          return (
            <Accordion key={numeroLote} defaultExpanded={index === 0} sx={{ mb: 2 }}>
              <AccordionSummary expandIcon={<ExpandMore />}>
                <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
                  <Typography variant="h6">Lote Nº {numeroLote}</Typography>
                  <Chip label={`Cultivo: ${loteInfo.cultivoNombre}`} color="secondary" />
                </Box>
              </AccordionSummary>
              <AccordionDetails>
                {loteInfo.laboreosRealizados.length > 0 && (
                  <Box sx={{ mb: 3 }}>
                    <Typography variant="subtitle2" gutterBottom fontWeight="bold">
                      Laboreos Realizados
                    </Typography>
                    <TableContainer component={Paper} variant="outlined">
                      <Table size="small">
                        <TableHead>
                          <TableRow>
                            <TableCell>Tipo de Laboreo</TableCell>
                            <TableCell>Fecha</TableCell>
                          </TableRow>
                        </TableHead>
                        <TableBody>
                          {loteInfo.laboreosRealizados.map((laboreo, idx) => (
                            <TableRow key={idx}>
                              <TableCell>{laboreo.tipoLaboreo}</TableCell>
                              <TableCell>
                                {dayjs(laboreo.fecha).locale('es').format('DD/MM/YYYY HH:mm')}
                              </TableCell>
                            </TableRow>
                          ))}
                        </TableBody>
                      </Table>
                    </TableContainer>
                  </Box>
                )}

                <Typography variant="subtitle2" gutterBottom fontWeight="bold" sx={{ mt: 2 }}>
                  Laboreos Disponibles
                </Typography>
                <FormGroup>
                  {loteInfo.tiposLaboreoDisponibles.map((tipo, idx) => {
                    const laboreo = [tipo.nombreTipoLaboreo, tipo.nombreMomentoLaboreo];
                    return (
                      <FormControlLabel
                        key={idx}
                        control={
                          <Checkbox
                            checked={isLaboreoSelected(numeroLote, laboreo)}
                            onChange={() => handleToggleLaboreo(numeroLote, laboreo)}
                          />
                        }
                        label={`${tipo.nombreTipoLaboreo} - ${tipo.nombreMomentoLaboreo}`}
                        sx={{
                          p: 1,
                          borderRadius: 1,
                          '&:hover': {
                            bgcolor: 'action.hover',
                          },
                        }}
                      />
                    );
                  })}
                </FormGroup>
              </AccordionDetails>
            </Accordion>
          );
        }) : (
          <Alert severity="warning" sx={{ mb: 3 }}>
            No hay información de lotes disponible. Por favor, vuelve al paso anterior.
          </Alert>
        )}

        {selectedLaboreos.length > 0 && (
          <Alert severity="info" sx={{ mt: 3 }}>
            Has seleccionado {selectedLaboreos.length} laboreo{selectedLaboreos.length > 1 ? 's' : ''}
          </Alert>
        )}

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
            disabled={selectedLaboreos.length === 0 || loading}
            size="large"
          >
            Siguiente
          </Button>
        </Box>
      </CardContent>
    </Card>
  );
};

export default Paso3Laboreos;

