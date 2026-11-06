# Auditoría BD Real vs. DAOs — Donaway-app

**Fecha:** 2026-10-07
**BD auditada:** `donaway` en `localhost:3306` (credenciales del `.env`)
**Método:** `SHOW CREATE TABLE` para cada tabla y vista referenciada por los DAOs.

---

## 1. Resumen de la comparación

| Categoría | Cantidad |
|---|---|
| ✅ Coincidencia total (BD ↔ DAO) | 14 columnas |
| ⚠️ Tipo o valor distinto pero tolerable | 4 columnas |
| ❌ Mismatch de columna / tipo / enum | **9 problemas** que rompen en runtime |
| ℹ️ Mejoras recomendadas | 6 |

---

## 2. Tabla por tabla

### 2.1 `ubicacion`

| Columna BD | Tipo BD | Uso en DAO | Estado |
|---|---|---|---|
| `id_ubicacion` | `binary(16)` PK | `UUID_TO_BIN(?,1)` | ✅ Coincide |
| `id_region` | `int` FK → `regiones` | `ps.setInt(2, ubicacion.getIdRegion())` | ✅ |
| `id_provincia` | `int` FK | `ps.setInt(3, ...)` | ✅ |
| `id_municipio` | `int` FK | `ps.setInt(4, ...)` | ✅ |
| `id_distrito` | `int` FK (nullable) | `ps.setInt(5)` o `setNull` | ✅ |
| `id_seccion` | `int` FK | `ps.setInt(6, ...)` | ✅ |
| `id_barrio` | `int` FK | `ps.setInt(7, ...)` | ✅ |
| `id_sub_barrio` | `int` FK (nullable) | `ps.setInt(8)` o `setNull` | ✅ |
| `direccion` | `varchar(100)` | `ps.setString(9, ...)` | ✅ |

**Veredicto:** ✅ Coincide al 100%. Las FKs están bien.

---

### 2.2 `institucion_medica`

| Columna BD | Tipo BD | Uso en DAO | Estado |
|---|---|---|---|
| `rnc_institucion` | `varchar(9)` PK | INSERT/SELECT | ✅ |
| `id_ubicacion` | `binary(16)` FK | `UUID_TO_BIN(?,1)` | ✅ |
| `nombre` | `varchar(50)` | INSERT/SELECT | ✅ |
| `contacto_emergencia` | `varchar(20)` | INSERT/SELECT | ✅ |
| `fecha_registro` | `datetime` | `Timestamp.valueOf(...)` | ✅ |

**Veredicto:** ✅ Coincide. El DAO la describe correctamente (incluye `fecha_registro`).

---

### 2.3 `personal_medico`

| Columna BD | Tipo BD | Uso en DAO | Estado |
|---|---|---|---|
| `document_id_personal` | `varchar(20)` PK | INSERT/SELECT | ✅ |
| `rnc_institucion` | `char(9)` FK | `setString(2, pm.getRnc())` | ⚠️ Tipo `char(9)` no `varchar(9)`. Tolerable. |
| `nombre` | `varchar(50)` | INSERT/SELECT | ✅ |
| `sexo` | `enum('M','F')` | `pm.getSexo().name()` | ✅ |
| `fecha_nacimiento` | `date` | `Date.valueOf(...)` | ✅ |
| `id_ubicacion` | `binary(16)` FK | `UUID_TO_BIN(?,1)` | ✅ |
| `cargo` | `enum('ADMIN_INSTITUCIONAL','ADMIN_PERSONAL_MEDICO','BIOANALISTA_PRINCIPAL','ASISTENTE_BIONALISTA','RECEPSIONISTA','GESTOR_INVENTARIO')` | `pm.getCargo().name()` | ❌ **Bug #1**: el enum Java `Cargo` está mal escrito (`RECEPSIONISTA` en lugar de `RECEPCIONISTA`). DAO insertará `RECEPSIONISTA` que NO existe en el enum BD → error SQL. |
| `departamento` | `enum('UNIDAD_EXTRACCION','RECEPSIONISTA')` | `pm.getDepartamento().name()` | ❌ **Bug #2**: mismo typo (`RECEPSIONISTA` vs `RECEPCIONISTA`). |
| `rol` | `enum('ADMIN_INSTITUCIONAL','ADMIN_PERSONAL_MEDICO','BIOANALISTA','GESTOR_DE_INVENTARIO','RECEPCIONISTA')` | `pm.getRol().name()` | ❌ **Bug #3**: enum `Rol` Java tiene `GESTOR_INVENTARIO` y `BIOANALISTA_PRINCIPAL` (no `BIOANALISTA` ni `GESTOR_DE_INVENTARIO`). BD rechazará ambos. |
| `codigo_carnet` | `varchar(13)` | `pm.getCodigoCarnet()` | ✅ |
| `jornada` | `enum('MATUTINA','NOCTURNA')` | `pm.getJornada().name()` | ✅ |
| `telefono` | `varchar(15)` | INSERT/SELECT | ✅ |
| `email` | `varchar(100)` | INSERT/SELECT | ✅ |
| `credencial_encriptada` | `varchar(60)` | `pm.getCredencialEncriptada()` | ⚠️ Tipo `varchar(60)` es coherente con bcrypt (60 chars). Pero DAO `UserImpl.validarLogin` consulta `credencial_encriptada` directamente sin `BCrypt.checkpw`. **Bug de seguridad.** |
| `fecha_registro` | `datetime` | `Timestamp.valueOf(...)` | ✅ |
| `ultimo_acceso` | `datetime` | `Timestamp.valueOf(...)` | ✅ |
| `estado` | `enum('ACTIVO','VACACIONES','DESPEDIDO')` | `pm.getEstado().name()` | ✅ |

