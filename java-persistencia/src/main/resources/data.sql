-- Script de poblado de datos para H2 con PRIMARY KEYS NATURALES
-- Se ejecuta automáticamente después de la creación del esquema

-- ============================================
-- ESTADOS (PK: nombre)
-- ============================================
INSERT INTO estados (nombre, descripcion, estado_final) VALUES
('En Preparación', 'Proyecto en preparación', false),
('Vigente', 'Proyecto vigente', false),
('Finalizado', 'Proyecto finalizado', true),
('Cancelado', 'Proyecto cancelado', true);

-- ============================================
-- TIPOS DE SUELO (PK: numero)
-- ============================================
INSERT INTO tipos_suelo (numero, nombre, descripcion) VALUES
(1, 'Arcilloso', 'Suelo con alto contenido de arcilla'),
(2, 'Arenoso', 'Suelo con alto contenido de arena'),
(3, 'Limoso', 'Suelo con alto contenido de limo'),
(4, 'Franco', 'Suelo equilibrado con arcilla, arena y limo'),
(5, 'Humífero', 'Suelo rico en materia orgánica');

-- ============================================
-- MOMENTOS DE LABOREO (PK: nombre)
-- ============================================
INSERT INTO momentos_laboreo (nombre, descripcion) VALUES
('Pre-siembra', 'Laboreo antes de la siembra'),
('Siembra', 'Momento de siembra'),
('Post-siembra', 'Laboreo después de la siembra'),
('Crecimiento', 'Laboreo durante el crecimiento'),
('Cosecha', 'Momento de cosecha');

-- ============================================
-- TIPOS DE LABOREO (PK: nombre)
-- ============================================
INSERT INTO tipos_laboreo (nombre, descripcion) VALUES
('Arado', 'Roturar la tierra'),
('Rastrillado', 'Nivelar y desmenuzar la tierra'),
('Siembra', 'Plantación del cultivo'),
('Escardillado', 'Remoción de malezas entre surcos'),
('Cosecha', 'Recolección del cultivo'),
('Fumigación', 'Aplicación de agroquímicos'),
('Riego', 'Aplicación de agua al cultivo'),
('Rolado', 'Aplastamiento de rastrojos');

-- ============================================
-- CULTIVOS (PK: nombre, FK: tipo_suelo_numero)
-- ============================================
INSERT INTO cultivos (nombre, tipo_suelo_numero) VALUES
('Soja', 1),
('Maní', 1),
('Girasol', 1),
('Maíz', 2);

-- ============================================
-- ÓRDENES DE LABOREO (PK compuesta: orden + tipo_laboreo_nombre + momento_laboreo_nombre)
-- ============================================
INSERT INTO ordenes_laboreo (orden, tipo_laboreo_nombre, momento_laboreo_nombre) VALUES
(1, 'Arado', 'Pre-siembra'),
(2, 'Rastrillado', 'Pre-siembra'),
(3, 'Siembra', 'Siembra'),
(4, 'Escardillado', 'Post-siembra'),
(5, 'Cosecha', 'Cosecha'),
(6, 'Fumigación', 'Post-siembra'),
(7, 'Riego', 'Crecimiento');

-- ============================================
-- RELACIÓN CULTIVO - ORDEN DE LABOREO (ManyToMany)
-- ============================================
-- Soja: Arado, Rastrillado, Siembra, Escardillado, Cosecha
INSERT INTO cultivo_orden_laboreo (cultivo_nombre, orden_orden, orden_tipo_laboreo_nombre, orden_momento_laboreo_nombre) VALUES
('Soja', 1, 'Arado', 'Pre-siembra'),
('Soja', 2, 'Rastrillado', 'Pre-siembra'),
('Soja', 3, 'Siembra', 'Siembra'),
('Soja', 4, 'Escardillado', 'Post-siembra'),
('Soja', 5, 'Cosecha', 'Cosecha');

