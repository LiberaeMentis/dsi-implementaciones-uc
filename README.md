# Registrar Laboreo en Lotes

Sistema para gestionar el registro de laboreos (tareas agrícolas) en lotes de cultivo. Implementado con **múltiples tecnologías** (Spring Boot, Django, React) para fines académicos.

---

## 📖 Contexto del Proyecto

### Dominio: Administración de Campos

**Entidades principales:**
- **Campo**: Terreno agrícola dividido en lotes
- **Lote**: Subdivisión de un campo con un tipo de suelo
- **Proyecto de Cultivo**: Cultivo vigente en un lote (Soja, Maíz, Maní, Girasol)
- **Laboreo**: Tarea agrícola realizada (Arado, Siembra, Cosecha, etc.)
- **Empleado**: Persona que realiza el laboreo

**Flujo del caso de uso:**
1. Seleccionar un campo
2. Seleccionar lotes del campo
3. Definir qué laboreos se realizarán en cada lote
4. Indicar fecha/hora de inicio y fin
5. Seleccionar el empleado que realizó la tarea
6. Confirmar y registrar los laboreos

---

## 📁 Estructura del Proyecto

```
cu-registrar-laboreo-en-lotes/
├── front/              # Frontend React + Vite + Material-UI
├── java/               # Backend Spring Boot (datos en memoria)
├── java-persistencia/  # Backend Spring Boot + JPA + H2 (persistencia)
├── python/             # Backend Django + SQLite in-memory
└── README.md           # Este archivo
```

**4 aplicaciones independientes:**
- **1 Frontend** compatible con cualquiera de los 3 backends
- **3 Backends** con la misma API REST, diferentes implementaciones

---

## 🚀 Inicio Rápido

### Opción 1: Frontend + Java (In-Memory)

```bash
# Terminal 1: Backend Java
cd java
./mvnw spring-boot:run

# Terminal 2: Frontend
cd front
npm install
npm run dev
```

Frontend: `http://localhost:3000` | Backend: `http://localhost:8080`

---

### Opción 2: Frontend + Java con Persistencia (JPA + H2)

```bash
# Terminal 1: Backend Java con H2
cd java-persistencia
mvn spring-boot:run

# Terminal 2: Frontend
cd front
npm install
npm run dev
```

**Ventajas:**
- Base de datos H2 in-memory
- Consola H2: `http://localhost:8080/h2-console` (URL: `jdbc:h2:mem:laboreosdb`, user: `sa`)
- Relaciones JPA con claves naturales
- Script SQL para poblar datos (`data.sql`)

---

### Opción 3: Frontend + Python (Django)

```bash
# Terminal 1: Backend Python
cd python
pip install -r requirements.txt
python3 iniciar_servidor.py

# Terminal 2: Frontend
cd front
npm install
npm run dev
```

**El script `iniciar_servidor.py`:**
- Aplica migraciones
- Puebla datos iniciales
- Inicia servidor en puerto 8080

---

### Opción 4: Solo Frontend (Modo Demo)

```bash
cd front
npm install
npm run dev
```

Edita `front/src/services/laboreoService.js`:
```javascript
const DEMO_MODE = true;  // Usa datos mockeados
```

---

## 🛠️ Requisitos Previos

### Frontend
- **Node.js 16+** y npm
- Puerto 3000 disponible

### Backend Java / Java-Persistencia
- **Java 17+**
- **Maven 3.6+** (incluido en wrapper `mvnw`)
- Puerto 8080 disponible

### Backend Python
- **Python 3.11+**
- **pip**
- Puerto 8080 disponible

---

## 💻 Información por Aplicación

### 🌐 Frontend (`front/`)

**Tecnologías:**
- React 18
- Material-UI (MUI)
- Vite (build tool)
- Axios (HTTP client)
- Context API (estado global)

**Características:**
- Wizard de 7 pasos con sidebar de progreso
- Validaciones en cada paso
- Diseño responsive con tema agro/campo
- Modo demo con datos mockeados
- Compatible con todos los backends sin cambios

**Estructura:**
```
front/
├── src/
│   ├── components/
│   │   ├── layout/         # Layout principal y sidebar
│   │   └── steps/          # Componentes de cada paso (Paso1Campo, Paso2Lotes, etc.)
│   ├── services/           # laboreoService.js (API client)
│   ├── App.jsx
│   └── theme.js            # Tema Material-UI
└── vite.config.js          # Proxy a backend
```

