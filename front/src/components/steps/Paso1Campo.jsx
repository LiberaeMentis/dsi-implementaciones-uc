import { useEffect, useState } from 'react';
import {
  Card,
  CardContent,
  Typography,
  FormControl,
  FormLabel,
  RadioGroup,
  FormControlLabel,
  Radio,
  Button,
  Box,
  CircularProgress,
  Alert,
} from '@mui/material';
import { ArrowForward } from '@mui/icons-material';
import { useLaboreo } from '../../context/LaboreoContext';
import { iniciarRegistro, seleccionarCampo } from '../../services/laboreoService';

const Paso1Campo = () => {
  const {
    campos,
    setCampos,
    campoSeleccionado,
    setCampoSeleccionado,
    setLotes,
    nextStep,
    setError,
    setLoading,
    loading,
  } = useLaboreo();

  const [selectedCampo, setSelectedCampo] = useState(campoSeleccionado?.nombre || '');
  const [localError, setLocalError] = useState('');

  useEffect(() => {
    const cargarCampos = async () => {
      setLoading(true);
      const result = await iniciarRegistro();
      if (result.success) {
        setCampos(result.data);
      } else {
        setError(result.message);
        setLocalError(result.message);
      }
      setLoading(false);
    };

    if (campos.length === 0) {
      cargarCampos();
    }
  }, []);

  const handleNext = async () => {
    if (!selectedCampo) {
      setLocalError('Por favor, selecciona un campo');
      return;
    }

    setLoading(true);
    setLocalError('');

    const result = await seleccionarCampo(selectedCampo);
    
    if (result.success) {
      const campo = campos.find((c) => c.nombre === selectedCampo);
      setCampoSeleccionado(campo);
      setLotes(result.data);
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
          Seleccionar Campo
        </Typography>
        <Typography variant="body2" color="text.secondary" sx={{ mb: 4 }}>
          Selecciona el campo donde deseas registrar los laboreos
        </Typography>

        {loading ? (
          <Box sx={{ display: 'flex', justifyContent: 'center', py: 4 }}>
            <CircularProgress />
          </Box>
        ) : localError ? (
          <Alert severity="error" sx={{ mb: 3 }}>
            {localError}
          </Alert>
        ) : (
          <>
            <FormControl component="fieldset" fullWidth>
              <FormLabel component="legend" sx={{ mb: 2, fontWeight: 500 }}>
                Campos Disponibles
              </FormLabel>
              <RadioGroup
                value={selectedCampo}
                onChange={(e) => setSelectedCampo(e.target.value)}
              >
                {campos.map((campo) => (
                  <FormControlLabel
                    key={campo.nombre}
                    value={campo.nombre}
                    control={<Radio />}
                    label={
                      <Box>
                        <Typography variant="body1" fontWeight={500}>
                          {campo.nombre}
                        </Typography>
                        <Typography variant="body2" color="text.secondary">
                          {campo.cantidadHectareas} hectáreas
                        </Typography>
                      </Box>
                    }
                    sx={{
                      mb: 2,
                      p: 2,
                      border: '1px solid',
                      borderColor: 'divider',
                      borderRadius: 2,
                      '&:hover': {
                        bgcolor: 'action.hover',
                      },
                    }}
                  />
                ))}
              </RadioGroup>
            </FormControl>

            {!selectedCampo && (
              <Alert severity="info" sx={{ mt: 3 }}>
                Selecciona un campo para continuar
              </Alert>
            )}
          </>
        )}

        <Box sx={{ display: 'flex', justifyContent: 'flex-end', mt: 4 }}>
          <Button
            variant="contained"
            endIcon={<ArrowForward />}
            onClick={handleNext}
            disabled={!selectedCampo || loading}
            size="large"
          >
            Siguiente
          </Button>
        </Box>
      </CardContent>
    </Card>
  );
};

export default Paso1Campo;

