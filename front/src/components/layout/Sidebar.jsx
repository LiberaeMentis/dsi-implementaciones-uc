import {
  List,
  ListItem,
  ListItemButton,
  ListItemIcon,
  ListItemText,
  Paper,
  Typography,
  Box,
} from '@mui/material';
import {
  CheckCircle,
  RadioButtonUnchecked,
  Landscape,
  GridOn,
  Agriculture,
  Schedule,
  Person,
  CheckCircleOutline,
  Flag,
} from '@mui/icons-material';
import { useLaboreo } from '../../context/LaboreoContext';

const steps = [
  { id: 1, label: 'Seleccionar Campo', icon: Landscape },
  { id: 2, label: 'Seleccionar Lotes', icon: GridOn },
  { id: 3, label: 'Seleccionar Laboreos', icon: Agriculture },
  { id: 4, label: 'Fecha y Hora', icon: Schedule },
  { id: 5, label: 'Seleccionar Empleado', icon: Person },
  { id: 6, label: 'Confirmar Registro', icon: CheckCircleOutline },
  { id: 7, label: 'Resultado', icon: Flag },
];

const Sidebar = () => {
  const { currentStep, completedSteps, goToStep } = useLaboreo();

  const isStepCompleted = (stepId) => completedSteps.includes(stepId);
  const isStepCurrent = (stepId) => currentStep === stepId;
  const isStepAccessible = (stepId) => stepId === 1 || completedSteps.includes(stepId - 1);

  return (
    <Paper
      elevation={0}
      sx={{
        height: '100%',
        borderRadius: 0,
        borderRight: '1px solid',
        borderColor: 'divider',
        bgcolor: 'background.paper',
      }}
    >
      <Box sx={{ p: 3, borderBottom: '1px solid', borderColor: 'divider' }}>
        <Typography variant="h6" color="primary" fontWeight="bold">
          Registro de Laboreos
        </Typography>
        <Typography variant="body2" color="text.secondary" sx={{ mt: 0.5 }}>
          Paso {currentStep} de 7
        </Typography>
      </Box>

      <List sx={{ p: 2 }}>
        {steps.map((step) => {
          const Icon = step.icon;
          const completed = isStepCompleted(step.id);
          const current = isStepCurrent(step.id);
          const accessible = isStepAccessible(step.id);

          return (
            <ListItem key={step.id} disablePadding sx={{ mb: 1 }}>
              <ListItemButton
                onClick={() => accessible && goToStep(step.id)}
                disabled={!accessible}
                selected={current}
                sx={{
                  borderRadius: 2,
                  '&.Mui-selected': {
                    bgcolor: 'primary.light',
                    color: 'primary.contrastText',
                    '&:hover': {
                      bgcolor: 'primary.main',
                    },
                  },
                  '&.Mui-disabled': {
                    opacity: 0.5,
                  },
                }}
              >
                <ListItemIcon sx={{ minWidth: 40, color: 'inherit' }}>
                  {completed ? (
                    <CheckCircle color={current ? 'inherit' : 'success'} />
                  ) : current ? (
                    <Icon color="inherit" />
                  ) : (
                    <RadioButtonUnchecked color={accessible ? 'inherit' : 'disabled'} />
                  )}
                </ListItemIcon>
                <ListItemText
                  primary={step.label}
                  primaryTypographyProps={{
                    fontWeight: current ? 600 : 400,
                    fontSize: '0.95rem',
                  }}
                />
              </ListItemButton>
            </ListItem>
          );
        })}
      </List>
    </Paper>
  );
};

export default Sidebar;

