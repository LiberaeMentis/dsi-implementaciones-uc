# Frontend - Registro de Laboreos en Lotes

Frontend moderno desarrollado con React + Vite + Material-UI para el sistema de registro de laboreos en lotes.

## 🚀 Tecnologías

- **React 18** - Biblioteca de UI
- **Vite** - Build tool y dev server
- **Material-UI (MUI)** - Framework de componentes
- **Axios** - Cliente HTTP
- **date-fns** - Manejo de fechas
- **Context API** - Manejo de estado global

## 📋 Prerequisitos

- Node.js 16+ 
- npm o yarn
- Backend Spring Boot corriendo en `http://localhost:8080`

## 🔧 Instalación

```bash
# Instalar dependencias
npm install
```
cd front
npm install
npm run dev

## 🏃 Ejecución

```bash
# Modo desarrollo
npm run dev

# Build para producción
npm run build

# Preview del build
npm run preview
```

La aplicación estará disponible en `http://localhost:3000`

### 🎭 Modo Demo (sin backend)

Para probar el frontend sin necesidad del backend, abre el archivo `src/services/laboreoService.js` y cambia:

```javascript
const DEMO_MODE = true;  // Ya está en true por defecto
```

En modo demo:
- Usa datos mockeados predefinidos
- Simula delays de red realistas
- Permite navegar por todos los pasos del wizard
- Perfecto para probar el diseño y la UX

Para conectar con el backend real, cambia a:
```javascript
const DEMO_MODE = false;
```

## 🎯 Características

- **Wizard de 7 pasos** con navegación intuitiva
- **Sidebar** con indicadores de progreso
- **Validaciones** en cada paso
- **Feedback visual** con alerts y mensajes
- **Diseño responsive** para todos los dispositivos
- **Tema personalizado** con colores agro/campo
- **Manejo de errores** robusto

## 📁 Estructura

```
src/
├── components/
│   ├── layout/          # Layout principal y sidebar
│   └── steps/           # Componentes de cada paso
├── context/             # Context API (estado global)
├── services/            # Servicios API
├── App.jsx              # Componente raíz
├── main.jsx            # Entry point
└── theme.js            # Tema Material-UI
```

## 🔄 Flujo de la aplicación

1. **Paso 1**: Seleccionar Campo
2. **Paso 2**: Seleccionar Lotes
3. **Paso 3**: Seleccionar Laboreos por Lote
4. **Paso 4**: Definir Fecha y Hora
5. **Paso 5**: Seleccionar Empleado
6. **Paso 6**: Confirmar Registro
7. **Paso 7**: Ver Resultado

## 🎨 Tema

El tema utiliza una paleta de colores inspirada en el campo:
- **Primary**: Verde (#4caf50)
- **Secondary**: Marrón (#8d6e63)
- **Tipografía**: Roboto
- **Border radius**: 8px

## 🔗 API

El frontend se comunica con el backend a través de proxy configurado en Vite:
- `/laboreos/*` → `http://localhost:8080/laboreos/*`

## 📝 Notas

- Asegúrate de que el backend esté corriendo antes de iniciar el frontend
- Las fechas se validan para ser anteriores a la fecha actual
- Los laboreos de tipo "Siembra" y "Cosecha" no son válidos en este flujo

