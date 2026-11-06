/* Esquema real de la BD donaway extraído con SHOW CREATE TABLE el 2026-10-07 */
-- ubicacion
ubicacion	CREATE TABLE `ubicacion` (\n  `id_ubicacion` binary(16) NOT NULL,\n  `id_region` int DEFAULT NULL,\n  `id_provincia` int DEFAULT NULL,\n  `id_municipio` int DEFAULT NULL,\n  `id_distrito` int DEFAULT NULL,\n  `id_seccion` int DEFAULT NULL,\n  `id_barrio` int DEFAULT NULL,\n  `id_sub_barrio` int DEFAULT NULL,\n  `direccion` varchar(100) DEFAULT NULL,\n  PRIMARY KEY (`id_ubicacion`),\n  KEY `fk_ubicacicion_region` (`id_region`),\n  KEY `fk_ubicacicion_provincias` (`id_provincia`),\n  KEY `fk_ubicacicion_municipios` (`id_municipio`),\n  KEY `fk_ubicacicion_distritos` (`id_distrito`),\n  KEY `fk_ubicacicion_secciones` (`id_seccion`),\n  KEY `fk_ubicacicion_barrios` (`id_barrio`),\n  KEY `fk_ubicacicion_sub_barrios` (`id_sub_barrio`),\n  CONSTRAINT `fk_ubicacicion_barrios` FOREIGN KEY (`id_barrio`) REFERENCES `barrios` (`id`),\n  CONSTRAINT `fk_ubicacicion_distritos` FOREIGN KEY (`id_distrito`) REFERENCES `distritos` (`id`),\n  CONSTRAINT `fk_ubicacicion_municipios` FOREIGN KEY (`id_municipio`) REFERENCES `municipios` (`id`),\n  CONSTRAINT `fk_ubicacicion_provincias` FOREIGN KEY (`id_provincia`) REFERENCES `provincias` (`id`),\n  CONSTRAINT `fk_ubicacicion_region` FOREIGN KEY (`id_region`) REFERENCES `regiones` (`id`),\n  CONSTRAINT `fk_ubicacicion_secciones` FOREIGN KEY (`id_seccion`) REFERENCES `secciones` (`id`),\n  CONSTRAINT `fk_ubicacicion_sub_barrios` FOREIGN KEY (`id_sub_barrio`) REFERENCES `sub_barrios` (`id`)\n) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci


-- institucion_medica
institucion_medica	CREATE TABLE `institucion_medica` (\n  `rnc_institucion` varchar(9) NOT NULL,\n  `id_ubicacion` binary(16) DEFAULT NULL,\n  `nombre` varchar(50) DEFAULT NULL,\n  `contacto_emergencia` varchar(20) DEFAULT NULL,\n  `fecha_registro` datetime DEFAULT NULL,\n  PRIMARY KEY (`rnc_institucion`),\n  KEY `id_ubicacion` (`id_ubicacion`),\n  CONSTRAINT `institucion_medica_ibfk_1` FOREIGN KEY (`id_ubicacion`) REFERENCES `ubicacion` (`id_ubicacion`)\n) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci


-- personal_medico
personal_medico	CREATE TABLE `personal_medico` (\n  `document_id_personal` varchar(20) NOT NULL,\n  `rnc_institucion` char(9) DEFAULT NULL,\n  `nombre` varchar(50) DEFAULT NULL,\n  `sexo` enum('M','F') DEFAULT NULL,\n  `fecha_nacimiento` date DEFAULT NULL,\n  `id_ubicacion` binary(16) DEFAULT NULL,\n  `cargo` enum('ADMIN_INSTITUCIONAL','ADMIN_PERSONAL_MEDICO','BIOANALISTA_PRINCIPAL','ASISTENTE_BIONALISTA','RECEPSIONISTA','GESTOR_INVENTARIO') DEFAULT NULL,\n  `departamento` enum('UNIDAD_EXTRACCION','RECEPSIONISTA') DEFAULT NULL,\n  `rol` enum('ADMIN_INSTITUCIONAL','ADMIN_PERSONAL_MEDICO','BIOANALISTA','GESTOR_DE_INVENTARIO','RECEPCIONISTA') DEFAULT NULL,\n  `codigo_carnet` varchar(13) DEFAULT NULL,\n  `jornada` enum('MATUTINA','NOCTURNA') DEFAULT NULL,\n  `telefono` varchar(15) DEFAULT NULL,\n  `email` varchar(100) DEFAULT NULL,\n  `credencial_encriptada` varchar(60) DEFAULT NULL,\n  `fecha_registro` datetime DEFAULT NULL,\n  `ultimo_acceso` datetime DEFAULT NULL,\n  `estado` enum('ACTIVO','VACACIONES','DESPEDIDO') DEFAULT NULL,\n  PRIMARY KEY (`document_id_personal`),\n  KEY `rnc_institucion` (`rnc_institucion`),\n  KEY `id_ubicacion` (`id_ubicacion`),\n  CONSTRAINT `personal_medico_ibfk_1` FOREIGN KEY (`rnc_institucion`) REFERENCES `institucion_medica` (`rnc_institucion`),\n  CONSTRAINT `personal_medico_ibfk_2` FOREIGN KEY (`id_ubicacion`) REFERENCES `ubicacion` (`id_ubicacion`)\n) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci


-- donante
donante	CREATE TABLE `donante` (\n  `document_id_donante` varchar(20) NOT NULL,\n  `nombre` varchar(50) DEFAULT NULL,\n  `sexo` enum('M','F') DEFAULT NULL,\n  `fecha_nacimiento` date DEFAULT NULL,\n  `id_ubicacion` binary(16) DEFAULT NULL,\n  `telefono` varchar(15) DEFAULT NULL,\n  `email` varchar(100) DEFAULT NULL,\n  `fecha_registro` datetime DEFAULT NULL,\n  `tipo_sangre` enum('A','B','AB','O') DEFAULT NULL,\n  `factor_rh` enum('POSITIVO','NEGATIVO') DEFAULT NULL,\n  `ultima_donacion` datetime DEFAULT NULL,\n  `estado_disponibilidad` enum('ACTIVO','DIFERIDO','EXCLUIDO') DEFAULT NULL,\n  `autorizacion_contacto` tinyint(1) DEFAULT NULL,\n  PRIMARY KEY (`document_id_donante`),\n  KEY `id_ubicacion` (`id_ubicacion`),\n  CONSTRAINT `donante_ibfk_1` FOREIGN KEY (`id_ubicacion`) REFERENCES `ubicacion` (`id_ubicacion`)\n) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci


-- donacion
donacion	CREATE TABLE `donacion` (\n  `id_donacion` binary(16) NOT NULL,\n  `document_id_donante` varchar(20) DEFAULT NULL,\n  `document_id_personal` varchar(20) DEFAULT NULL,\n  `rnc_institucion` varchar(9) DEFAULT NULL,\n  `codigo_referencia` char(17) DEFAULT NULL,\n  `fecha_extraccion` datetime DEFAULT NULL,\n  `tipo_donacion` enum('SANGRE_TOTAL','AFERESIS') DEFAULT NULL,\n  `tipo_componente` enum('GLOBULOS_ROJOS','PLAQUETAS','PLASMA') DEFAULT NULL,\n  `cantidad_pinta` decimal(4,2) DEFAULT NULL,\n  PRIMARY KEY (`id_donacion`),\n  KEY `document_id_donante` (`document_id_donante`),\n  KEY `document_id_personal` (`document_id_personal`),\n  KEY `rnc_institucion` (`rnc_institucion`),\n  CONSTRAINT `donacion_ibfk_1` FOREIGN KEY (`document_id_donante`) REFERENCES `donante` (`document_id_donante`),\n  CONSTRAINT `donacion_ibfk_2` FOREIGN KEY (`document_id_personal`) REFERENCES `personal_medico` (`document_id_personal`),\n  CONSTRAINT `donacion_ibfk_3` FOREIGN KEY (`rnc_institucion`) REFERENCES `institucion_medica` (`rnc_institucion`)\n) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci


-- datos_triaje
datos_triaje	CREATE TABLE `datos_triaje` (\n  `id_triaje` binary(16) NOT NULL,\n  `id_donacion` binary(16) DEFAULT NULL,\n  `codigo_referencia` char(17) DEFAULT NULL,\n  `peso` decimal(4,2) DEFAULT NULL,\n  `presion_sistolica` int NOT NULL,\n  `presion_diastolica` int NOT NULL,\n  `pulso` int NOT NULL,\n  `temperatura_celsius` decimal(4,2) DEFAULT NULL,\n  `hemoglobina` decimal(4,2) DEFAULT NULL,\n  PRIMARY KEY (`id_triaje`),\n  KEY `id_donacion` (`id_donacion`),\n  CONSTRAINT `datos_triaje_ibfk_1` FOREIGN KEY (`id_donacion`) REFERENCES `donacion` (`id_donacion`),\n  CONSTRAINT `datos_triaje_chk_1` CHECK ((`peso` >= 110)),\n  CONSTRAINT `datos_triaje_chk_2` CHECK ((`presion_sistolica` between 90 and 180)),\n  CONSTRAINT `datos_triaje_chk_3` CHECK ((`presion_diastolica` between 50 and 100)),\n  CONSTRAINT `datos_triaje_chk_4` CHECK ((`pulso` between 50 and 100)),\n  CONSTRAINT `datos_triaje_chk_5` CHECK ((`temperatura_celsius` between 36.0 and 37.2)),\n  CONSTRAINT `datos_triaje_chk_6` CHECK ((`hemoglobina` between 12.5 and 20.0))\n) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci


-- analisis_lab
analisis_lab	CREATE TABLE `analisis_lab` (\n  `id_analisis` binary(16) NOT NULL,\n  `id_donacion` binary(16) DEFAULT NULL,\n  `codigo_referencia` char(17) DEFAULT NULL,\n  `vih` tinyint(1) DEFAULT NULL,\n  `hepatitis_b` tinyint(1) DEFAULT NULL,\n  `hepatitis_c` tinyint(1) DEFAULT NULL,\n  `sifilis` tinyint(1) DEFAULT NULL,\n  `chacas` tinyint(1) DEFAULT NULL,\n  `nat` tinyint(1) DEFAULT NULL,\n  `otros` varchar(100) DEFAULT NULL,\n  PRIMARY KEY (`id_analisis`),\n  KEY `id_donacion` (`id_donacion`),\n  CONSTRAINT `analisis_lab_ibfk_1` FOREIGN KEY (`id_donacion`) REFERENCES `donacion` (`id_donacion`)\n) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci


-- unidad_hemoderivados
unidad_hemoderivados	CREATE TABLE `unidad_hemoderivados` (\n  `id_hemoderivada` binary(16) NOT NULL,\n  `codigo_barras` varchar(100) DEFAULT NULL,\n  `id_donacion` binary(16) DEFAULT NULL,\n  `rnc_institucion` varchar(9) DEFAULT NULL,\n  `codigo_referencia` char(17) DEFAULT NULL,\n  `tipo_sangre` enum('A','B','AB','O') DEFAULT NULL,\n  `factor_rh` enum('POSITIVO','NEGATIVO') DEFAULT NULL,\n  `tipo_componente` enum('GLOBULOS ROJOS','PLAQUETAS','PLASMA') DEFAULT NULL,\n  `estado_unidad` enum('DISPONIBLE','RESERVADA','DESCARTADA','ESPERA','USADA') DEFAULT NULL,\n  `fecha_caducidad` date DEFAULT NULL,\n  `total_sangre` decimal(4,2) DEFAULT NULL,\n  PRIMARY KEY (`id_hemoderivada`),\n  UNIQUE KEY `codigo_barras` (`codigo_barras`),\n  KEY `id_donacion` (`id_donacion`),\n  KEY `rnc_institucion` (`rnc_institucion`),\n  CONSTRAINT `unidad_hemoderivados_ibfk_1` FOREIGN KEY (`id_donacion`) REFERENCES `donacion` (`id_donacion`),\n  CONSTRAINT `unidad_hemoderivados_ibfk_2` FOREIGN KEY (`rnc_institucion`) REFERENCES `institucion_medica` (`rnc_institucion`)\n) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci


-- receptor
receptor	CREATE TABLE `receptor` (\n  `document_id_receptor` varchar(20) NOT NULL,\n  `nombre` varchar(50) DEFAULT NULL,\n  `sexo` enum('M','F') DEFAULT NULL,\n  `fecha_nacimiento` date DEFAULT NULL,\n  `peso` decimal(4,2) DEFAULT NULL,\n  `telefono` varchar(15) DEFAULT NULL,\n  `fecha_registro` datetime DEFAULT NULL,\n  `tipo_sangre` enum('A','B','AB','O') DEFAULT NULL,\n  `factor_rh` enum('POSITIVO','NEGATIVO') DEFAULT NULL,\n  PRIMARY KEY (`document_id_receptor`)\n) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci


-- solicitud_transfusion
solicitud_transfusion	CREATE TABLE `solicitud_transfusion` (\n  `id_solicitud` binary(16) NOT NULL,\n  `document_id_receptor` varchar(20) DEFAULT NULL,\n  `rnc_institucion` varchar(9) DEFAULT NULL,\n  `codigo_referencia` char(17) DEFAULT NULL,\n  `tipo_componente` enum('GLOBULOS ROJOS','PLAQUETAS','PLASMA') DEFAULT NULL,\n  `fecha_solicitud` datetime DEFAULT NULL,\n  `fecha_entrega` datetime DEFAULT NULL,\n  `estado_solicitud` enum('PENDIENTE','EN CAMINO','ENTREGADA') DEFAULT NULL,\n  `cantidad_solicitada` decimal(4,2) DEFAULT NULL,\n  `prioridad` enum('EMERGENCIA','URGENTE','RUTINA') DEFAULT NULL,\n  PRIMARY KEY (`id_solicitud`),\n  UNIQUE KEY `unq_solicitud_componente` (`id_solicitud`,`tipo_componente`),\n  KEY `document_id_receptor` (`document_id_receptor`),\n  KEY `rnc_institucion` (`rnc_institucion`),\n  CONSTRAINT `solicitud_transfusion_ibfk_1` FOREIGN KEY (`document_id_receptor`) REFERENCES `receptor` (`document_id_receptor`),\n  CONSTRAINT `solicitud_transfusion_ibfk_2` FOREIGN KEY (`rnc_institucion`) REFERENCES `institucion_medica` (`rnc_institucion`)\n) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci


-- transfusion
transfusion	CREATE TABLE `transfusion` (\n  `id_transfusion` binary(16) NOT NULL,\n  `id_solicitud` binary(16) DEFAULT NULL,\n  `rnc_institucion` varchar(9) DEFAULT NULL,\n  `codigo_referencia` char(17) DEFAULT NULL,\n  `codigo_barras` varchar(36) DEFAULT NULL,\n  `tipo_componente` enum('GLOBULOS ROJOS','PLAQUETAS','PLASMA') DEFAULT NULL,\n  `tipo_transfusion` enum('DIRECTA','AUTOLOGA','ALOGENICA') DEFAULT NULL,\n  `fecha_transfusion` datetime DEFAULT NULL,\n  `cantidad_Sumistrada` decimal(4,2) DEFAULT NULL,\n  PRIMARY KEY (`id_transfusion`),\n  KEY `rnc_institucion` (`rnc_institucion`),\n  KEY `codigo_barras` (`codigo_barras`),\n  KEY `id_solicitud` (`id_solicitud`,`tipo_componente`),\n  CONSTRAINT `transfusion_ibfk_1` FOREIGN KEY (`rnc_institucion`) REFERENCES `institucion_medica` (`rnc_institucion`),\n  CONSTRAINT `transfusion_ibfk_2` FOREIGN KEY (`codigo_barras`) REFERENCES `unidad_hemoderivados` (`codigo_barras`),\n  CONSTRAINT `transfusion_ibfk_3` FOREIGN KEY (`id_solicitud`, `tipo_componente`) REFERENCES `solicitud_transfusion` (`id_solicitud`, `tipo_componente`)\n) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci


-- v_ubicacion_completa
v_ubicacion_completa	CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `v_ubicacion_completa` AS select `u`.`id_ubicacion` AS `id_ubicacion`,bin_to_uuid(`u`.`id_ubicacion`,1) AS `id_ubicacion_uuid`,concat(`r`.`nombre`,', ',`p`.`nombre`,', ',`m`.`nombre`,', ',coalesce(`d`.`nombre`,'N/A'),', ',coalesce(`s`.`nombre`,'N/A'),', ',coalesce(`b`.`nombre`,'N/A')) AS `direccion_completa`,`u`.`direccion` AS `detalle_direccion` from ((((((`ubicacion` `u` join `regiones` `r` on((`u`.`id_region` = `r`.`id`))) join `provincias` `p` on((`u`.`id_provincia` = `p`.`id`))) join `municipios` `m` on((`u`.`id_municipio` = `m`.`id`))) left join `distritos` `d` on((`u`.`id_distrito` = `d`.`id`))) left join `secciones` `s` on((`u`.`id_seccion` = `s`.`id`))) left join `barrios` `b` on((`u`.`id_barrio` = `b`.`id`)))	utf8mb4	utf8mb4_0900_ai_ci


-- regiones
regiones	CREATE TABLE `regiones` (\n  `id` int NOT NULL,\n  `nombre` varchar(50) NOT NULL,\n  PRIMARY KEY (`id`)\n) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci


-- provincias
provincias	CREATE TABLE `provincias` (\n  `id` int NOT NULL,\n  `nombre` varchar(150) NOT NULL,\n  `regionId` int NOT NULL,\n  PRIMARY KEY (`id`),\n  KEY `fk_provincias_regiones` (`regionId`),\n  KEY `ix_provincias_id` (`id`),\n  CONSTRAINT `fk_provincias_regiones` FOREIGN KEY (`regionId`) REFERENCES `regiones` (`id`)\n) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci


-- municipios
municipios	CREATE TABLE `municipios` (\n  `id` int NOT NULL,\n  `provinciaId` int DEFAULT NULL,\n  `nombre` varchar(150) NOT NULL,\n  PRIMARY KEY (`id`),\n  KEY `fk_municipios_provincias` (`provinciaId`),\n  CONSTRAINT `fk_municipios_provincias` FOREIGN KEY (`provinciaId`) REFERENCES `provincias` (`id`)\n) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci


-- distritos
distritos	CREATE TABLE `distritos` (\n  `id` int NOT NULL,\n  `nombre` varchar(150) DEFAULT NULL,\n  `municipioId` int DEFAULT NULL,\n  PRIMARY KEY (`id`),\n  KEY `fk_distritos_municipios` (`municipioId`),\n  CONSTRAINT `fk_distritos_municipios` FOREIGN KEY (`municipioId`) REFERENCES `municipios` (`id`)\n) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci


-- secciones
secciones	CREATE TABLE `secciones` (\n  `id` int NOT NULL,\n  `nombre` varchar(150) DEFAULT NULL,\n  `municipioId` int DEFAULT NULL,\n  `distritoId` int DEFAULT NULL,\n  PRIMARY KEY (`id`),\n  KEY `fk_secciones_distritos` (`distritoId`),\n  KEY `fk_secciones_municipios` (`municipioId`),\n  CONSTRAINT `fk_secciones_distritos` FOREIGN KEY (`distritoId`) REFERENCES `distritos` (`id`),\n  CONSTRAINT `fk_secciones_municipios` FOREIGN KEY (`municipioId`) REFERENCES `municipios` (`id`)\n) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci


-- barrios
barrios	CREATE TABLE `barrios` (\n  `id` int NOT NULL,\n  `nombre` varchar(150) DEFAULT NULL,\n  `seccionId` int DEFAULT NULL,\n  PRIMARY KEY (`id`),\n  KEY `fk_barrios_secciones` (`seccionId`),\n  CONSTRAINT `fk_barrios_secciones` FOREIGN KEY (`seccionId`) REFERENCES `secciones` (`id`)\n) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci


-- sub_barrios
sub_barrios	CREATE TABLE `sub_barrios` (\n  `id` int NOT NULL,\n  `nombre` varchar(150) DEFAULT NULL,\n  `barrioId` int DEFAULT NULL,\n  PRIMARY KEY (`id`),\n  KEY `fk_sub_barrios_barrios` (`barrioId`),\n  CONSTRAINT `fk_sub_barrios_barrios` FOREIGN KEY (`barrioId`) REFERENCES `barrios` (`id`)\n) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci


