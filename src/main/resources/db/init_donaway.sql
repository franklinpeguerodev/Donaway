
CREATE SCHEMA IF NOT EXISTS "public";
SET SCHEMA "public";
-- -----------------------------------------------------
-- Modulo de Identificacion y Seguridad
-- -----------------------------------------------------

-- Tabla Ubicacion
CREATE TABLE IF NOT EXISTS ubicacion (
  	id_ubicacion VARCHAR (36) PRIMARY KEY,
  	region VARCHAR (20),
  	provincia INT,
  	municipio INT,
  	distrito INT,
  	seccion INT,
  	barrio INT,
  	sub_barrio INT
);

--Tabla instucion medica
CREATE TABLE IF NOT EXISTS institucion_medica (
	rnc_institucion VARCHAR (9) PRIMARY KEY,
	id_ubicacion VARCHAR (36),		
	nombre VARCHAR (50),
	contacto_emergencia VARCHAR (20),
	FOREIGN KEY (id_ubicacion) REFERENCES ubicacion (id_ubicacion)
	
);

--Tabla personal medico
CREATE TABLE IF NOT EXISTS personal_medico(
	document_id_personal VARCHAR(20) PRIMARY KEY,
	rnc_institucion VARCHAR(9),
	nombre VARCHAR (50),
	fecha_nacimiento DATE,
	id_ubicacion VARCHAR (36),
	rol VARCHAR (20),
	telefono VARCHAR (15),
	email VARCHAR (100),
	credenciales_encriptadas VARCHAR (255),
	fecha_registro DATETIME,
	ultima_acceso DATETIME,
	FOREIGN KEY (rnc_institucion) REFERENCES institucion_medica(rnc_institucion),
	FOREIGN KEY (id_ubicacion) REFERENCES ubicacion (id_ubicacion)
);


-- -----------------------------------------------------
-- Modulo del Donante (Entidad Maestra)
-- -----------------------------------------------------

--Tabla donante
CREATE TABLE IF NOT EXISTS donante (
	document_id_donante VARCHAR (20) PRIMARY KEY,
	nombre VARCHAR (50),
	sexo VARCHAR (10),
	fecha_nacimiento DATE,
	id_ubicacion VARCHAR (36),
	telefono VARCHAR (15),
	email VARCHAR (100),
	fecha_registro DATETIME,
	tipo_sangre VARCHAR (2),
	factor_rh VARCHAR (1),
	ultima_donacion DATETIME,
	estado_disponibilidad VARCHAR (8),
	autorizacion_contacto BOOLEAN,
	FOREIGN KEY (id_ubicacion) REFERENCES ubicacion (id_ubicacion)
	
);
--Tabla donacion
CREATE TABLE IF NOT EXISTS donacion(
	id_donacion VARCHAR(36),
	document_id_donante VARCHAR(36),
	document_id_personal VARCHAR(36),
	rnc_institucion VARCHAR (9),
	fecha_extraccion DATETIME,
	tipo_donacion VARCHAR(20),
	tipo_componente VARCHAR(20),
	cantidad_pinta DECIMAL(4,2),
	FOREIGN KEY (document_id_donante) REFERENCES donante(document_id_donante),
	FOREIGN KEY (document_id_personal) REFERENCES personal_medico(document_id_personal),
	FOREIGN KEY (rnc_institucion) REFERENCES institucion_medica(rnc_institucion)
);
--Tabla datos triaje
CREATE TABLE IF NOT EXISTS datos_triaje (
	id_triaje VARCHAR(36) PRIMARY KEY,
	id_donacion VARCHAR(36),
	peso DECIMAL(5,2),
	presion_sistolica INT,
	presion_distolica INT,
	pulso INT,
	hemoglobina DECIMAL(4,2),
	temperatura DECIMAL(4,2),
	FOREIGN KEY (id_donacion) REFERENCES donacion(id_donacion)
	);
--tabla analisis laboratorio
CREATE TABLE IF NOT EXISTS analisis_lab(
	id_analisis VARCHAR(36),
	id_donacion VARCHAR(36),
	vih BOOLEAN,
	hepatitis_b BOOLEAN,
	hepatitis_c BOOLEAN,
	sifilis BOOLEAN,
	chacas BOOLEAN,
	nat BOOLEAN,
	otros VARCHAR(100),
	FOREIGN KEY (id_donacion) REFERENCES donacion(id_donacion)
);

-- -----------------------------------------------------
-- Modulo de Inventario
-- -----------------------------------------------------

-- Tabla unidad de hemoderivados
CREATE TABLE IF NOT EXISTS unidad_hemoderivados (
	id_hemorivada VARCHAR(36),
	codigo_barras VARCHAR(36),
	id_donacion VARCHAR(36),
	rnc_institucion VARCHAR(9),
	tipo_componente VARCHAR(10),
	estado_unidad VARCHAR(10),
	fecha_caducidad DATE,
	FOREIGN KEY (id_donacion) REFERENCES donacion(id_donacion),
	FOREIGN KEY (rnc_institucion) REFERENCES institucion_medica (rnc_institucion)
);

-- -----------------------------------------------------
-- Modulo de Solicitud de Transfusion
-- -----------------------------------------------------

--Tabla receptor de sangre
CREATE TABLE IF NOT EXISTS receptor(
	document_id_receptor VARCHAR (20) PRIMARY KEY,
	nombre VARCHAR (50),
	fecha_nacimiento DATE,
	sexo VARCHAR (10),
	peso DECIMAL(5,2),
	telefono VARCHAR (15),
	fecha_registro DATETIME,
	tipo_sangre VARCHAR (2),
	factor_rh VARCHAR (1)
);

--Tabla solicitud de transfusion de sangre
CREATE TABLE IF NOT EXISTS solicitud_transfusion (
	id_solicitud VARCHAR(36),
	document_id_receptor VARCHAR (20),
	rnc_institucion VARCHAR (9),
	fecha_solicitud DATETIME,
	fecha_entrega DATETIME,
	estado_solicitud VARCHAR (9),
	prioridad VARCHAR (10),
	FOREIGN KEY (document_id_receptor) REFERENCES receptor(document_id_receptor),
	FOREIGN KEY (rnc_institucion) REFERENCES institucion_medica(rnc_institucion)
);

--Tabla transfusion de sangren
CREATE TABLE IF NOT EXISTS transfusion(
	id_transfusion VARCHAR(36) PRIMARY KEY,
	id_solicitud VARCHAR(36),
	rnc_institucion VARCHAR (9),
	codigo_barras VARCHAR(36),	
	tipo_transfusion VARCHAR(9),
	fecha_transfusion DATETIME,
	FOREIGN KEY (id_solicitud) REFERENCES solicitud_transfusion(id_solicitud),
	FOREIGN KEY (rnc_institucion) REFERENCES institucion_medica(rnc_institucion),
	FOREIGN KEY (codigo_barras) REFERENCES  unidad_hemoderivados (codigo_barras)
);

-- -----------------------------------------------------
-- Modulo de historial
-- -----------------------------------------------------
--Completar

-- -----------------------------------------------------
-- Modulo ubicacion DPA
-- -----------------------------------------------------

-- 1. Provincias (No depende de nadie)

CREATE TABLE IF NOT EXISTS provincias (
  id INT PRIMARY KEY,
  nombre VARCHAR(50) NOT NULL
);

-- 2. Municipios (Depende de Provincias)

CREATE TABLE IF NOT EXISTS municipios (
  id INT PRIMARY KEY,
  provinciaId INT,
  nombre VARCHAR(150) NOT NULL,
  CONSTRAINT fk_municipios_provincias FOREIGN KEY (provinciaId) REFERENCES provincias (id)
);

-- 3. Distritos (Depende de Municipios)

CREATE TABLE IF NOT EXISTS distritos (
  id INT PRIMARY KEY,
  nombre VARCHAR(150),
  municipioId INT,
  CONSTRAINT fk_distritos_municipios FOREIGN KEY (municipioId) REFERENCES municipios (id)
);

-- 4. Secciones (Depende de Municipios y Distritos)

CREATE TABLE IF NOT EXISTS secciones (
  id INT PRIMARY KEY,
  nombre VARCHAR(150),
  municipioId INT,
  distritoId INT,
  CONSTRAINT fk_secciones_distritos FOREIGN KEY (distritoId) REFERENCES distritos (id),
  CONSTRAINT fk_secciones_municipios FOREIGN KEY (municipioId) REFERENCES municipios (id)
);

-- 5. Barrios (Depende de Secciones)

CREATE TABLE IF NOT EXISTS barrios (
  id INT PRIMARY KEY,
  nombre VARCHAR(150),
  seccionId INT,
  CONSTRAINT fk_barrios_secciones FOREIGN KEY (seccionId) REFERENCES secciones (id)
);

-- 6. Sub_barrios (Depende de Barrios)

CREATE TABLE IF NOT EXISTS sub_barrios (
  id INT PRIMARY KEY,
  nombre VARCHAR(150),
  barrioId INT,
  CONSTRAINT fk_sub_barrios_barrios FOREIGN KEY (barrioId) REFERENCES barrios (id)
);