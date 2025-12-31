import axios from 'axios';

const API_URL = '/laboreos';

// MODO DEMO: Cambia a true para usar datos mockeados sin backend
const DEMO_MODE = false;

// Datos mockeados para el modo demo
const mockData = {
  campos: [
    { nombre: 'Campo Norte', cantidadHectareas: 25.5 },
    { nombre: 'Campo Sur', cantidadHectareas: 20.0 },
  ],
  lotes: [
    { numero: 1, fechaInicioProyecto: '2024-10-30' }, // Soja (2 meses atrás)
    { numero: 2, fechaInicioProyecto: '2024-11-30' }, // Maíz (1 mes atrás)
    { numero: 3, fechaInicioProyecto: '2024-12-15' }, // Maní (15 días atrás)
    { numero: 4, fechaInicioProyecto: '2024-09-30' }, // Girasol (3 meses atrás)
  ],
  lotesInfo: [
    {
      // Lote 1: Soja - Según dominio: Arar, Rastrillar, Sembrar, Escardillar, Cosechar
      cultivoNombre: 'Soja',
      laboreosRealizados: [
        { tipoLaboreo: 'Arado', fecha: '2024-10-31T08:00:00' },
        { tipoLaboreo: 'Rastrillado', fecha: '2024-11-02T09:00:00' },
      ],
      tiposLaboreoDisponibles: [
        { nombreTipoLaboreo: 'Arado', nombreMomentoLaboreo: 'Pre-siembra' },
        { nombreTipoLaboreo: 'Rastrillado', nombreMomentoLaboreo: 'Pre-siembra' },
        { nombreTipoLaboreo: 'Siembra', nombreMomentoLaboreo: 'Siembra' },
        { nombreTipoLaboreo: 'Escardillado', nombreMomentoLaboreo: 'Post-siembra' },
        { nombreTipoLaboreo: 'Cosecha', nombreMomentoLaboreo: 'Cosecha' },
      ],
    },
    {
      // Lote 2: Maíz
      cultivoNombre: 'Maíz',
      laboreosRealizados: [
        { tipoLaboreo: 'Arado', fecha: '2024-12-01T07:30:00' },
      ],
      tiposLaboreoDisponibles: [
        { nombreTipoLaboreo: 'Arado', nombreMomentoLaboreo: 'Pre-siembra' },
        { nombreTipoLaboreo: 'Rastrillado', nombreMomentoLaboreo: 'Pre-siembra' },
        { nombreTipoLaboreo: 'Siembra', nombreMomentoLaboreo: 'Siembra' },
        { nombreTipoLaboreo: 'Fumigación', nombreMomentoLaboreo: 'Post-siembra' },
        { nombreTipoLaboreo: 'Cosecha', nombreMomentoLaboreo: 'Cosecha' },
      ],
    },
    {
      // Lote 3: Maní
      cultivoNombre: 'Maní',
      laboreosRealizados: [
        { tipoLaboreo: 'Arado', fecha: '2024-12-16T10:00:00' },
        { tipoLaboreo: 'Rastrillado', fecha: '2024-12-17T08:30:00' },
      ],
      tiposLaboreoDisponibles: [
        { nombreTipoLaboreo: 'Arado', nombreMomentoLaboreo: 'Pre-siembra' },
        { nombreTipoLaboreo: 'Rastrillado', nombreMomentoLaboreo: 'Pre-siembra' },
        { nombreTipoLaboreo: 'Siembra', nombreMomentoLaboreo: 'Siembra' },
        { nombreTipoLaboreo: 'Fumigación', nombreMomentoLaboreo: 'Post-siembra' },
        { nombreTipoLaboreo: 'Cosecha', nombreMomentoLaboreo: 'Cosecha' },
      ],
    },
    {
      // Lote 4: Girasol
      cultivoNombre: 'Girasol',
      laboreosRealizados: [
        { tipoLaboreo: 'Arado', fecha: '2024-10-01T09:00:00' },
        { tipoLaboreo: 'Rastrillado', fecha: '2024-10-03T08:00:00' },
        { tipoLaboreo: 'Siembra', fecha: '2024-10-05T07:00:00' },
      ],
      tiposLaboreoDisponibles: [
        { nombreTipoLaboreo: 'Arado', nombreMomentoLaboreo: 'Pre-siembra' },
        { nombreTipoLaboreo: 'Rastrillado', nombreMomentoLaboreo: 'Pre-siembra' },
        { nombreTipoLaboreo: 'Siembra', nombreMomentoLaboreo: 'Siembra' },
        { nombreTipoLaboreo: 'Riego', nombreMomentoLaboreo: 'Crecimiento' },
        { nombreTipoLaboreo: 'Cosecha', nombreMomentoLaboreo: 'Cosecha' },
      ],
    },
  ],
  empleados: [
    { nombre: 'Juan', apellido: 'Pérez' },
    { nombre: 'María', apellido: 'González' },
    { nombre: 'Carlos', apellido: 'Rodríguez' },
    { nombre: 'Ana', apellido: 'Martínez' },
    { nombre: 'Pedro', apellido: 'López' },
  ],
};


const handleError = (error) => {
  if (error.response) {
    return {
      success: false,
      message: error.response.data.error || error.response.data.mensaje || 'Error en la solicitud',
      status: error.response.status
    };
  } else if (error.request) {
    return {
      success: false,
      message: 'No se pudo conectar con el servidor. Asegúrate de que el backend esté ejecutándose.',
      status: 0
    };
  } else {
    return {
      success: false,
      message: error.message || 'Error desconocido',
      status: 0
    };
  }
};

export const iniciarRegistro = async () => {
  if (DEMO_MODE) {
    return { success: true, data: mockData.campos };
  }
  
  try {
    const response = await axios.post(`${API_URL}/iniciar`);
    return { success: true, data: response.data };
  } catch (error) {
    return handleError(error);
  }
};

export const seleccionarCampo = async (nombreCampo) => {
  if (DEMO_MODE) {
    return { success: true, data: mockData.lotes };
  }
  
  try {
    const response = await axios.post(`${API_URL}/seleccionar-campo`, { nombreCampo });
    return { success: true, data: response.data };
  } catch (error) {
    return handleError(error);
  }
};

export const seleccionarLotes = async (numerosLote) => {
  if (DEMO_MODE) {
    // Retorna la info de los lotes seleccionados
    const lotesSeleccionados = numerosLote.map(num => mockData.lotesInfo[num - 1]);
    return { success: true, data: lotesSeleccionados };
  }
  
  try {
    const response = await axios.post(`${API_URL}/seleccionar-lotes`, { numerosLote });
    return { success: true, data: response.data };
  } catch (error) {
    return handleError(error);
  }
};

export const seleccionarLaboreos = async (laboreosPorLote) => {
  if (DEMO_MODE) {
    return { success: true, data: { mensaje: 'Órdenes de laboreo seleccionadas correctamente' } };
  }
  
  try {
    const response = await axios.post(`${API_URL}/seleccionar-laboreo`, { laboreosPorLote });
    return { success: true, data: response.data };
  } catch (error) {
    return handleError(error);
  }
};

export const enviarFechaHora = async (fechasPorLaboreo) => {
  if (DEMO_MODE) {
    return { success: true, data: mockData.empleados };
  }
  
  try {
    // Convertir las fechas al formato esperado por el backend
    const fechasParaEnviar = fechasPorLaboreo.map(item => ({
      numeroLote: item.numeroLote,
      laboreo: item.laboreo,
      fechaHoraInicio: item.fechaHoraInicio.toISOString().slice(0, 19),
      fechaHoraFin: item.fechaHoraFin.toISOString().slice(0, 19)
    }));

    const response = await axios.post(`${API_URL}/fecha-hora`, {
      fechasPorLaboreo: fechasParaEnviar
    });
    return { success: true, data: response.data };
  } catch (error) {
    return handleError(error);
  }
};

export const seleccionarEmpleado = async (empleadosPorLaboreo) => {
  if (DEMO_MODE) {
    return { success: true, data: { mensaje: 'Empleados seleccionados correctamente' } };
  }
  
  try {
    const response = await axios.post(`${API_URL}/seleccionar-empleado`, {
      empleadosPorLaboreo
    });
    return { success: true, data: response.data };
  } catch (error) {
    return handleError(error);
  }
};

export const confirmarRegistro = async () => {
  if (DEMO_MODE) {
    // Simula que los laboreos son válidos (puedes cambiar a false para probar el otro caso)
    return { success: true, data: { esValido: true } };
  }
  
  try {
    const response = await axios.post(`${API_URL}/confirmar-registro`);
    return { success: true, data: response.data };
  } catch (error) {
    return handleError(error);
  }
};

export const finalizarRegistro = async () => {
  if (DEMO_MODE) {
    return { success: true, data: { mensaje: 'Registro de laboreos finalizado' } };
  }
  
  try {
    const response = await axios.post(`${API_URL}/finalizar`);
    return { success: true, data: response.data };
  } catch (error) {
    return handleError(error);
  }
};