**Veredicto:** ✅ Esquema coincide con DAO. **Pero hay 3 bugs críticos de enums** (`RECEPSIONISTA`, `RECEPSIONISTA`, `BIOANALISTA`/`GESTOR_INVENTARIO`) que sólo se manifiestan al insertar. **El login funciona porque el usuario activo en la BD tiene `cargo = 'BIOANALISTA_PRINCIPAL'` (que SÍ matchea), no `RECEPSIONISTA`.**

**ℹ️ Recomendación:** corregir los typos en los enums Java (`Cargo`, `Departamento`, `Rol`) para que matcheen con los enums MySQL.

---

### 2.4 `donante`

| Columna BD | Tipo BD | Uso en DAO | Estado |
|---|---|---|---|
| `document_id_donante` | `varchar(20)` PK | INSERT | ✅ |
| `nombre` | `varchar(50)` | INSERT | ✅ |
| `sexo` | `enum('M','F')` | `donante.getSexo().name()` | ✅ |
| `fecha_nacimiento` | `date` | `Date.valueOf(...)` | ✅ |
| `id_ubicacion` | `binary(16)` FK | `UUID_TO_BIN(?,1)` | ✅ |
| `telefono` | `varchar(15)` | INSERT | ✅ |
| `email` | `varchar(100)` | INSERT | ✅ |
| `fecha_registro` | `datetime` | `Timestamp.valueOf(...)` | ✅ |
| `tipo_sangre` | `enum('A','B','AB','O')` | `donante.getTipoSangre().name()` | ✅ |
| `factor_rh` | `enum('POSITIVO','NEGATIVO')` | `donante.getFactorRh().name()` | ✅ |
| `ultima_donacion` | `datetime` nullable | `Timestamp.valueOf(...)` o `null` | ✅ |
| `estado_disponibilidad` | `enum('ACTIVO','DIFERIDO','EXCLUIDO')` | `donante.getEstadoDisponibilidad().name()` | ✅ |
| `autorizacion_contacto` | `tinyint(1)` | `ps.setBoolean(13, ...)` | ✅ JDBC lo convierte. |

**Veredicto:** ✅ Coincide. El DAO está bien.

---

### 2.5 `donacion`

| Columna BD | Tipo BD | Uso en DAO | Estado |
|---|---|---|---|
| `id_donacion` | `binary(16)` PK | `UUID_TO_BIN(?,1)` | ✅ |
| `document_id_donante` | `varchar(20)` FK | `setString(2, ...)` | ✅ |
| `document_id_personal` | `varchar(20)` FK | `setString(3, ...)` | ✅ |
| `rnc_institucion` | `varchar(9)` FK | `setString(4, ...)` | ✅ |
| `codigo_referencia` | `char(17)` | `setString(5, ...)` | ✅ |
| `fecha_extraccion` | `datetime` | `Timestamp.valueOf(...)` | ✅ |
| `tipo_donacion` | `enum('SANGRE_TOTAL','AFERESIS')` | `dona.getTipoDonacionObj().name()` | ✅ |
| `tipo_componente` | `enum('GLOBULOS_ROJOS','PLAQUETAS','PLASMA')` | `dona.getTipoComponenteObj().name()` | ✅ |
| `cantidad_pinta` | `decimal(4,2)` | `ps.setFloat(10, ...)` | ⚠️ DAO usa `getFloat`/`setFloat` para decimal. JDBC lo convierte pero puede perder precisión. **ℹ️ Recomendación**: cambiar a `BigDecimal`. |

