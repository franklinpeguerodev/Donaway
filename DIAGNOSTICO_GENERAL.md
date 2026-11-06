# Diagnóstico General del Repositorio `Donaway-app`

**Proyecto:** Sistema médico institucional para la gestión automatizada de donaciones y reservas de sangre (JavaFX + Maven, MySQL).
**Fecha del diagnóstico:** 2026-10-07
**Alcance:** Análisis estático y cruzado del código Java, FXML, SQL, configuración, dependencias y convenciones.

> **Resumen ejecutivo (TL;DR)**
> El login **sí funciona** en el sistema desplegado, la navegación básica entre pantallas funciona y los popups se abren. Lo que **no funciona end-to-end** son los flujos reales: casi todos los formularios no tienen `fx:controller` ni acción de envío, varias columnas/tablas/Vistas SQL usadas por los DAO no existen en `init_donaway.sql`, los IDs tratados como `BINARY(16)` chocan con columnas declaradas `VARCHAR(36)`, hay constantes con grafía inconsistente (`RECEPSIONISTA` vs `RECEPCIONISTA`), varias rutas FXML referenciadas no existen, los popups cargan datos hardcoded en lugar de datos reales, y los servicios de muchas entidades (`InstitucionServices`, `ReceptorImpl`, parte de `SolicitudTransfusionService`…) están vacíos. Esto es un proyecto en fase muy temprana con piezas inconexas; antes de continuar hay que fijar un modelo de datos y un contrato FXML↔Controller estables.

---

## 1. Arquitectura general (cómo está organizado el proyecto)

```
src/main/java/com/donaway
├── main/Main.java                       (clase Application, entry-point)
├── controller/
│   ├── controllerpersonalmedico/        (ControllerPrincipal, GraficasController, ControladorCentroNotificaciones, NotificacionObjeto, ModuloFormularios, VistasEmergentes)
│   ├── controllerpersonal_inventario/   (ControllerInventario, ControllerInventarioTabla, InventarioUnidad, RegistroPacientes)
│   ├── controllersecretaria/            (ControllerSecretaria, ControllerAuxiliar vacío)
│   ├── persona/                         (DonanteController)
│   └── user/                            (UserLoginController, ProbandoDaoController)
├── dao/                                 (interfaces + impl/, MySQL con UUID_TO_BIN / BIN_TO_UUID)
├── dto/login/                           (LoginRequestDto, UserSessionDto, SessionManager)
├── exception/                           (Alerta, ValidacionException)
├── service/                             (servicios muy delgados: validaciones + try/catch + bridge al DAO)
├── model/                               (entidades + enums en utils/constants)
└── utils/
    ├── db/ConexionDbDonaway.java        (HikariCP + Dotenv)
    ├── generador/                       (IdGenerador, CarnetGenerador)
    ├── constants/                       (Rol, Cargo, Departamento, Jornada, EstadoPersonal, EstadoDisponibilidad, EstadoUnidad, EstadoSolicitud, PrioridadSolicitud, TipoComponente, TipoDonancion, TipoSangre, TipoTransfusion, Sexo, FactorRh)
    ├── security/SeguridadEncrip.java    (BCrypt – pero NO se usa para verificar)
    ├── mensaje/Mensaje.java             (constantes de error)
    └── time/Time.java
```

Observaciones:

- **No hay DI / Inversión de Control**: cada `Service` hace `new Impl()` dentro de su propio archivo (acoplamiento fuerte, imposible de testear/mockear).
- **No hay fachada**: los Controllers llaman directamente a Services/DAOs saltándose capas.
- **No hay un módulo de seguridad real**: el "login" no encripta la verificación, no hay hashing real en uso.
- **No hay tests** (ni `src/test/java`).
- **No hay logging**: todo se hace con `System.out.println` y `printStackTrace`.
- **No hay validación de tipos de sangre/Compatibilidad Rh**: la lógica médica crítica (compatibilidad donante↔receptor) no existe.

---

## 2. Configuración y dependencias (`pom.xml` / `.env*`)

### 2.1 `pom.xml`

| Hallazgo | Severidad |
|----------|-----------|
| **Dependencia `jbcrypt` duplicada** (líneas 27-31 y 63-67). Maven no falla pero es ruido. | Media |
| Falta `org.openjfx:javafx-base` y `javafx-graphics`: Maven incluye sólo `controls` y `fxml`. En empaquetado modular moderno esto puede dar `ClassNotFoundException` al arrancar. | Alta |
| El plugin `javafx-maven-plugin` apunta a `com.donaway.main.Main` (que sí existe). | OK |
| `maven.compiler.release` = 21 → Java 21 (compatible con `javafx 21`). | OK |
| **No hay plugin `surefire`** → no corren tests (no hay tests tampoco). | OK (consistente) |
| Falta plugin para `jpackage` o generación de instalador. | OK (sólo desarrollo) |
| `controlsfx` 11.1.2 está OK, pero la documentación oficial recomienda alinear la versión con la de JavaFX. | Baja |
| No hay configuración de <resources> para `src/main/resources`, Maven lo añade por defecto. | OK |

### 2.2 `.env` (raíz) y `.env.example`

- `.env` contiene credenciales **hardcodeadas** (`root`/`admin`) y sube a git (aunque `.gitignore` lo excluye, el archivo ya está en el repo). El log/commits podrían exponerlas.
- `.env.example` describe variables para **H2** (`org.h2.Driver`, `jdbc:h2:...`) mientras que la aplicación real **usa MySQL** (`mysql-connector-j` en pom). Quien copie `.env.example` y lo use como `.env` **no conseguirá conectar**.
- Las credenciales de la nube (`DB_NUBE_DONAWAY_*`) están vacías.
- `DB_LOCAL_DONAWAY_URL` no usa SSL y zona horaria UTC. OK para local.
- `.env` está en `.gitignore` ✅ pero el archivo ya fue versionado (no se quita con sólo añadirlo a gitignore).

### 2.3 `ConexionDbDonaway.java`

- Inicializa `HikariDataSource` en un bloque estático. Una falla de conexión **bloquea el arranque** (no se hace lazy init). No hay manejo de reconexión.
- Carga credenciales vía `Dotenv.load()` pero **no lee `DB_NUBE_DONAWAY_*`** ni tiene mecanismo para alternar local ↔ nube.
- `setDriverClassName(driver)` está deprecado en HikariCP ≥ 4 (recomiendan usar `setDataSourceClassName`).
- No se valida que `url`, `user`, `password` no sean `null` antes de configurar HikariCP.

