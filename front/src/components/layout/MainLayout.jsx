import { Box, Container, Grid, AppBar, Toolbar, Typography, Snackbar, Alert } from '@mui/material';
import { Agriculture } from '@mui/icons-material';
import Sidebar from './Sidebar';
import { useLaboreo } from '../../context/LaboreoContext';

import Paso1Campo from '../steps/Paso1Campo';
import Paso2Lotes from '../steps/Paso2Lotes';
import Paso3Laboreos from '../steps/Paso3Laboreos';
import Paso4Fechas from '../steps/Paso4Fechas';
import Paso5Empleado from '../steps/Paso5Empleado';
import Paso6Confirmacion from '../steps/Paso6Confirmacion';
import Paso7Resultado from '../steps/Paso7Resultado';

const MainLayout = () => {
  const { currentStep, error, setError } = useLaboreo();

  const renderStep = () => {
    switch (currentStep) {
      case 1:
        return <Paso1Campo />;
      case 2:
        return <Paso2Lotes />;
      case 3:
        return <Paso3Laboreos />;
      case 4:
        return <Paso4Fechas />;
      case 5:
        return <Paso5Empleado />;
      case 6:
        return <Paso6Confirmacion />;
      case 7:
        return <Paso7Resultado />;
      default:
        return <Paso1Campo />;
    }
  };

  return (
    <Box sx={{ display: 'flex', flexDirection: 'column', minHeight: '100vh' }}>
      <AppBar position="static" elevation={1}>
        <Toolbar>
          <Agriculture sx={{ mr: 2 }} />
          <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
            Registro de Laboreos en Lotes
          </Typography>
        </Toolbar>
      </AppBar>

      <Box sx={{ flexGrow: 1, display: 'flex' }}>
        <Grid container sx={{ height: '100%' }}>
          <Grid item xs={12} md={3}>
            <Sidebar />
          </Grid>
          <Grid item xs={12} md={9}>
            <Container maxWidth="lg" sx={{ py: 4 }}>
              {renderStep()}
            </Container>
          </Grid>
        </Grid>
      </Box>

      <Snackbar
        open={!!error}
        autoHideDuration={6000}
        onClose={() => setError(null)}
        anchorOrigin={{ vertical: 'bottom', horizontal: 'center' }}
      >
        <Alert onClose={() => setError(null)} severity="error" sx={{ width: '100%' }}>
          {error}
        </Alert>
      </Snackbar>
    </Box>
  );
};

export default MainLayout;