-- Maní: Arado, Rastrillado, Siembra, Fumigación, Cosecha
INSERT INTO cultivo_orden_laboreo (cultivo_nombre, orden_orden, orden_tipo_laboreo_nombre, orden_momento_laboreo_nombre) VALUES
('Maní', 1, 'Arado', 'Pre-siembra'),
('Maní', 2, 'Rastrillado', 'Pre-siembra'),
('Maní', 3, 'Siembra', 'Siembra'),
('Maní', 6, 'Fumigación', 'Post-siembra'),
('Maní', 5, 'Cosecha', 'Cosecha');

-- Girasol: Arado, Rastrillado, Siembra, Riego, Cosecha
INSERT INTO cultivo_orden_laboreo (cultivo_nombre, orden_orden, orden_tipo_laboreo_nombre, orden_momento_laboreo_nombre) VALUES
('Girasol', 1, 'Arado', 'Pre-siembra'),
('Girasol', 2, 'Rastrillado', 'Pre-siembra'),
('Girasol', 3, 'Siembra', 'Siembra'),
('Girasol', 7, 'Riego', 'Crecimiento'),
('Girasol', 5, 'Cosecha', 'Cosecha');

-- ============================================
-- Maíz: Arado, Rastrillado, Siembra, Riego, Fumigación, Cosecha
-- (ajustá la lista si querés otra estrategia)
-- ============================================
INSERT INTO cultivo_orden_laboreo (
  cultivo_nombre,
  orden_orden,
  orden_tipo_laboreo_nombre,
  orden_momento_laboreo_nombre
) VALUES
('Maíz', 1, 'Arado', 'Pre-siembra'),
('Maíz', 2, 'Rastrillado', 'Pre-siembra'),
('Maíz', 3, 'Siembra', 'Siembra'),
('Maíz', 7, 'Riego', 'Crecimiento'),
('Maíz', 6, 'Fumigación', 'Post-siembra'),
('Maíz', 5, 'Cosecha', 'Cosecha');

-- ============================================
-- EMPLEADOS (PK compuesta: nombre + apellido)
-- ============================================
INSERT INTO empleados (nombre, apellido) VALUES
('Juan', 'Pérez'),
('María', 'González'),
('Carlos', 'Rodríguez');

-- ============================================
-- CAMPOS (PK: nombre)
-- ============================================
INSERT INTO campos (nombre, cantidad_hectareas, habilitado) VALUES
('Campo Norte', 25.5, true),
('Campo Sur', 20.0, true),
('Campo Este', 15.0, false);

-- ============================================
-- LOTES (PK: numero, FK: tipo_suelo_numero, campo_nombre)
-- ============================================
INSERT INTO lotes (numero, cantidad_hectareas, tipo_suelo_numero, campo_nombre) VALUES
(1, 10.5, 1, 'Campo Norte'),
(2, 15.0, 2, 'Campo Norte'),
(3, 8.0, 1, 'Campo Sur'),
(4, 12.0, 1, 'Campo Sur');

-- ============================================
-- PROYECTOS DE CULTIVO (PK: numero, FK: cultivo_nombre, estado_nombre, lote_numero)
-- ============================================
-- Calculamos fechas dinámicamente usando funciones de H2
INSERT INTO proyectos_cultivo (numero, cultivo_nombre, estado_nombre, fecha_inicio, fecha_fin, observaciones, lote_numero) VALUES
(1, 'Soja', 'Vigente', DATEADD('MONTH', -2, CURRENT_DATE), NULL, '', 1),
(2, 'Maíz', 'Vigente', DATEADD('MONTH', -1, CURRENT_DATE), NULL, '', 2),
(3, 'Maní', 'Vigente', DATEADD('DAY', -15, CURRENT_DATE), NULL, '', 3),
(4, 'Girasol', 'Vigente', DATEADD('MONTH', -3, CURRENT_DATE), NULL, '', 4);