---

## 3. Esquema SQL (`init_donaway.sql`, `init_data_dpa.sql`)

> **Ésta es la causa #1 de que la aplicación no funcione. Hay decenas de columnas referenciadas en Java que no existen en SQL.**

### 3.1 Tablas declaradas vs columnas esperadas

| Tabla | Faltantes críticas |
|---|---|
| `ubicacion` | Columnas: `region, provincia, municipio, distrito, seccion, barrio, sub_barrio` son tipos planos (VARCHAR/INT), **no** foreign keys a las tablas DPA (`regiones`, `provincias`...). El DAO inserta region/provincia/municipio como `int` pero la tabla usa `region VARCHAR(20)`. **No hay tabla `regiones`**. |
| `institucion_medica` | **Falta `fecha_registro`**, `InstitucionImpl.registrarInstitucion` lo inserta → `Unknown column` en runtime. |
| `personal_medico` | Faltan: `cargo`, `departamento`, `rol`, `codigo_carnet`, `jornada`, `estado`, `sexo`, `credencial_encriptada` (la columna real es `credenciales_encriptadas` con typo + "s" final). |
| `donante` | Faltan: `ultima_donacion` no es parte de la clave. Faltan también los `CHECK`/índices que el servicio espera. La columna `ultima_donacion` está como `DATETIME` pero el DAO le pasa `Timestamp.valueOf(localDateTime)` – OK. |
| `donacion` | **Falta `codigo_referencia`** (DAO y modelo lo usan). Faltan FKs claras hacia donante/personal/institución con los mismos nombres que el modelo espera. |
| `datos_triaje` | **Typo en `presion_distolica`** (debería ser `presion_diastolica`). DAO hace `getDouble("peso")` pero columna es `peso DECIMAL(5,2)` – funciona. |
| `analisis_lab` | Falta `codigo_referencia` (DAO lo usa). Falta `id_triaje` PK. |
| `unidad_hemoderivados` | **Faltan columnas críticas**: `total_sangre`, `tipo_sangre`, `factor_rh`, `codigo_referencia`, `cantidad_pintas`. |
| `receptor` | Falta `email`. La columna es `fecha_registro` pero `ReceptorImpl` inserta `fechaRegistro` (camelCase) → `Unknown column 'fechaRegistro'`. |
| `solicitud_transfusion` | Faltan `tipo_componente`, `cantidad_solicitada`, `codigo_referencia`. |
| `transfusion` | Falta `cantidad_administrada`. Falta FK `codigo_barras REFERENCES unidad_hemoderivados(codigo_barras)` (declarada pero el script no la incluye). |

### 3.2 Tipos de columna incompatibles con el DAO

- Todos los IDs (`id_donacion`, `id_triaje`, `id_analisis`, `id_hemorivada`, `id_solicitud`, `id_transfusion`, `id_ubicacion`) son declarados **`VARCHAR(36)`**, pero el DAO usa **`UUID_TO_BIN(?,1)`** y **`BIN_TO_UUID(...,1)`** que requieren `BINARY(16)` / `VARBINARY(16)`. Resultado: error en cada INSERT/SELECT. (Esto afecta prácticamente **todas** las queries.)
- `ubicacion.id_*` deberían ser FKs `INT` a `regiones/provincias/municipios/distritos/secciones/barrios/sub_barrios`. Están como INT, **pero el DAO inserta valores vía `UUID_TO_BIN(?,1)`** (en `DonanteDaoImpl.registrarDonante`).

### 3.3 Datos DPA (`init_data_dpa.sql`)

- **No existe ni la tabla ni los datos de `regiones`**, pero `UbicacionImpl.listaRegiones()` hace `SELECT * FROM regiones` → fallará siempre.
- `init_data_dpa.sql` empieza con `SET FOREIGN_KEY_CHECKS = 0;` pero **nunca los reactiva** (`SET FOREIGN_KEY_CHECKS = 1;`).
- Hace `INSERT` directos (formato Adminer) en vez de sentencias parametrizadas — OK para init, pero incluye dumps grandes (más de 20 000 líneas) que no se versionan limpiamente.
- **No hay `regiones` poblado** pero la consulta de dropdown del combo de regiones depende de eso.

### 3.4 Vistas / procedimientos

- **Vista `v_ubicacion_completa` referenciada en 3 DAOs** (`DonanteDaoImpl`, `PersonalMedicoImpl`, `InstitucionImpl`) **no está definida en el SQL**.

---

## 4. DAOs (`dao/`) — análisis cruzado

### 4.1 `IDonanteDao` / `DonanteDaoImpl`

- `registrarDonante` y `modificarDonante` insertan/actualizan con `UUID_TO_BIN(?,1)` sobre columna `VARCHAR(36)` → **fallo de tipo**.
- `obtenerPorId`, `obtenerTodo`, `obtenerPorEstado` hacen `INNER JOIN v_ubicacion_completa` → **vista inexistente → error SQL**.
- El mapeo `mapearDonante` setea `dirreccionCompleta` (typo: "dirreción" con doble r). El getter también está mal escrito. Inconsistencia ortográfica.
- `ejecutarConsultaLista(sql, null, false)` recibe `tieneParametro=false` para `obtenerTodo` y `true` para `obtenerPorEstado`, pero los constructores pasan `null` también a `obtenerPorEstado`. Funcional pero confuso.

### 4.2 `IReceptorDao` / `ReceptorImpl`

- `registrarReceptor` inserta columnas con nombres incorrectos:
  - `fechaRegistro` (camelCase) en lugar de `fecha_registro` (snake_case) → **SQL falla**.
  - Inserta `email` que **no existe** en la tabla `receptor`.
- `modificarReceptor`, `obtenerTodo`, `buscarPorNombre`, `obtenerPorId` están vacíos (devuelven `null` o nada).
- El interface declara `obtenerTodo` y `buscarPorNombre` con retorno `List<Receptor>`, no se cumplen.

### 4.3 `IDonacionDao` / `DonacionDaoImpl`

- `registrar` referencia `codigo_referencia` (columna inexistente) → SQL falla.
- `obtenerPorId`, `obtenerPorCodigoReferencia` usan `UUID_TO_BIN/BIN_TO_UUID` → falla de tipo.
- `mapearDonacion` ignora el `cedulaReceptor` (siempre `null`) — el modelo lo espera pero no se usa en BD.
- No hay método para "actualizar" donación; el DAO es de sólo escritura + lectura.

### 4.4 `IDatosTriajeDao` / `DatosTriajeDaoImpl`

- `registrar` usa `presion_diastolica` (correcto), pero SQL tiene `presion_distolica` (typo) → SQL falla.
- `esAptoParaDonar` valida rangos distintos a los del servicio:
  - Servicio valida `peso >= 110 lbs`, temperatura 36.0–37.2, hemoglobina 12.5–20.0.
  - DAO valida `sistolica 90-180, diastolica 50-100, pulso 50-100, temperatura 36-37.5, hemoglobina >= 12.5`.
  - Las validaciones **no coinciden** (rangos diferentes), lo que dará rechazos arbitrarios.

### 4.5 `IAnalisisLabDao` / `AnalisisLabDaoImpl`

- `esReactivo` consulta `chacas = 1` (typo). La columna SQL es `chacas` también (typo consistente). El modelo la llama `isChagas()`. Funciona por coincidencia.
- `registrar` falta columna `codigo_referencia` en SQL (no existe en tabla).
- Todos los métodos que usan UUID fallan por tipo de columna.

### 4.6 `IInstitucionDao` / `InstitucionImpl`

- `registrarInstitucion` inserta `fecha_registro` que **no existe** en la tabla.
- `obtenerPorRnc` y `obtenerInstituciones` hacen JOIN a `v_ubicacion_completa` → **vista inexistente**.
- `modificarInstitucion` no actualiza `id_ubicacion` (sólo nombre y contacto).
- No hay `eliminarInstitucion` aunque es esperable en CRUD.

### 4.7 `IUnidadHemoderivadoDao` / `UnidadHemoderivadoDaoImpl`

- `registrar` envía `total_sangre`, `tipo_sangre`, `factor_rh`, `codigo_referencia` que **no existen** en la tabla `unidad_hemoderivados`.
- `cambiarEstado` funciona si el ID existe.
- `obtenerUnidadesPorVencer` usa `DATE_ADD(CURRENT_DATE, INTERVAL ? DAY)` con sintaxis MySQL pura – incompatible con H2 si se decidiera cambiar.

### 4.8 `ISolicitudTransfusionDao` / `SolicitudTransfusionDaoImpl`

- `registrar` inserta `codigo_referencia`, `cantidad_solicitada`, `tipo_componente` que **no existen** en SQL.
- `obtenerPendientes` filtra por `prioridad` pero el estado se compara con `'PENDIENTE'` hardcoded — el modelo lo trae del enum (`PENDIENTE` OK).
- No hay método para eliminar ni cancelar explícitamente.

### 4.9 `ITransfusionDao` / `TransfusionDaoImpl`

- `registrar` ejecuta 3 queries en transacción manual. Marca la solicitud como `'COMPLETADA'` pero el enum `EstadoSolicitud` no incluye `COMPLETADA` (sólo `PENDIENTE`, `EN_CAMINO`, `ENTREGADA`). Esto **falla por enum**, no porque la columna exista.
- `obtenerPorUnidad` y `obtenerPorCodigoBarras` son alias — duplicación sin valor.
- El modelo `Transfusion` recibe `cantidad_administrada` pero la columna SQL no existe.

### 4.10 `IUbicacionDao` / `UbicacionImpl`

- `listaRegiones` consulta `FROM regiones` → **tabla inexistente**.
- El `INSERT INTO ubicacion` pasa columnas `id_region, id_provincia, id_municipio, id_distrito, id_seccion, id_barrio, id_sub_barrio` pero la tabla `ubicacion` define `region, provincia, municipio, distrito, seccion, barrio, sub_barrio` (sin prefijo `id_`). **SQL falla**.
- `listaSeccionPorMunicipio` consulta `distritoId IS NULL` (convención camelCase) pero la tabla SQL tiene `distritoId` (sí, está bien en SQL). OK.

### 4.11 `IPersonalMedico` / `PersonalMedicoImpl`

- `registrarPersonalMedico` inserta columnas que **no existen** en SQL (`cargo`, `departamento`, `rol`, `codigo_carnet`, `jornada`, `estado`, `sexo`, `credencial_encriptada`).
- `obtenerPorId`, `obtenerPorCodigoCarnet`, `obtenerInstitucion`, `obtenerPorDepartamento`, `obtenerPorJornada`, `obtenerActivos` hacen JOIN a `v_ubicacion_completa` → **vista inexistente**.
- `validarCredenciales(email, password)` usa la columna `email` (existe) y `credencial_encriptada` (no existe).
- `mapearPersonal` llama constructor con 17 argumentos en orden específico; si la consulta cambia el orden de columnas, el mapeo queda silenciosamente mal. **Frágil**.

### 4.12 `IUserDao` / `UserImpl`

- `validarLogin` consulta `credencial_encriptada` (sin "s") sobre columna `credenciales_encriptadas` (con "s" y typo) **en el `init_donaway.sql`**. **El usuario confirma que el login sí funciona**, así que la BD real debe tener la columna con el nombre correcto (`credencial_encriptada`).
- **Compara password en texto plano** (`ps.setString(2, request.password())`); nunca invoca `BCrypt.checkpw`. Riesgo de seguridad crítico si la BD guarda hashes.
- Esta clase es usada por `UserLoginController`.

---

## 5. Servicios (`service/`)

| Servicio | Estado real | Notas |
|---|---|---|
| `DonanteServices` | Funcional con bugs SQL | Sólo procesa `registrar`; faltan modificar/eliminar/buscar. |
| `InstitucionServices` | **VACÍO** (sólo `class InstitucionServices {}`) | No hay forma de gestionar instituciones desde la app. |
| `PersonalMedicoServices` | Funcional con bugs SQL | Sólo `procesarNuevoPersonal`. No encripta la contraseña (a pesar de existir la clase). |
| `DonacionService` | OK parcial | Falta `actualizar`/`eliminar`. |
| `ReceptorServices` | Funcional con bugs SQL | Sólo `procesarNuevoReceptor`. |
| `SolicitudTransfusionService` | OK parcial | No expone `historialPorDonante` ni búsqueda por receptor. |
| `TransfusionService` | OK parcial | Idem. |
| `UnidadHemoderivadoService` | OK parcial | Faltan alertas/Stock global. |
| `UbicacionService` | OK parcial | Valida con `id == 0` pero no contempla la posibilidad de que el dropdown devuelva IDs null. |
| `AnalisisLabServices` | OK parcial | `esSangreSegura` es crítico pero depende de la columna `chacas` (typo). |
| `DatosTriajeServices` | Valida rangos distintos al DAO | Inconsistencia descrita arriba. |