**Configuración:**
- Proxy configurado: `/laboreos` → `http://localhost:8080/laboreos`
- Cambiar entre modo demo/backend en `laboreoService.js`

---

### ☕ Java In-Memory (`java/`)

**Tecnologías:**
- Spring Boot 3.2
- Java 17
- Maven

**Características:**
- **Datos en memoria**: Cargados al iniciar con `@PostConstruct`
- **Sin base de datos**: Todo en memoria (se pierde al reiniciar)
- **Patrón experto**: Toda la lógica en las clases de dominio

**Estructura:**
```
java/
└── src/main/java/com/dsi/laboreos/
    ├── model/             # Entidades de dominio
    ├── controller/        # REST controllers
    ├── service/           # GestorLaboreos (lógica de negocio)
    └── dto/               # Data Transfer Objects
```

**Datos precargados:**
- 3 Campos (2 habilitados)
- 4 Lotes con proyectos vigentes
- 4 Cultivos (Soja, Maíz, Maní, Girasol)
- 8 Tipos de Laboreo
- 3 Empleados

---

### 💾 Java con Persistencia (`java-persistencia/`)

**Tecnologías:**
- Spring Boot 3.2
- Spring Data JPA
- Hibernate
- H2 Database (in-memory)
- Java 17
- Maven

**Características:**
- **Persistencia real**: Base de datos H2 in-memory
- **JPA/Hibernate**: Mapeo objeto-relacional
- **Claves naturales**: PKs simples (`nombre`, `numero`) y compuestas (`@IdClass`)
- **Relaciones unidireccionales**: Según diagrama de dominio
- **FetchType.EAGER**: Todas las relaciones cargadas automáticamente
- **Script SQL**: `data.sql` para poblar datos al iniciar


**Consola H2:**
- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:laboreosdb`
- Usuario: `sa`
- Password: *(vacío)*

**Estructura:**
```
java-persistencia/
├── src/main/
│   ├── java/com/dsi/laboreos/
│   │   ├── model/            # Entidades JPA con @Entity
│   │   ├── repository/       # JpaRepository interfaces
│   │   ├── service/          # GestorLaboreos con @EventListener
│   │   ├── controller/       # REST controllers
│   │   ├── dto/              # DTOs
│   │   └── config/           # CorsConfig
│   └── resources/
│       ├── application.properties
│       └── data.sql          # Script de población de datos
└── pom.xml
```

**Carga de datos:**
- `@EventListener(ContextRefreshedEvent.class)`: Carga datos **después** de que `data.sql` se ejecuta
- Evita el problema de cargar en el constructor antes de que la BD esté lista

---

### 🐍 Python Django (`python/`)

**Tecnologías:**
- Django 5.0
- Django ORM
- SQLite in-memory
- Python 3.11+

**Características:**
- **SQLite in-memory**: Base de datos temporal (se pierde al reiniciar)
- **Django ORM**: Mapeo objeto-relacional
- **Claves naturales**: Mismo esquema que `java-persistencia`
- **Migraciones**: Generadas automáticamente
- **Comando custom**: `python manage.py poblar_datos`

**Estructura:**
```
python/
├── laboreos/
│   ├── models/               # Modelos Django (Campo, Lote, etc.)
│   ├── boundaries/           # PantAdmLaboreos (API views)
│   ├── controller/           # GestorLaboreos (lógica de negocio)
│   ├── dtos.py              # Dataclasses para responses
│   ├── management/
│   │   └── commands/
│   │       └── poblar_datos.py
│   └── migrations/
├── laboreos_project/         # Settings Django
├── manage.py
└── iniciar_servidor.py       # Script todo-en-uno
```

**Script de inicio:**
```bash
python3 iniciar_servidor.py
```

Hace:
1. Migraciones (`migrate`)
2. Poblar datos (`poblar_datos`)
3. Iniciar servidor en puerto 8080

---

## 📖 Endpoints de la API

**Todos los backends exponen la misma API REST:**

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/laboreos/iniciar` | Inicia el caso de uso, devuelve campos disponibles |
| POST | `/laboreos/seleccionar-campo` | Selecciona un campo, devuelve sus lotes |
| POST | `/laboreos/seleccionar-lotes` | Selecciona lotes, devuelve info del proyecto vigente |
| POST | `/laboreos/seleccionar-laboreo` | Define qué laboreos se harán por lote |
| POST | `/laboreos/fecha-hora` | Envía fecha/hora inicio/fin, devuelve empleados |
| POST | `/laboreos/seleccionar-empleado` | Selecciona el empleado que hizo el laboreo |
| POST | `/laboreos/confirmar-registro` | Confirma y crea los laboreos |
| POST | `/laboreos/finalizar` | Finaliza el caso de uso |