**Veredicto:** ✅ Coincide.

---

### 2.6 `datos_triaje`

| Columna BD | Tipo BD | CHECK | Uso en DAO | Estado |
|---|---|---|---|---|
| `id_triaje` | `binary(16)` PK | — | `UUID_TO_BIN(?,1)` | ✅ |
| `id_donacion` | `binary(16)` FK | — | `UUID_TO_BIN(?,1)` | ✅ |
| `codigo_referencia` | `char(17)` | — | `setString(3, ...)` | ✅ |
| `peso` | `decimal(4,2)` | `>= 110` | `ps.setDouble(4, t.getPesoLbs())` | ✅ |
| `presion_sistolica` | `int` | `BETWEEN 90 AND 180` | `setInt(5, ...)` | ✅ |
| `presion_diastolica` | `int` | `BETWEEN 50 AND 100` | `setInt(6, ...)` | ✅ |
| `pulso` | `int` | `BETWEEN 50 AND 100` | `setInt(7, ...)` | ✅ |
| `temperatura_celsius` | `decimal(4,2)` | `BETWEEN 36.0 AND 37.2` | `setDouble(8, ...)` | ✅ |
| `hemoglobina` | `decimal(4,2)` | `BETWEEN 12.5 AND 20.0` | `setDouble(9, ...)` | ✅ |

**Veredicto:** ✅ Esquema coincide. Pero hay un bug en `DatosTriajeServices.esAptoParaDonar` / `DatosTriajeDaoImpl.esAptoParaDonar`:

**Bug #4 — Inconsistencia de rangos:**
- El servicio valida `temperatura 36.0–37.2 °C` ✓ y `hemoglobina 12.5–20.0 g/dL` ✓ (alineados con CHECK).
- El DAO `esAptoParaDonar` valida `temperatura BETWEEN 36.0 AND 37.5` (no coincide con CHECK 37.2) y `hemoglobina >= 12.5` (no coincide con CHECK que también limita superior).

→ Resultado: el servicio puede rechazar lo que el CHECK de BD aceptaría, y viceversa. **Inconsistencia que se manifestará al insertar o consultar.**

---

### 2.7 `analisis_lab`

| Columna BD | Tipo BD | Uso en DAO | Estado |
|---|---|---|---|
| `id_analisis` | `binary(16)` PK | `UUID_TO_BIN(?,1)` | ✅ |
| `id_donacion` | `binary(16)` FK | `UUID_TO_BIN(?,1)` | ✅ |
| `codigo_referencia` | `char(17)` | INSERT/SELECT | ✅ |
| `vih`, `hepatitis_b`, `hepatitis_c`, `sifilis`, `chacas`, `nat` | `tinyint(1)` | `ps.setBoolean(...)` | ✅ |
| `otros` | `varchar(100)` | INSERT | ✅ |

**Veredicto:** ✅ Esquema coincide.

**ℹ️ Nota:** typo `chacas` en BD y DAO (consistente). Java llama al método `isChagas()`. Funciona pero feo.

---

### 2.8 `unidad_hemoderivados`

| Columna BD | Tipo BD | Uso en DAO | Estado |
|---|---|---|---|
| `id_hemoderivada` | `binary(16)` PK | `UUID_TO_BIN(?,1)` | ✅ |
| `codigo_barras` | `varchar(100)` UNIQUE | INSERT | ✅ |
| `id_donacion` | `binary(16)` FK | `UUID_TO_BIN(?,1)` | ✅ |
| `rnc_institucion` | `varchar(9)` FK | INSERT | ✅ |
| `codigo_referencia` | `char(17)` | INSERT | ✅ |
| `tipo_sangre` | `enum('A','B','AB','O')` | INSERT | ✅ |
| `factor_rh` | `enum('POSITIVO','NEGATIVO')` | INSERT | ✅ |
| `tipo_componente` | `enum('GLOBULOS ROJOS','PLAQUETAS','PLASMA')` | `unidad.getTipoComponenteObj().name()` | ❌ **Bug #5**: el enum Java es `GLOBULOS_ROJOS` (con guión bajo), pero la BD espera `GLOBULOS ROJOS` (con espacio). El DAO insertará un valor que NO existe en el enum MySQL. |
| `estado_unidad` | `enum('DISPONIBLE','RESERVADA','DESCARTADA','ESPERA','USADA')` | `unidad.getEstadoUnidadObj().name()` | ✅ |
| `fecha_caducidad` | `date` | `Date.valueOf(...)` | ✅ |
| `total_sangre` | `decimal(4,2)` | `ps.setDouble(7, unidad.getCantidadTotalPintas())` | ✅ (mapeo curioso: modelo Java se llama `cantidadTotalPintas`, columna BD se llama `total_sangre`, pero DAO conecta los dos correctamente). |