### 5.1 `ValidacionException` (RuntimeException) y manejo de errores

- Los servicios lanzan `ValidacionException` (Runtime). **Ninguna Controller la captura**. Resultado, cualquier validación rota la aplicación con stack trace en lugar de mostrar el `Alerta` bonito.
- `Mensaje.ERR_REGISTRO_UBI` se duplica en `UbicacionService.registrarNuevaUbicacion` (lanza string literal en lugar de constante).

---

## 6. Controllers (`controller/`)

### 6.1 `UserLoginController`

- **El password se lee con `TextField`** (`@FXML private TextField inputPassword;`) – no se oculta. Bug de seguridad/UX grave.
- **Login verificado por el usuario: funciona.** Asumo entonces que la columna de credenciales en la BD real se llama tal cual la consulta (`credencial_encriptada`) o que se aplicó una migración distinta al `init_donaway.sql` que está versionado. Aun así, hay observaciones importantes sobre **cómo** funciona:
  - `UserImpl.validarLogin` compara la contraseña **en texto plano** contra la columna `credencial_encriptada` (`ps.setString(2, request.password())`). **Nunca invoca `BCrypt.checkpw`**, aunque la clase `SeguridadEncrip` ya existe y es capaz de hashear. Si la BD guarda hashes BCrypt, el login pasa sólo si la contraseña plana coincide carácter a carácter con el hash (lo cual nunca ocurrirá). Si la BD guarda contraseñas planas, el login pasa pero es un riesgo de seguridad crítico.
  - Recomendación: migrar el seed a hashes generados por `SeguridadEncrip.hasearpassword` y validar con `BCrypt.checkpw(plain, hash)`; añadir `PasswordField` en el FXML.
- `cargarVistaSegunRol` define rutas equivocadas:
  - `case "BIOANALISTA_PRINCIPAL"` → `"/views/vistas_formularios/vistas_generales/plantilla_personal_medico.fxml"` (carpeta y nombre **inexistentes**, archivo real está en `/views/vistas_genericas/Plantilla_personal_medico.fxml`). Mayúsculas/minúsculas tampoco coinciden → **no se carga en Linux**. *Nota: el usuario indica que el login funciona, por lo que es probable que esta ruta sí resuelva en su entorno (¿quizás hay un archivo con ese nombre en algún classpath adicional, o un mirror del FXML?). Habría que confirmar qué archivo se está cargando realmente cuando se loguea un BIOANALISTA_PRINCIPAL.*
  - `case "GESTOR_HEMODERIVADOS"` → `"/"` (ruta inválida).
  - `case "RECEPCIONISTA"` → `"/"` (ruta inválida). Además `Cargo.RECEPCIONISTA` está mal escrito en el enum (`RECEPSIONISTA`), por lo que el `switch` nunca matchea. *Idem: si en la práctica el login lleva a una pantalla, entonces el rol activo no está pasando por este switch o se resuelve por un camino distinto.*
  - `case "ADMIN_INSTITUCIONAL"` → `""` (string vacío, error garantizado).
- Si el login "pasa", abre una nueva `Stage` y **cierra la ventana actual** — no hay registro de la sesión persistido más allá de `SessionManager` (en memoria). Cerrar la app = perder sesión.

### 6.2 `DonanteController` (registra donante)

- **Hardcodea** `String idubi = "lksldaksldkasl";` (placeholder absurdo). El DAO recibe ese id y va a fallar en el INSERT.
- **No usa la vista de ubicación** (`btn_abrirMapa`) que sí muestra el mapa en su PopOver. La vista nunca se llama y el valor nunca se setea correctamente.
- **No valida mayoría de edad** (mensaje `ERR_MENOR_EDAD` existe pero no se usa).
- **No valida campos vacíos** (puede insertar donante con documento vacío).
- `cargarVistaSegunRol` no aplica; `donanteDao.actualizarEstado` se llama pero el estado se envía por defecto sin preguntar.

### 6.3 `ProbandoDaoController` (en `controller/user`)

- Es código de pruebas (parsea fechas en formato `dd/MM/yyyy`, `dd/MM/yyyy HH:mm` con `LocalDateTime.parse` que **lanza** si la fecha es inválida – sin try/catch, rompe la app).
- Crea `Donante` con un **constructor inexistente** que no asigna nada (todos los campos quedan en null). Luego `DonanteServices.procesarNuevoDonante` recibe un donante con `documentIdDonante` null → DAO intentará un INSERT con null → SQL falla por PK NOT NULL.
- **No debería estar en producción** – borrar.

### 6.4 `ControllerInventario`

- `initialize()` carga `/views/vistas_formularios/vistas_inventario/grafica_cant_SangreReservada.fxml`. Este FXML existe.
- `irAInicio()` carga `grafica_cant_Donante.fxml` (en `/vistas_modulos/`). Existe.
- `irALaboratorio()` carga `form_unidad_hermoderivados.fxml`. Existe, sin controller y sin `fx:id` – es sólo estética.
- `irASolicitud()` carga `form_solicitud.fxml`. Existe pero **sin controller**, sin `fx:id`, botón `Enviar` sin `onAction` – no hace nada al pulsar.
- `irAInspector()` carga `grafica_cant_Donante.fxml` (mismo) – no parece inspector real.
- `abrirGestiongraficas()` carga `panel_modulo.fxml` con `MisModulos` que apunta a 3 gráficos. Existe.
- Toda la lógica de notificaciones y perfil se hace **a fuego** con datos hardcoded ("Alexa Greilenny Bello Guzmán", etc.) y no desde la sesión.

### 6.5 `ControllerInventarioTabla`

- Llena los charts con datos **hardcoded** (`obtenerDatosInventario()` y `obtenerDatosPacientes()` con `FXCollections.observableArrayList(...)`). El sistema nunca consulta la BD.
- `initialize()` se llama **dos veces** sobre los mismos charts (carga `datos` y luego `datosInventario` + `datosPacientes`).
- Hay un bloque entero comentado al final (líneas 182-259): código duplicado/deprecated.

### 6.6 `ControllerPrincipal` (personal médico)

- Imports y referencias cruzadas a `GraficasController` y `VistasEmergentes`.
- `ejecutarCargaModuloInicial` carga `panelRegistroDonante.fxml` y hace `lookup("#btn_abrirMapa")`. Esta vista tiene un `Button fx:id="btn_abrirMapa"` que dispara el popover. **El popover se carga pero no tiene `fx:controller`** (`form_ubicacion.fxml` no lo define), por lo que **los ComboBox nunca se llenan**: son editables y vacíos.
- `procesarVisualizacionGrafica` carga gráficos con datos hardcoded.
- `abrirGestionFormularios_solicitudes` carga `form_solicitud.fxml` sin controller – no envía nada.
- `abrirGestionFormularios_analisis` carga 2 forms (extracción, resultado_lab) que **no tienen controller** ni acción de envío – no envían nada.
- `abrirGestionFormularios_laboratorio` carga 2 forms (transfusión, triaje) que **no tienen controller** – no envían nada.
- Notificaciones hardcoded: `lista.getItems().addAll(...)` con datos falsos.
- `mostrarPopOverPerfil` arma el popup con `usuario.nombre()` etc. – al menos usa `SessionManager`. Pero los labels de departamento muestran el `cargo` por error (`usuario.cargo().toString()` en lugar de `usuario.departamento().toString()`).

### 6.7 `ControllerSecretaria`

- Código muy similar a `ControllerInventario`. **Duplicación masiva** (~85% idéntico).
- `abrirGestiongraficas` apunta a 3 formularios de búsqueda, pero esos FXMLs no tienen controller (sólo estética).
- `irAInicio` carga el módulo de personal médico (`panelRegistroDonante.fxml`) que **no debería abrir la secretaría** – probablemente copy/paste.

### 6.8 `ControllerGraficosIndependiente` (misnamed)

- Se llama "independiente" pero está acoplado a `ControllerPrincipal`/`ControllerInventario`/`ControllerSecretaria`.
- `initialize` setea handlers de 3 botones (análisis, pacientes, sangre) → carga gráficas. Datos hardcoded.
- Cuando se carga `graficaCentral_AnalisisEspera.fxml` busca `contenedor_detalles_grafica` con `lookup`. Pero ese `VBox` **tiene `fx:id="contenedor_detalles_grafica"`** – OK.
- `cargarGraficaIndependiente` depende de `principalController.getContenedorPrincipal()` – si el gestor no fue seteado (caso desde `controllerSecretaria`/`caeza_inventario`), la gráfica se muestra en `null` y no se ve.

### 6.9 `VistasEmergentes`

- `mostrarPopOverUbicacion` carga `form_ubicacion.fxml` sin controller. Los ComboBox nunca se hidratan. El botón `Enviar` no tiene acción.
- `cargarCentroGestionFormularios` carga `panel_formularios_analab.fxml` – **no existe** en `src/main/resources`.

### 6.10 `ControladorCentroNotificaciones`

- `listaCompleta` se inicializa con `new VBox()` en la declaración (campo) y se sobreescribe por FXML. OK por FXML loader.
- `mostrarNoti_detalle` muestra texto literal fijo ("ID de Transfusión: #12345...") – no datos reales.
- La clase se usa como controller en `panel_modulo.fxml`, `centroNotificaciones.fxml` y `panelNotificaciones.fxml` – **3 FXML distintos, mismo controller**. Esto causa que al cargar uno se pisen los `fx:id` de otro. Acoplamiento frágil.

### 6.11 `GraficasController`

- Datos 100% hardcoded en `prepararGraficaSangre`, `prepararGraficaPacientes`, `prepararGraficaAnalisis`.
- Los `inicializarNavegacion` (menu lateral) llama a `gestor.cargarGraficaIndependiente(...)`. Si `gestor` no se setea, no funciona (NPE).

### 6.12 `ControllerAuxiliar`, `NotificacionObjeto`, `InventarioUnidad`, `RegistroPacientes`, `ModuloFormularios`, `MenuGraficasObjeto`

- `ControllerAuxiliar` → clase vacía.
- `NotificacionObjeto` (controller) duplica a `Notificacion` (modelo) con la misma estructura.
- `InventarioUnidad` y `RegistroPacientes` son POJOs para charts con `SimpleStringProperty` – ok.
- `ModuloFormularios` y `MenuGraficasObjeto` son POJOs contenedores – ok.

---

## 7. Modelos (`model/`)

### 7.1 `Persona` (abstracta)

- Define campos protected pero el constructor `Persona()` vacío nunca se llama. Se usa `super()` desde `Donante`, `PersonalMedico`. OK.
- Los setters y getters siguen convención JavaBean.

### 7.2 `Donante` / `Receptor` / `PersonalMedico`

- `Donante` define **dos constructores**:
  - Uno con tipos enum (`Sexo`, `TipoSangre`, `FactorRh`) – usado por DAO.
  - Otro con String/String – "constructor temporal" según comentario – **no asigna campos** (sólo `super(...)` desde clase padre y deja el resto en null). Es código muerto.
- `Receptor` tiene constructor con `BigDecimal peso`. DAO hace `ps.setBigDecimal(8, receptor.getPeso())` – OK.
- `PersonalMedico` constructor con 17 parámetros – coincide con `mapearPersonal` en orden.

### 7.3 `SolicitudTransfusion`

- Doble getter/setter para `estadoSolicitud` y `prioridad`: `getEstadoSolicitud()/setEstadoSolicitud()` (devuelve `estadoSolicitudObj`) y `getEstadoSolicitudObj()/setEstadoSolicitudObj()`. **Redundante y confuso**.

### 7.4 `Donacion`

- Constructor con 10 parámetros pero `cantidadPinta` está como `float`. La columna SQL es `DECIMAL(4,2)` – DAO lee con `getFloat(...)` (puede perder precisión pero funciona).

### 7.5 `AnalisisLaboratorio`

- Modelo OK. La columna SQL es `chacas` (typo) – DAO hace `getBoolean("chacas")` y modelo `isChagas()` – funciona por casualidad.

### 7.6 `UnidadHemoderivados`

- Modelo con `cantidadTotalPintas` (double). Columna SQL no existe – falla al INSERT.

### 7.7 Modelos de Ubicación (`Ubicacion`, `UbicacionDpaVista`, `Region`, `Provincia`, etc.)

- `Ubicacion` tiene campos `idRegion, idProvincia, idMunicipio, idDistrito, idSeccion, idBarrio, idSubBarrio` (int). La tabla SQL tiene `region, provincia, ...` sin prefijo `id_` y tipos diferentes (algunos VARCHAR). **No coinciden** – DAO fallará.

### 7.8 `Institucion`

- Campo `dirreccionCompleta` (typo: doble r). El DAO setea este campo, pero el método se llama `setDirreccionCompleta` (typo). El nombre del getter también: `getDirreccionCompleta`. Inconsistencia con la ortografía.

### 7.9 `Admin`

- Clase **vacía** (`public class Admin {}`). No se usa en ninguna parte.

### 7.10 `Notificacion` (model) vs `NotificacionObjeto` (controller)

- **Dos clases equivalentes**: `model.user.Notificacion` y `controller.controllerpersonalmedico.NotificacionObjeto`. Una debería eliminarse.

---

## 8. Enums (`utils/constants/`)

| Enum | Inconsistencias / Issues |
|------|---|
| `Rol` | `RECEPCIONISTA("Recepcionista")` (bien). |
| `Cargo` | `RECEPSIONISTA` (typo) — diferente a `Rol`. |
| `Departamento` | `RECEPSIONISTA` (typo) — diferente a `Rol`. |
| `Jornada` | "Nocturno(12 AM - 6 AM)" – typos ortográficos (sin espacio antes del paréntesis). Las horas parecen invertidas (nocturno 12 AM–6 AM es válido en RD). |
| `EstadoPersonal` | OK. |
| `EstadoDisponibilidad` | OK. |
| `EstadoUnidad` | OK. |
| `EstadoSolicitud` | No incluye `COMPLETADA` que usa `TransfusionDaoImpl`. |
| `PrioridadSolicitud` | OK. |
| `TipoComponente` | "Globulos Rojos" sin tilde. |
| `TipoDonancion` | **Typo en el nombre de la clase** (debería ser `TipoDonacion`). |
| `TipoSangre` | OK. |
| `TipoTransfusion` | "Autologa", "Alogenetica" sin tilde. |
| `Sexo` | **Enum vs descripcionFront tienen un mismatch fundamental**: el enum tiene `M="Masculino", F="Femenino"`. El método `desdeTextoFront()` busca por descripción, pero `Sexo.valueOf(rs.getString("sexo"))` en el DAO **busca por nombre del enum** (`M`/`F`). La columna SQL tiene `sexo VARCHAR(10)` y almacena probablemente "Masculino"/"Femenino". **Nunca matchearán**. |
| `FactorRh` | OK. |

---

## 9. FXML ↔ Controllers (binding)

### 9.1 FXMLs sin `fx:controller` (no funcionales)

| FXML | Notas |
|---|---|
| `form_ubicacion.fxml` | ComboBox nunca poblados; botón Enviar sin `onAction`. |
| `form_unidad_hermoderivados.fxml` | Todos los TextField/ComboBox sin `fx:id` y sin acción de envío. |
| `form_solicitud.fxml` (× 2 copias) | Sin controller; sin `fx:id`; botón Enviar sin `onAction`. |
| `registro_extraccion.fxml` | Sin controller; sin action. |
| `registro_resultado_lab.fxml` | Sin controller; sin action. |
| `registro_datos_triaje.fxml` | Sin controller; sin action. |
| `form_datos_transfusion.fxml` | Sin controller; sin action. |
| `form_buscar_solicitud.fxml` (× 2) | Sin controller. |
| `form_busqueda_donacion.fxml` | Sin controller. |
| `form_busqueda_donante.fxml` (× 2) | Sin controller. |
| `form_busqueda_solicitudes.fxml` | Sin controller. |
| `form_busqueda_donaciones.fxml` | Sin controller. |
| `form_registro_institucional.fxml` | Sin controller (debería llamar a `InstitucionServices` que está vacío). |

### 9.2 FXMLs que referencian controllers inexistentes o mal mapeados

- `panelDetallesPerfil.fxml` (× 2) tiene `fx:controller="ControllerGraficosIndependiente"` que **no es un profile controller**. La pantalla muestra detalles de usuario pero el controller sólo sabe de gráficos.
- `panelNotificaciones.fxml` declara `listaCompleta` (`fx:id`) – el controller `ControladorCentroNotificaciones` tiene `listaCompleta = new VBox()`. OK por FXML loader.

### 9.3 Rutas FXML rotas en código

| Llamada | Problema |
|--------|----------|
| `UserLoginController.cargarVistaSegunRol` → `/views/vistas_formularios/vistas_generales/plantilla_personal_medico.fxml` | Archivo **no existe** en esa ruta (existe en `/views/vistas_genericas/Plantilla_personal_medico.fxml` con mayúscula). |
| `ControladorCentroNotificaciones` y `ControllerInventario` referencian `grafica_cant_sangreReservada.fxml` con "s" minúscula en el segundo nombre – archivo `grafica_cant_sangreReservada.fxml` existe en `/vistas_modulos/` y `grafica_cant_SangreReservada.fxml` con S mayúscula en `/vistas_inventario/`. Confusión. |
| `ControllerInventario.initialize` carga `grafica_cant_SangreReservada.fxml` (S mayúscula) – existe. |
| `ControllerInventario.irAInicio` carga `grafica_cant_Donante.fxml` – existe. |
| `ControllerInventario.irAInicio` carga `grafica_cant_Donante.fxml` (mismo) – repite. |
| `VistasEmergentes.cargarCentroGestionFormularios` carga `panel_formularios_analab.fxml` – **no existe**. |
| `ControllerInventario.irABusquedas` carga `form_buscar_solicitud.fxml` – existe en `/vistas_modulos/`. |

### 9.4 `fx:id` referenciados pero no declarados

- `ControllerSecretaria.irARegistro` hace `lookup("#btnabrirMapa")` pero el FXML cargado (`panelRegistroDonante.fxml`) tiene `fx:id="btn_abrirMapa"` (sin 't' de typo). No matchea → silently `null`.
- `ControllerPrincipal.mostrarPopOverPerfil` – los `Label` en el FXML son `lbl_nombre_usuario`, `lbl_cargo_usuario`, `lbl_id_usuario`, `lbl_departamento_usuario`, `lbl_region_usuario`. El controller no setea ninguno (sólo hace el popup aparte). Inconsistencia: el controller usa `usuario.cargo().toString()` para "Departamento" – bug visible.

### 9.5 `fx:id` con typo en archivos

- `panelRegistroDonante.fxml` tiene `fx:id="dateNaciemiento"` (typo: debería ser `dateNacimiento`). Java es `private DatePicker dateNaciemiento;` – funciona por simetría pero es feo.
- `panelRegistroDonante.fxml` tiene `fx:id="comboTipoSangre" id="a_ver"` – el `id` (no `fx:id`) no tiene efecto y ensucia.

### 9.6 Recursos faltantes referenciados

- `VistasEmergentes.cargarCentroGestionFormularios` carga `panel_formularios_analab.fxml` que **no existe**.
- `Login` references style_personal_medico/formulario_nuevoRegistro.css que probablemente existe, pero la carpeta de styles debe contener todos los CSS referidos (no audité todos).

---

## 10. Utilidades (`utils/`)

### 10.1 `SeguridadEncrip`

- `hasearpassword` (typo) usa BCrypt con factor 12. OK.
- **Nunca hay un `verificarPassword`** (debería existir `BCrypt.checkpw(plain, hash)`). El login pasa la password plana al SQL.

### 10.2 `IdGenerador`

- `generadorUUID()` – bien.
- `generadorCodigoReferencia(prefijo)` – bien.

### 10.3 `CarnetGenerador`

- `generadorSiglasInstitucion(nombre)` – heurística, OK.
- `generadorId()` – usa NanoId con alfabeto limitado – OK.
- `GeneradorCarnet(nombreInst)` – formato `DW-{siglas}-{5caracteres}` – OK.
- **El método se llama `GeneradorCarnet`** (PascalCase, en una clase de utilidades). Convención Java → debería ser `generarCarnet`.

### 10.4 `Time`

- `dateNow()` – formato "dd 'de' MMMM 'de' yyyy" en español. OK.
- `dateTimeNow()` – **no es static** – no se puede usar desde la clase. Debería ser `static`.

### 10.5 `Mensaje` (constantes)

- Sólo contiene strings, no se usa para internacionalización. Aceptable para un proyecto simple.

---

## 11. Logging y observabilidad

- `System.out.println` por do en controllers/DAO.
- `e.printStackTrace()` en lugar de un logger (Log4j/SLF4J).
- Ninguna traza unificada.

---

## 12. Seguridad

- Contraseña en `TextField` (no `PasswordField`).
- Login compara passwords en texto plano.
- BCrypt disponible pero no se aplica.
- `.env` con credenciales versionado.
- No hay HTTPS (no aplica a desktop, pero la app podría manejar tokens).
- No hay protección CSRF (no aplica a desktop).
- Sesión en memoria con `SessionManager` (Singleton estático) – no se cierra al cerrar la app; no hay logout implementado.

---

## 13. UX / usabilidad

- Hardcoded user data ("Alexa Greilenny Bello Guzmán") en 3 controllers distintos – placeholder obvio.
- Gráficos con datos inventados (45/12/65/20 pacientes, 22% etc.).
- Notificaciones hardcoded.
- Botones de "Cerrar Sesión" sin `onAction`.
- Popups sin datos reales.
- Campos sin validación visual.

---

## 14. Errores e inconsistencias (resumen consolidado)

### 14.1 Bugs críticos (impiden ejecutar flujos reales)

1. **`UUID_TO_BIN`/`BIN_TO_UUID` sobre columnas `VARCHAR(36)`** → error SQL en cada operación de casi todas las tablas (según `init_donaway.sql`).
2. **Tabla `regiones` inexistente** (según `init_donaway.sql`) → primera carga de ubicación falla.
3. **Vista `v_ubicacion_completa` inexistente** (según `init_donaway.sql`) → todas las consultas de donante/personal/institución fallan.
4. **`InstitucionImpl.registrarInstitucion` inserta columna `fecha_registro` inexistente** (según `init_donaway.sql`).
5. **`SolicitudTransfusionDaoImpl.registrar` inserta columnas inexistentes** (`codigo_referencia`, `cantidad_solicitada`, `tipo_componente`) (según `init_donaway.sql`).
6. **`UnidadHemoderivadoDaoImpl.registrar` inserta columnas inexistentes** (`total_sangre`, `tipo_sangre`, `factor_rh`, `codigo_referencia`, `cantidad_pintas`) (según `init_donaway.sql`).
7. **`ReceptorImpl.registrarReceptor` inserta columnas con nombre mal escrito y una inexistente** (`fechaRegistro` en vez de `fecha_registro`, `email`) (según `init_donaway.sql`).
8. **`TransfusionDaoImpl.registrar` usa estado `COMPLETADA`** no presente en el enum.
9. **`UserLoginController.cargarVistaSegunRol` apunta a rutas inexistentes** (algunas branches son string vacío o "/").
10. **Casi todos los formularios FXML carecen de `fx:controller` y de `onAction`** – los botones "Enviar" no hacen nada en la mayoría de vistas.
12. **`form_ubicacion.fxml` no tiene controller** → los ComboBox están vacíos.
13. **`DonanteController` hardcodea `idubi = "lksldaksldkasl"`** → donación fallará.
14. **`ConexionDbDonaway` no lee credenciales de la nube** ni permite alternar local/nube.
15. **`Donante` tiene un constructor con String que NO asigna campos** – `ProbandoDaoController` lo usa → NPEs/inserción rota.

### 14.2 Bugs de seguridad

1. `PasswordField` no usado en `login.fxml`.
2. BCrypt no se aplica al verificar login (se compara en texto plano).
3. `.env` versionado.

### 14.3 Inconsistencias

1. Enums con grafía inconsistente (`RECEPCIONISTA` vs `RECEPSIONISTA`).
2. `Sexo.valueOf(...)` vs `Sexo.desdeTextoFront(...)` semánticamente incompatibles con lo guardado en BD.
3. `dirreccionCompleta` (typo doble r) en modelos y DAOs.
5. `SeguridadEncrip.hasearpassword` (typo).
6. `Time.dateTimeNow()` no static.
7. `TipoDonancion` (typo nombre clase).
8. `Institucion.setDirreccionCompleta` (typo doble).
9. `Donacion.fechaExtracion` (typo "cion" en lugar de "cción").
10. `SolicitudTransfusion` tiene doble getter/setter para `estadoSolicitud` y `prioridad`.
11. Constructor "temporal" de `Donante` con String y campos sin asignar.
12. DAO `Sexo` lee enum `valueOf` (espera "M"/"F") pero los valores guardados son "Masculino"/"Femenino" (descripción).

### 14.4 Deuda arquitectónica

- Código duplicado masivo (`ControllerInventario` ≈ `ControllerSecretaria` ≈ `ControllerPrincipal`).
- DAO `ReceptorImpl` con 4 métodos vacíos.
- `InstitucionServices` completamente vacío.
- `ControllerAuxiliar` y `Admin` vacíos.
- `ProbandoDaoController` es código de prueba en producción.
- `ControllerGraficosIndependiente` mal nombrado (maneja perfil y gráficos).
- Acoplamiento fuerte entre controllers (referencias cruzadas con setters).
- Gráficos con datos hardcoded.
- Notificaciones hardcoded.
- Perfiles hardcoded.

### 14.5 SQL

- Tipos `VARCHAR(36)` incompatibles con `UUID_TO_BIN`.
- Múltiples columnas referenciadas no existen en script.
- Vista `v_ubicacion_completa` no creada.
- Tabla `regiones` no creada.
- `FOREIGN_KEY_CHECKS` desactivado sin reactivar.
- Falta `fecha_registro` en `institucion_medica`.
- Typo `presion_distolica` en `datos_triaje`.

---

## 15. Recomendaciones priorizadas

### 15.1 Inmediato (fixes bloqueantes)

- Definir el modelo SQL único (alinear columnas Java ↔ SQL).
- Cambiar columnas UUID a `BINARY(16)` o usar columnas VARCHAR sin funciones `UUID_TO_BIN`.
- Crear la vista `v_ubicacion_completa` y la tabla `regiones` con sus datos.
- Reemplazar `UserImpl.validarLogin` por una verificación con `BCrypt.checkpw`.
- Cambiar `TextField` de password por `PasswordField` en `login.fxml`.
- Corregir las rutas FXML en `cargarVistaSegunRol`.
- Eliminar hardcode `idubi` y forzar el uso del flujo de ubicación.
- Eliminar `ProbandoDaoController` y el constructor temporal de `Donante`.

### 15.2 Corto plazo (refactor)

- Implementar `InstitucionServices` y `ReceptorImpl` (métodos vacíos).
- Unificar las constantes duplicadas (`RECEPCIONISTA`).
- Corregir el typo `RECEPSIONISTA` en `Cargo` y `Departamento`.
- Corregir el modelo de `Sexo` (decidir entre enum o descripción, alinear SQL).
- Sustituir todos los `DAO.getConnection()` por un pool configurable y lazy.
- Reemplazar `printStackTrace` por logger (SLF4J).
- Centralizar el manejo de `ValidacionException` en los controllers (try/catch + `Alerta`).

### 15.3 Mediano plazo (diseño)

- Quitar duplicación de controllers (`ControllerInventario`/`ControllerSecretaria`/`ControllerPrincipal`).
- Inyectar dependencias (Spring o similar) en lugar de `new Impl()`.
- Agregar tests (JUnit 5 + Mockito) para servicios y modelos.
- Implementar CRUD completo en cada módulo (crear/modificar/eliminar/listar).
- Reemplazar gráficos hardcoded por consultas reales (servicio de dashboard).
- Implementar notificaciones reales (poll o push local).
- Implementar logout.
- Internacionalización (i18n) para mensajes y labels.

---

## 16. Apéndice — chequeo rápido de rutas y archivos

| Recurso | ¿Existe? |
|---------|-----------|
| `/views/vistas_genericas/Plantilla_personal_medico.fxml` | ✅ |
| `/views/vistas_formularios/vistas_generales/plantilla_personal_medico.fxml` (referenciada en código) | ❌ |
| `/views/vistas_formularios/vistas_personal_medico/vistas_secretaria/Plantilla_secretaria.fxml` | ✅ |
| `/views/vistas_formularios/vistas_personal_medico/registro_personal_medico.fxml` (referenciado en ControllerInventario) | ❌ (no existe) |
| `panel_formularios_analab.fxml` (referenciado en VistasEmergentes) | ❌ |
| Tabla SQL `regiones` | ❌ |
| Vista SQL `v_ubicacion_completa` | ❌ |
| `controllerSecretaria.registrar_personal_medico` | ❌ |
| Servicio `InstitucionServices` con métodos reales | ❌ (vacío) |
| DAO `ReceptorImpl` con `obtenerTodo`, `buscarPorNombre`, `obtenerPorId`, `modificarReceptor` | ❌ (vacíos) |

---

## 17. Conclusión

> El proyecto es un **esqueleto navegable**: las pantallas se cargan, los menús colapsan, los popups aparecen, pero **ningún flujo funcional está conectado al backend**. La capa DAO tiene la forma correcta (interfaces, implementación, manejo de errores) pero su SQL no es ejecutable; la capa de servicios es muy delgada y mayormente incompleta; los controllers son en gran parte copy-paste con datos ficticios; y los FXMLs críticos carecen de `fx:controller` o de `onAction`.
>
> Antes de continuar** se recomienda encarecidamente**:
> 1. Decidir y congelar el **esquema SQL** y ajustar todos los DAO/SQL.
> 2. Decidir y congelar el **contrato FXML ↔ Controller** (qué controllers atienden qué vistas y qué `fx:id`s viajan).
> 3. Eliminar el código duplicado y muerto (controllers auxiliares, `ProbandoDaoController`, métodos vacíos).
> 4. Implementar la **lógica de seguridad** (BCrypt real + PasswordField + logout).
> 5. Luego, implementar CRUDs reales uno por uno, con tests.