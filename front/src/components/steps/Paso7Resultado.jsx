import { useEffect, useState } from 'react';
import {
  Card,
  CardContent,
  Typography,
  Button,
  Box,
  Alert,
  CircularProgress,
} from '@mui/material';
import { CheckCircle, Error, Refresh } from '@mui/icons-material';
import { useLaboreo } from '../../context/LaboreoContext';
import { finalizarRegistro } from '../../services/laboreoService';

const Paso7Resultado = () => {
  const { resultadoValidacion, resetWizard, setError } = useLaboreo();
  const [finalizando, setFinalizando] = useState(false);
  const [finalizado, setFinalizado] = useState(false);

  useEffect(() => {
    const finalizar = async () => {
      setFinalizando(true);
      const result = await finalizarRegistro();
      
      if (!result.success) {
        setError(result.message);
      }
      
      setFinalizando(false);
      setFinalizado(true);
    };

    if (!finalizado) {
      finalizar();
    }
  }, []);

  const handleNuevoRegistro = () => {
    resetWizard();
  };

  if (finalizando) {
    return (
      <Card>
        <CardContent sx={{ p: 4, textAlign: 'center' }}>
          <CircularProgress size={60} sx={{ mb: 3 }} />
          <Typography variant="h6">Finalizando registro...</Typography>
        </CardContent>
      </Card>
    );
  }

  return (
    <Card>
      <CardContent sx={{ p: 4 }}>
        <Box sx={{ textAlign: 'center', mb: 4 }}>
          {resultadoValidacion ? (
            <>
              <CheckCircle
                sx={{
                  fontSize: 100,
                  color: 'success.main',
                  mb: 2,
                }}
              />
              <Typography variant="h4" gutterBottom color="success.main" fontWeight="bold">
                ¡Registro Completado!
              </Typography>
              <Typography variant="body1" color="text.secondary">
                Los laboreos han sido registrados correctamente
              </Typography>
            </>
          ) : (
            <>
              <Error
                sx={{
                  fontSize: 100,
                  color: 'error.main',
                  mb: 2,
                }}
              />
              <Typography variant="h4" gutterBottom color="error.main" fontWeight="bold">
                Registro No Válido
              </Typography>
              <Typography variant="body1" color="text.secondary">
                Los laboreos fueron registrados pero contienen tipos no válidos
              </Typography>
            </>
          )}
        </Box>

        {resultadoValidacion ? (
          <Alert severity="success" sx={{ mb: 3 }}>
            <Typography variant="body2">
              <strong>¡Éxito!</strong> Todos los laboreos seleccionados son válidos y han sido registrados
              en el sistema. El empleado asignado podrá comenzar con las tareas programadas.
            </Typography>
          </Alert>
        ) : (
          <Alert severity="warning" sx={{ mb: 3 }}>
            <Typography variant="body2">
              <strong>Atención:</strong> Los laboreos fueron creados pero se detectó que incluyen tipos
              "Siembra" o "Cosecha", los cuales no son válidos para este tipo de registro. Por favor,
              revisa la información y realiza un nuevo registro si es necesario.
            </Typography>
          </Alert>
        )}

        <Box
          sx={{
            p: 3,
            bgcolor: 'background.default',
            borderRadius: 2,
            mb: 3,
          }}
        >
          <Typography variant="h6" gutterBottom fontWeight="bold">
            Próximos pasos
          </Typography>
          <Typography variant="body2" color="text.secondary" paragraph>
            • Los laboreos han sido guardados en el sistema
          </Typography>
          <Typography variant="body2" color="text.secondary" paragraph>
            • El empleado asignado puede comenzar con las tareas
          </Typography>
          <Typography variant="body2" color="text.secondary">
            • Puedes registrar nuevos laboreos usando el botón de abajo
          </Typography>
        </Box>

        <Box sx={{ display: 'flex', justifyContent: 'center', mt: 4 }}>
          <Button
            variant="contained"
            size="large"
            startIcon={<Refresh />}
            onClick={handleNuevoRegistro}
            sx={{ px: 4 }}
          >
            Registrar Nuevo Laboreo
          </Button>
        </Box>
      </CardContent>
    </Card>
  );
};

export default Paso7Resultado;