**Ejemplo:**
```bash
# Iniciar caso de uso
curl -X POST http://localhost:8080/laboreos/iniciar

# Seleccionar campo
curl -X POST http://localhost:8080/laboreos/seleccionar-campo \
  -H "Content-Type: application/json" \
  -d '{"nombreCampo":"Campo Norte"}'
```

## 🔧 Uso con VS Code

El proyecto incluye configuración de VS Code para ejecutar automáticamente:

1. Abrir el proyecto en VS Code
2. Ir a **Run and Debug** (Ctrl+Shift+D)
3. Selecciona:
   - **"Full Stack (Java + React)"** - Backend java/ + frontend
   - **"Full Stack (Java Persistencia + React)"** - Backend java-persistencia/ + frontend
   - **"Full Stack (Python + React)"** - Backend python/ + frontend
   - **"Frontend Only"** - Solo frontend en modo demo

---

## 🐛 Troubleshooting

### ❌ Backend Java no inicia
- Verificar Java 17+: `java -version`
- Puerto 8080 ocupado: `lsof -ti:8080 | xargs kill -9` (macOS/Linux)
- Ejecutar: `cd java && ./mvnw clean install`


### ❌ Backend Python: "no such table"
- Usa `python3 iniciar_servidor.py` (hace migraciones automáticamente)
- O ejecuta manualmente: `python manage.py migrate`

### ❌ Frontend no se conecta
- Backend corriendo en `http://localhost:8080`? Verifica con `curl`
- CORS habilitado? Revisar la consola del navegador (F12)
- Proxy configurado? Revisar `front/vite.config.js`

### ❌ `./mvnw` no funciona

```bash
mvnw.cmd spring-boot:run
```

### ❌ Error CRLF / LF

```bash
git config --global core.autocrlf false
```

### ❌ Python no reconoce `python3`

```bash
python iniciar_servidor.py
```

### ❌ PowerShell bloquea scripts
Windows, por defecto, bloquea la ejecución de scripts para evitar que un malware descargado se ejecute sin que te des cuenta.

Si intentás correr un script y ves algo como:

```
cannot be loaded because running scripts is disabled on this system
```
Eso es la Execution Policy bloqueándolo.
La solución es ejecutar el siguiete comando y reiniciar la terminal.

```powershell
Set-ExecutionPolicy RemoteSigned
```

### ❌ Encoding raro

```powershell
chcp 65001
```

---

## 📝 Notas Importantes

### Claves Primarias Naturales (Java-Persistencia y Python)

Ambas implementaciones con persistencia usan **claves naturales** en lugar de IDs auto-generados:

**Ventajas:**
- Más cercano al dominio real
- PKs con significado de negocio
- Más académico/didáctico

**Desventajas:**
- Más complejo de implementar
- PKs compuestas para algunas entidades
- Requiere `@IdClass` (Java) o `unique_together` (Python)

**Ejemplos:**
```java
// Java-Persistencia
@Entity
public class Campo {
    @Id
    private String nombre;  // PK natural
}

@Entity
@IdClass(EmpleadoId.class)
public class Empleado {
    @Id
    private String nombre;     // PK compuesta
    @Id
    private String apellido;   // PK compuesta
}
```

```python
# Python
class Campo(models.Model):
    nombre = models.CharField(max_length=100, primary_key=True)

class Empleado(models.Model):
    nombre = models.CharField(max_length=100)
    apellido = models.CharField(max_length=100)
    
    class Meta:
        unique_together = [['nombre', 'apellido']]
```

---

## 👥 Autores

Proyecto desarrollado para la materia **Diseño de Sistemas de Información (DSI)** - 2026

---

**Versión**: 1.0  
**Última actualización**: Enero 2026