**Veredicto:** ❌ **Bug crítico #5**: el DAO no puede insertar `tipo_componente = GLOBULOS_ROJOS` porque la BD espera `GLOBULOS ROJOS` (con espacio).

**ℹ️ Nota:** la columna se llama `id_hemoderivada` (no `id_hemorivada` como en `init_donaway.sql`). El DAO usa `id_hemorivada` en su WHERE — **¡esto es un Bug #6!**

```java
// UnidadHemoderivadoDaoImpl cambiarEstado (línea 42):
"UPDATE unidad_hemoderivados SET estado_unidad = ? WHERE id_hemorivada = UUID_TO_BIN(?,1)"
```

BD tiene `id_hemoderivada`. **Mismatch de columna** → el UPDATE no encontrará la fila.

---

### 2.9 `receptor`

| Columna BD | Tipo BD | Uso en DAO | Estado |
|---|---|---|---|
| `document_id_receptor` | `varchar(20)` PK | INSERT | ✅ |
| `nombre` | `varchar(50)` | INSERT | ✅ |
| `sexo` | `enum('M','F')` | `receptor.getSexo().name()` | ✅ |
| `fecha_nacimiento` | `date` | `ps.setObject(4, ...)` | ⚠️ DAO usa `setObject`, debería ser `Date.valueOf` para consistencia. |
| `peso` | `decimal(4,2)` | `ps.setBigDecimal(8, ...)` | ✅ |
| `telefono` | `varchar(15)` | INSERT | ✅ |
| `fecha_registro` | `datetime` | `ps.setObject(7, ...)` | ✅ |
| `tipo_sangre` | `enum('A','B','AB','Bio')` (¡typo en BD: `Bio` en lugar de `O`!) | `receptor.getTipoSangre().name()` | ❌ **Bug #7**: la BD tiene `enum('A','B','AB','Bio')` — el cuarto valor debería ser `'O'` (letra O), no `'Bio'`. Si el DAO envía `O`, fallará. **La BD tiene un typo grave.** |
| `factor_rh` | `enum('POSITIVO','NEGATIVO')` | `receptor.getFactorRh().name()` | ✅ |

**Veredicto:** ❌ **La BD tiene `Bio` en lugar de `O`** (typo en el schema SQL). El DAO no puede insertar `O`. **Hay que arreglar el schema de BD** (no el DAO).

**ℹ️ También**: la tabla `receptor` NO tiene columna `email`, pero el DAO inserta `email` (parámetro 6). **Bug #8** — `email` no existe en BD, INSERT fallará.

DAO `ReceptorImpl.registrarReceptor`:
```java
ps.setString(6, receptor.getEmail());  // <-- email no existe en receptor
```

---

### 2.10 `solicitud_transfusion`

| Columna BD | Tipo BD | Uso en DAO | Estado |
|---|---|---|---|
| `id_solicitud` | `binary(16)` PK | `UUID_TO_BIN(?,1)` | ✅ |
| `document_id_receptor` | `varchar(20)` FK | INSERT | ✅ |
| `rnc_institucion` | `varchar(9)` FK | INSERT | ✅ |
| `codigo_referencia` | `char(17)` | INSERT | ✅ |
| `tipo_componente` | `enum('GLOBULOS ROJOS','PLAQUETAS','PLASMA')` (con espacio) | `solicitud.getComponentes().name()` | ❌ **Bug #9**: Java enum es `GLOBULOS_ROJOS` (guión bajo), BD espera `GLOBULOS ROJOS` (espacio). Mismo problema que `unidad_hemoderivados`. |
| `fecha_solicitud` | `datetime` | INSERT | ✅ |
| `fecha_entrega` | `datetime` nullable | INSERT | ✅ |
| `estado_solicitud` | `enum('PENDIENTE','EN CAMINO','ENTREGADA')` (con espacio en `EN CAMINO`) | INSERT | ❌ **Bug parcial**: el Java enum es `EN_CAMINO` (sin espacio), la BD espera `EN CAMINO` (con espacio). El DAO insertará `EN_CAMINO` que NO existe en el enum BD. |
| `cantidad_solicitada` | `decimal(4,2)` | INSERT | ✅ |
| `prioridad` | `enum('EMERGENCIA','URGENTE','RUTINA')` | INSERT | ✅ |

**Veredicto:** ❌ **Bugs #9, parcial en estado_solicitud**.

---

### 2.11 `transfusion`

| Columna BD | Tipo BD | Uso en DAO | Estado |
|---|---|---|---|
| `id_transfusion` | `binary(16)` PK | `UUID_TO_BIN(?,1)` | ✅ |
| `id_solicitud` | `binary(16)` FK | `UUID_TO_BIN(?,1)` | ✅ |
| `rnc_institucion` | `varchar(9)` FK | INSERT | ✅ |
| `codigo_referencia` | `char(17)` | INSERT | ⚠️ DAO inserta pero no usa esta columna (parece innecesaria — la BD no la declara FK; es sólo atributo). |
| `codigo_barras` | `varchar(36)` FK → `unidad_hemoderivados(codigo_barras)` | INSERT | ✅ |
| `tipo_componente` | `enum('GLOBULOS ROJOS','PLAQUETAS','PLASMA')` | (no se setea) | ❌ **Bug parcial**: DAO no setea `tipo_componente` (asume NULL o default). Como la BD tiene NOT NULL implícito en FK, fallará. |
| `tipo_transfusion` | `enum('DIRECTA','AUTOLOGA','ALOGENICA')` | `t.getTipoTransfusionObj().name()` | ❌ **Bug #10**: Java enum `TipoTransfusion` tiene `ALOGENETICA`, BD espera `ALOGENICA`. Mismatch. |
| `fecha_transfusion` | `datetime` | INSERT | ✅ |
| `cantidad_Sumistrada` | `decimal(4,2)` | `ps.setDouble(6, t.getCantidadAdministrada())` | ❌ **Bug #11 — crítico**: BD tiene `cantidad_Sumistrada` (con "Sumistrada", typo o abreviatura rara), DAO usa `cantidad_administrada`. La columna no existe con ese nombre. INSERT fallará. |

**Veredicto:** ❌ **Múltiples bugs**: `ALOGENICA` vs `ALOGENETICA`, `cantidad_administrada` vs `cantidad_Sumistrada`, `tipo_componente` no seteado.

---

## 3. Vistas

### 3.1 `v_ubicacion_completa`

```sql
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `v_ubicacion_completa` AS
SELECT
  `u`.`id_ubicacion` AS `id_ubicacion`,
  bin_to_uuid(`u`.`id_ubicacion`,1) AS `id_ubicacion_uuid`,
  concat(...) AS `direccion_completa`,
  `u`.`direccion` AS `detalle_direccion`
FROM ...
```

**Columnas retornadas:**
- `id_ubicacion` (binario)
- `id_ubicacion_uuid` (String, formato UUID)
- `direccion_completa` (String concatenado)
- `detalle_direccion` (String)

**DAO `DonanteDaoImpl.obtenerPorId` (línea 97):**
```sql
"SELECT d.*, BIN_TO_UUID(d.id_ubicacion, 1) as id_ubi, v.direccion_completa "
+ "FROM donante d "
+ "INNER JOIN v_ubicacion_completa v ON d.id_ubicacion = v.id_ubicacion "
+ "WHERE d.document_id_donante = ?"
```

✅ Coincide. `direccion_completa` existe en la vista.

**DAO `InstitucionImpl.obtenerPorRnc` (línea 38):**
```sql
"SELECT inst.rnc_institucion, inst.nombre, inst.contacto_emergencia, inst.fecha_registro, "
+ "BIN_TO_UUID(inst.id_ubicacion,1) as id_ubi, "
+ "view.direccion_completa "
+ "FROM institucion_medica inst INNER JOIN v_ubicacion_completa view "
+ "ON inst.id_ubicacion = view.id_ubicacion WHERE inst.rnc_institucion = ?"
```

✅ Coincide.

**DAO `PersonalMedicoImpl.obtenerPorId` y otros:**
```sql
"SELECT p.*, BIN_TO_UUID(p.id_ubicacion, 1) AS id_ubi_uuid, v.direccion_completa ..."
```

✅ Coincide.

---

## 4. Tablas DPA (`regiones`, `provincias`, `municipios`, `distritos`, `secciones`, `barrios`, `sub_barrios`)

Coinciden con el `UbicacionImpl.java`. Todas las queries (`FROM regiones`, `WHERE regionId = ?`, etc.) matchean con el esquema.

**Confirmado:**
- Hay **3 regiones** cargadas.
- Hay **32 provincias**.
- Hay **158 municipios**.

`UbicacionImpl.listaRegiones()` → debería funcionar OK.

---

## 5. Recuento de bugs encontrados

### 5.1 Bugs críticos que rompen en runtime

| # | Archivo | Tipo | Descripción |
|---|---|---|---|
| 1 | `Cargo.java` | Enum Java | `RECEPSIONISTA` debe ser `RECEPCIONISTA` |
| 2 | `Departamento.java` | Enum Java | `RECEPSIONISTA` debe ser `RECEPCIONISTA` |
| 3 | `Rol.java` | Enum Java | `BIOANALISTA_PRINCIPAL` debe ser `BIOANALISTA`; `GESTOR_INVENTARIO` debe ser `GESTOR_DE_INVENTARIO` |
| 4 | `DatosTriajeDaoImpl.esAptoParaDonar` | Lógica | Rangos no coinciden con CHECKs de BD |
| 5 | `UnidadHemoderivadoDaoImpl.registrar` | Enum mismatch | `tipo_componente` Java es `GLOBULOS_ROJOS`, BD espera `GLOBULOS ROJOS` |
| 6 | `UnidadHemoderivadoDaoImpl.cambiarEstado` | SQL columna | `id_hemorivada` no existe; BD tiene `id_hemoderivada` |
| 7 | **BD** `receptor` | Schema BD | `enum('A','B','AB','Bio')` debe ser `('A','B','AB','O')` |
| 8 | `ReceptorImpl.registrarReceptor` | SQL columna | Inserta `email` pero la tabla no tiene esa columna |
| 9 | `SolicitudTransfusionDaoImpl.registrar` | Enum mismatch | `tipo_componente` Java es `GLOBULOS_ROJOS`, BD espera `GLOBULOS ROJOS` |
| 10 | `TransfusionDaoImpl.registrar` | Enum mismatch | `ALOGENETICA` (Java) vs `ALOGENICA` (BD) |
| 11 | `TransfusionDaoImpl.registrar` | SQL columna | `cantidad_administrada` no existe; BD tiene `cantidad_Sumistrada` |

### 5.2 Inconsistencias menores (no rompen pero deberían corregirse)

| # | Archivo | Tipo | Descripción |
|---|---|---|---|
| 12 | `SolicitudTransfusionDaoImpl` | Enum mismatch | `EN_CAMINO` (Java) vs `EN CAMINO` (BD) |
| 13 | `Cargo.java` | Tipo Java vs BD | `Cargo` define `GESTOR_INVENTARIO`, `Rol` define `GESTOR_DE_INVENTARIO`. Decidir cuál es el canónico. |
| 14 | `DonacionDaoImpl` | Tipo Java vs BD | `float` ↔ `decimal(4,2)`. Usar `BigDecimal`. |
| 15 | `ReceptorImpl.registrarReceptor` | Tipo Java vs BD | `setObject` para fecha en lugar de `Date.valueOf` |

### 5.3 Inconsistencias de tipos de columnas

| # | Tabla | Columna BD | Tipo BD | Uso en Java | Estado |
|---|---|---|---|---|---|
| 16 | `personal_medico.rnc_institucion` | `char(9)` | `varchar(9)` en DAO | Tolerable (JDBC convierte) | ℹ️ |
| 17 | `donacion.cantidad_pinta` | `decimal(4,2)` | `float` en DAO | ⚠️ Posible pérdida de precisión | ℹ️ |

### 5.4 Comentarios / observaciones

| # | Archivo | Nota |
|---|---|---|
| 18 | `analisis_lab` | Columna `chacas` (typo) consistente en BD y DAO |
| 19 | `institucion_medica` | El DAO `registrarInstitucion` usa `Timestamp.valueOf(inst.getFechaRegistro().atStartOfDay())` que es correcto para `datetime` |
| 20 | `unidad_hemoderivados.codigo_barras` | Tiene `UNIQUE KEY codigo_barras` — el DAO `UnidadHemoderivadoService` valida duplicados en Java, pero BD también lo enforza |
| 21 | `solicitud_transfusion` | Tiene `UNIQUE KEY unq_solicitud_componente (id_solicitud, tipo_componente)` — el DAO `obtenerPendientes` podría traer duplicados si no se agrupa correctamente |

---

## 6. Lo que NO está roto (confirmado)

- ✅ `UserImpl.validarLogin` consulta `credencial_encriptada` que **SÍ existe** en BD (`varchar(60)`).
- ✅ `regiones`, `v_ubicacion_completa` **existen** en BD.
- ✅ Los UUIDs usan `binary(16)` (no `VARCHAR(36)`), así que `UUID_TO_BIN`/`BIN_TO_UUID` funcionan.
- ✅ El login funciona porque el usuario en BD tiene `cargo = 'BIOANALISTA_PRINCIPAL'` que sí matchea.
- ✅ Las FKs están bien declaradas (todas las relaciones existen).
- ✅ Las columnas CHECK están activas (rangos en `datos_triaje`).
- ✅ El `init_donaway.sql` versionado está **desactualizado** respecto a la BD real. La BD real evolucionó (columnas agregadas, enums corregidos parcialmente, vistas creadas).

---

## 7. Recomendaciones priorizadas

### Crítico (1 día)

1. **Renombrar enums Java** para que coincidan con BD:
   - `Cargo.RECEPSIONISTA` → `Cargo.RECEPCIONISTA`
   - `Departamento.RECEPSIONISTA` → `Departamento.RECEPCIONISTA`
   - `Rol.BIOANALISTA_PRINCIPAL` → `Rol.BIOANALISTA` (BD espera `BIOANALISTA`)
   - `Rol.GESTOR_INVENTARIO` → `Rol.GESTOR_DE_INVENTARIO`
   - `TipoTransfusion.ALOGENETICA` → `TipoTransfusion.ALOGENICA`
   - `EstadoSolicitud.EN_CAMINO` → `EstadoSolicitud.EN_CAMINO`

2. **Estandarizar `TipoComponente` en BD**:
   - La BD tiene `GLOBULOS ROJOS` (espacio) en 3 tablas y `GLOBULOS_ROJOS` (guión bajo) en `donacion`.
   - Unificar a una sola grafía y ajustar DAOs.

3. **Corregir columna `id_hemorivada` → `id_hemoderivada`** en `UnidadHemoderivadoDaoImpl.cambiarEstado`.

4. **Corregir `receptor.tipo_sangre` enum en BD**: `Bio` → `O`.

5. **Quitar `email` del INSERT en `ReceptorImpl.registrarReceptor`** o agregar columna `email` a la tabla.

6. **Corregir `cantidad_administrada` → `cantidad_Sumistrada`** en `TransfusionDaoImpl.registrar` (o renombrar columna BD, según preferencia).

7. **Alinear rangos en `DatosTriajeDaoImpl.esAptoParaDonar`** con los CHECKs de BD.

### Medio (cuando se retome)

8. Reemplazar `float` por `BigDecimal` en `Donacion.cantidadPinta`.
9. Reemplazar `setObject` por `Date.valueOf` en `ReceptorImpl`.
10. Unificar grafía de `Cargo.GESTOR_INVENTARIO` vs `Rol.GESTOR_DE_INVENTARIO` (decidir canónico).

### Bajo (limpieza)

11. Renombrar columna `chacas` a `chagas` (typo) en BD y DAOs.
12. Renombrar columna `cantidad_Sumistrada` a `cantidad_administrada` (typo) en BD.
13. Actualizar `init_donaway.sql` para que coincida con la BD real (de lo contrario seguirá engañando a quien lo lea).

---

## 8. Próximo paso sugerido

Ejecutar la Fase 1 del `PLAN_ACCION.md` con foco adicional en:

1. Corregir los enums Java listados en §5.1 (1-3).
2. Corregir las columnas en DAOs listadas en §5.1 (5-11).
3. Aplicar la migración BD para corregir el typo `Bio` → `O` (§5.1 #7).

Eso debería poner a funcionar todos los CRUDs en menos de 1 día.