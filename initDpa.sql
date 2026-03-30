-- 1. Provincias (No depende de nadie)
CREATE SCHEMA IF NOT EXISTS "public";
SET SCHEMA "public";

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