import { createContext, useContext, useState } from 'react';

const LaboreoContext = createContext();

export const useLaboreo = () => {
  const context = useContext(LaboreoContext);
  if (!context) {
    throw new Error('useLaboreo debe ser usado dentro de LaboreoProvider');
  }
  return context;
};

export const LaboreoProvider = ({ children }) => {
  const [currentStep, setCurrentStep] = useState(1);
  const [completedSteps, setCompletedSteps] = useState([]);
  
  const [campos, setCampos] = useState([]);
  const [campoSeleccionado, setCampoSeleccionado] = useState(null);
  
  const [lotes, setLotes] = useState([]);
  const [lotesSeleccionados, setLotesSeleccionados] = useState([]);
  const [lotesInfo, setLotesInfo] = useState([]);
  
  const [laboreosSeleccionados, setLaboreosSeleccionados] = useState([]);
  
  // Fechas por laboreo: clave = "numeroLote|tipoLaboreo|momentoLaboreo", valor = { inicio, fin }
  const [fechasPorLaboreo, setFechasPorLaboreo] = useState({});
  
  // Empleados por laboreo: clave = "numeroLote|tipoLaboreo|momentoLaboreo", valor = { nombre, apellido }
  const [empleadosPorLaboreo, setEmpleadosPorLaboreo] = useState({});
  
  const [empleados, setEmpleados] = useState([]);
  
  const [resultadoValidacion, setResultadoValidacion] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const nextStep = () => {
    if (currentStep < 7) {
      if (!completedSteps.includes(currentStep)) {
        setCompletedSteps([...completedSteps, currentStep]);
      }
      setCurrentStep(currentStep + 1);
    }
  };

  const prevStep = () => {
    if (currentStep > 1) {
      setCurrentStep(currentStep - 1);
    }
  };

  const goToStep = (step) => {
    if (step <= 1 || completedSteps.includes(step - 1)) {
      setCurrentStep(step);
    }
  };

  const resetWizard = () => {
    setCurrentStep(1);
    setCompletedSteps([]);
    setCampos([]);
    setCampoSeleccionado(null);
    setLotes([]);
    setLotesSeleccionados([]);
    setLotesInfo([]);
    setLaboreosSeleccionados([]);
    setFechasPorLaboreo({});
    setEmpleadosPorLaboreo({});
    setEmpleados([]);
    setResultadoValidacion(null);
    setError(null);
  };

  const value = {
    currentStep,
    completedSteps,
    campos,
    setCampos,
    campoSeleccionado,
    setCampoSeleccionado,
    lotes,
    setLotes,
    lotesSeleccionados,
    setLotesSeleccionados,
    lotesInfo,
    setLotesInfo,
    laboreosSeleccionados,
    setLaboreosSeleccionados,
    fechasPorLaboreo,
    setFechasPorLaboreo,
    empleadosPorLaboreo,
    setEmpleadosPorLaboreo,
    empleados,
    setEmpleados,
    resultadoValidacion,
    setResultadoValidacion,
    loading,
    setLoading,
    error,
    setError,
    nextStep,
    prevStep,
    goToStep,
    resetWizard
  };

  return (
    <LaboreoContext.Provider value={value}>
      {children}
    </LaboreoContext.Provider>
  );
};

