-- 1. Provincias (No depende de nadie)
DROP TABLE IF EXISTS provincias CASCADE;
CREATE TABLE provincias (
  id INT PRIMARY KEY,
  nombre VARCHAR(50) NOT NULL
);

-- 2. Municipios (Depende de Provincias)
DROP TABLE IF EXISTS municipios CASCADE;
CREATE TABLE municipios (
  id INT PRIMARY KEY,
  provinciaId INT,
  nombre VARCHAR(150) NOT NULL,
  CONSTRAINT fk_municipios_provincias FOREIGN KEY (provinciaId) REFERENCES provincias (id)
);

-- 3. Distritos (Depende de Municipios)
DROP TABLE IF EXISTS distritos CASCADE;
CREATE TABLE distritos (
  id INT PRIMARY KEY,
  nombre VARCHAR(150),
  municipioId INT,
  CONSTRAINT fk_distritos_municipios FOREIGN KEY (municipioId) REFERENCES municipios (id)
);

-- 4. Secciones (Depende de Municipios y Distritos)
DROP TABLE IF EXISTS secciones CASCADE;
CREATE TABLE secciones (
  id INT PRIMARY KEY,
  nombre VARCHAR(150),
  municipioId INT,
  distritoId INT,
  CONSTRAINT fk_secciones_distritos FOREIGN KEY (distritoId) REFERENCES distritos (id),
  CONSTRAINT fk_secciones_municipios FOREIGN KEY (municipioId) REFERENCES municipios (id)
);

-- 5. Barrios (Depende de Secciones)
DROP TABLE IF EXISTS barrios CASCADE;
CREATE TABLE barrios (
  id INT PRIMARY KEY,
  nombre VARCHAR(150),
  seccionId INT,
  CONSTRAINT fk_barrios_secciones FOREIGN KEY (seccionId) REFERENCES secciones (id)
);

-- 6. Sub_barrios (Depende de Barrios)
DROP TABLE IF EXISTS sub_barrios CASCADE;
CREATE TABLE sub_barrios (
  id INT PRIMARY KEY,
  nombre VARCHAR(150),
  barrioId INT,
  CONSTRAINT fk_sub_barrios_barrios FOREIGN KEY (barrioId) REFERENCES barrios (id)
);