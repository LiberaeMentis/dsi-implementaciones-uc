import { useState } from 'react';
import {
  Card,
  CardContent,
  Typography,
  FormControl,
  FormGroup,
  FormControlLabel,
  Checkbox,
  Button,
  Box,
  Alert,
  Chip,
  Tooltip,
} from '@mui/material';
import { ArrowForward, ArrowBack } from '@mui/icons-material';
import dayjs from 'dayjs';
import 'dayjs/locale/es';
import { useLaboreo } from '../../context/LaboreoContext';
import { seleccionarLotes } from '../../services/laboreoService';

const Paso2Lotes = () => {
  const {
    lotes,
    lotesSeleccionados,
    setLotesSeleccionados,
    setLotesInfo,
    nextStep,
    prevStep,
    setError,
    setLoading,
    loading,
  } = useLaboreo();

  const [selectedLotes, setSelectedLotes] = useState(lotesSeleccionados.length > 0 ? lotesSeleccionados : []);
  const [localError, setLocalError] = useState('');

  const handleToggleLote = (numeroLote) => {
    setSelectedLotes((prev) =>
      prev.includes(numeroLote)
        ? prev.filter((n) => n !== numeroLote)
        : [...prev, numeroLote]
    );
  };

  const handleNext = async () => {
    if (selectedLotes.length === 0) {
      setLocalError('Por favor, selecciona al menos un lote');
      return;
    }

    setLoading(true);
    setLocalError('');

    const result = await seleccionarLotes(selectedLotes);
    
    if (result.success) {
      setLotesSeleccionados(selectedLotes);
      setLotesInfo(result.data);
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
          Seleccionar Lotes
        </Typography>
        <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
          Selecciona los lotes donde deseas registrar laboreos (puedes seleccionar varios)
        </Typography>
        <Typography variant="caption" color="text.secondary" sx={{ mb: 4, display: 'block', fontStyle: 'italic' }}>
          La fecha mostrada corresponde al proyecto de cultivo vigente de cada lote
        </Typography>

        {localError && (
          <Alert severity="error" sx={{ mb: 3 }}>
            {localError}
          </Alert>
        )}

        <FormControl component="fieldset" fullWidth>
          <FormGroup>
            {lotes.map((lote) => (
              <FormControlLabel
                key={lote.numero}
                control={
                  <Checkbox
                    checked={selectedLotes.includes(lote.numero)}
                    onChange={() => handleToggleLote(lote.numero)}
                  />
                }
                label={
                  <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
                    <Typography variant="body1" fontWeight={500}>
                      Lote Nº {lote.numero}
                    </Typography>
                    <Tooltip title="Fecha de inicio del proyecto de cultivo vigente" arrow>
                      <Chip
                        label={dayjs(lote.fechaInicioProyecto).locale('es').format('DD [de] MMMM YYYY')}
                        size="small"
                        color="primary"
                        variant="outlined"
                      />
                    </Tooltip>
                  </Box>
                }
                sx={{
                  mb: 2,
                  p: 2,
                  border: '1px solid',
                  borderColor: selectedLotes.includes(lote.numero) ? 'primary.main' : 'divider',
                  borderRadius: 2,
                  bgcolor: selectedLotes.includes(lote.numero) ? 'primary.light' : 'transparent',
                  '&:hover': {
                    bgcolor: selectedLotes.includes(lote.numero) ? 'primary.light' : 'action.hover',
                  },
                }}
              />
            ))}
          </FormGroup>
        </FormControl>

        {selectedLotes.length > 0 && (
          <Alert severity="info" sx={{ mt: 3 }}>
            Has seleccionado {selectedLotes.length} lote{selectedLotes.length > 1 ? 's' : ''}
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
            disabled={selectedLotes.length === 0 || loading}
            size="large"
          >
            Siguiente
          </Button>
        </Box>
      </CardContent>
    </Card>
  );
};

export default Paso2Lotes;

