# DonaWay RD

Sistema de escritorio para la **gestión de donaciones y reservas de sangre** en instituciones médicas. Centraliza el registro de donantes, el triaje, los análisis de laboratorio, el inventario de hemoderivados, las solicitudes de transfusión y la trazabilidad de las unidades donadas.

> **Estado del proyecto:** esqueleto funcional en fase temprana. El login y la navegación básica funcionan contra una base de datos MySQL real, pero la mayoría de los flujos de negocio todavía no están conectados de extremo a extremo. Antes de usarlo en un entorno real, revisa la sección [Refactorización necesaria](#refactorización-necesaria-para-ponerlo-funcional).

---

## Índice

- [Qué hace el proyecto](#qué-hace-el-proyecto)
- [Roles del sistema](#roles-del-sistema)
- [Tecnologías](#tecnologías)
- [Arquitectura y estructura](#arquitectura-y-estructura)
- [Requisitos para ejecutarlo](#requisitos-para-ejecutarlo)
- [Configuración obligatoria](#configuración-obligatoria)
- [Cómo ejecutarlo](#cómo-ejecutarlo)
- [Estado actual](#estado-actual)
- [Refactorización necesaria para ponerlo funcional](#refactorización-necesaria-para-ponerlo-funcional)
- [Documentación relacionada](#documentación-relacionada)
- [Convenciones del equipo](#convenciones-del-equipo)

---

## Qué hace el proyecto

DonaWay es una aplicación de escritorio JavaFX pensada para el personal de una institución de salud. Sus módulos principales son:

| Módulo | Qué resuelve |
|---|---|
| **Autenticación** | Inicio de sesión con ID institucional y contraseña, sesión en memoria y redirección según el cargo del usuario. |
| **Instituciones** | Registro y consulta de instituciones médicas por RNC, contacto de emergencia y ubicación. |
| **Personal médico** | Registro de personal con cargo, departamento, rol, carné, jornada, estado y credenciales. |
| **Donantes** | Registro y consulta de donantes con datos personales, tipo de sangre, factor Rh, estado de disponibilidad y ubicación. |
| **Ubicación / DPA** | Selección jerárquica de región, provincia, municipio, distrito, sección, barrio y sub-barrio de República Dominicana. |
| **Donaciones** | Registro de extracciones con código de referencia, tipo de donación, componente y cantidad de pintas. |
| **Triaje** | Registro de peso, presión arterial, pulso, temperatura y hemoglobina; validación de aptitud para donar. |
| **Análisis de laboratorio** | Registro de resultados de VIH, hepatitis B y C, sífilis, Chagas, NAT y otros; determinación de sangre segura. |
| **Inventario de hemoderivados** | Ingreso de unidades, código de barras, tipo de sangre/Rh, componente, estado, caducidad y alertas de vencimiento. |
| **Solicitudes de transfusión** | Creación de solicitudes por receptor, componente, cantidad, prioridad y estado. |
| **Transfusiones** | Registro del procedimiento, unidad utilizada, tipo de transfusión y cantidad administrada. |
| **Panel / notificaciones / perfil** | Pantallas de resumen, gráficos y notificaciones. Actualmente usan datos de ejemplo. |

La aplicación está orientada a un entorno institucional: cada usuario pertenece a una institución médica y opera según su cargo.

---

## Roles del sistema

Los cargos se definen en `src/main/java/com/donaway/utils/constants/Cargo.java`:

| Cargo | Enfoque esperado |
|---|---|
| `ADMIN_INSTITUCIONAL` | Administración de la institución y su personal. |
| `ADMIN_PERSONAL_MEDICO` | Administración del personal médico. |
| `BIOANALISTA_PRINCIPAL` | Triaje, análisis de laboratorio y gestión clínica de donaciones. |
| `ASISTENTE_BIONALISTA` | Apoyo en laboratorio y análisis. |
| `RECEPSIONISTA` | Recepción, registro de donantes y solicitudes. |
| `GESTOR_INVENTARIO` | Inventario de hemoderivados y disponibilidad. |

> ⚠️ La grafía `RECEPSIONISTA` es un error tipográfico presente en el código y en el esquema real de la base de datos. Se mantiene documentada tal cual para no confundirla con el valor real; corregirla es una tarea de refactor.

---

## Tecnologías

| Tecnología | Versión | Uso |
|---|---|---|
| **Java** | 21 | Lenguaje y runtime. |
| **JavaFX** | 21 (`javafx-controls`, `javafx-fxml`) | Interfaz gráfica de escritorio. |
| **FXML** | — | Definición declarativa de vistas. |
| **CSS** | — | Estilos visuales de la interfaz. |
| **ControlsFX** | 11.1.2 | Componentes adicionales de UI. |
| **Maven** | 3.9+ (no hay wrapper incluido) | Gestión de dependencias y build. |
| **MySQL** | 8.x | Base de datos relacional. |
| **MySQL Connector/J** | 8.4.0 | Driver JDBC. |
| **HikariCP** | 5.1.0 | Pool de conexiones. |
| **dotenv-java** | 3.0.0 | Lectura de variables desde `.env`. |
| **jBCrypt** | 0.4 | Hashing de contraseñas (disponible, pero el login actual no lo usa). |
| **java-uuid-generator** | 4.3.0 | Generación de UUID. |
| **NanoID (jnanoid)** | 1.0.1 | Generación de identificadores cortos y códigos de carné. |

### Dependencias que **no** están en el proyecto

- No hay framework web ni servidor embebido: es una app de escritorio.
- No hay H2 en `pom.xml`, aunque `.env.example` lo menciona.
- No hay Maven Wrapper (`mvnw` / `mvnw.cmd`).
- No hay framework de tests (JUnit) ni plugins de cobertura.
- No hay logger (SLF4J/Logback); se usa `System.out.println` y `printStackTrace`.

---

## Arquitectura y estructura

El proyecto sigue una arquitectura por capas, sin inyección de dependencias:

```
Controller (JavaFX)  →  Service (validaciones)  →  DAO (JDBC)  →  MySQL
        ↑                      ↑                      ↑
      FXML/CSS             modelos/DTOs          HikariCP + .env
```

```
Donaway-app/
├── pom.xml
├── .env                        # credenciales locales (NO versionar)
├── .env.example                # plantilla (desactualizada, ver notas)
├── src/main/java/com/donaway/
│   ├── main/Main.java           # punto de entrada JavaFX
│   ├── controller/              # controladores de las vistas
│   ├── service/                 # reglas de negocio y validaciones
│   ├── dao/                     # interfaces de acceso a datos
│   │   └── impl/                # implementaciones JDBC/MySQL
│   ├── model/                   # entidades del dominio
│   ├── dto/login/               # DTOs de login y sesión
│   ├── exception/               # Alerta y ValidacionException
│   └── utils/
│       ├── constants/           # enums del dominio
│       ├── db/                  # ConexionDbDonaway (HikariCP)
│       ├── generador/           # UUID, carné, códigos
│       ├── security/            # BCrypt
│       ├── mensaje/             # mensajes de error
│       └── time/                # utilidades de fecha
├── src/main/resources/
│   ├── views/                   # archivos FXML
│   ├── style/                   # hojas CSS
│   ├── img/                     # imágenes e iconos
│   └── db/                      # scripts SQL de inicialización
├── docs/                        # auditorías y esquema real de BD
├── DIAGNOSTICO_GENERAL.md
├── AUDITORIA_DIAGNOSTICO.md
└── PLAN_ACCION.md
```

### Puntos de entrada y capas

- **`com.donaway.main.Main`**: carga `login.fxml` y abre la ventana principal.
- **`UserLoginController`**: valida credenciales, guarda la sesión en `SessionManager` y abre la plantilla correspondiente al cargo.
- **Servicios** (`DonanteServices`, `DonacionService`, `DatosTriajeServices`, `AnalisisLabServices`, `UnidadHemoderivadoService`, `SolicitudTransfusionService`, `TransfusionService`, `UbicacionService`, etc.): validan datos y delegan en los DAO.
- **DAO**: implementaciones JDBC con SQL nativo y funciones `UUID_TO_BIN` / `BIN_TO_UUID`.
- **`ConexionDbDonaway`**: configura HikariCP a partir de las variables `DB_LOCAL_DONAWAY_*` del `.env`.

---

## Requisitos para ejecutarlo

Antes de ejecutar la aplicación necesitas tener instalado y configurado:

### Software obligatorio

| Requisito | Versión / nota |
|---|---|
| **JDK** | 21 (con `JAVA_HOME` configurado). Verifica con `java -version`. |
| **Maven** | 3.9 o superior. Verifica con `mvn -v`. No hay `mvnw` en el repositorio. |
| **MySQL Server** | 8.x en ejecución, accesible por TCP (por defecto `localhost:3306`). |
| **Cliente MySQL** | MySQL Workbench, DBeaver, HeidiSQL o la CLI `mysql`. |
| **Sistema operativo con entorno gráfico** | Windows, macOS o Linux con escritorio; JavaFX necesita una sesión gráfica. |

Opcional pero recomendado:

- IDE con soporte Maven y JavaFX (IntelliJ IDEA, Eclipse con m2e, VS Code con Extension Pack for Java).
- Un usuario de MySQL con permisos de `CREATE`, `ALTER`, `INSERT`, `SELECT`, `UPDATE` y `DELETE` sobre la base `donaway`.
- Git para clonar el repositorio.

### Comprobaciones rápidas

```bash
java -version          # debe mostrar 21.x
mvn -v                 # debe mostrar Apache Maven 3.9+
mysql --version        # o comprobar el servicio de MySQL
```

> En el entorno donde se preparó este README, `mvn` no estaba en el `PATH`; si te ocurre lo mismo, instala Maven o usa el Maven integrado del IDE.

---

## Configuración obligatoria

Esta sección es la que **debes completar sí o sí** para que la aplicación arranque y conecte con la base de datos.

### 1. Crear la base de datos

```sql
CREATE DATABASE donaway
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_0900_ai_ci;
```

La aplicación espera una base llamada `donaway` en `localhost:3306` (o la URL que definas en `.env`).

### 2. Crear el esquema de tablas

El proyecto **no incluye hoy un script SQL limpio y ejecutable** que reproduzca el esquema real. Ten en cuenta esto:

| Archivo | Estado | Uso recomendado |
|---|---|---|
| `src/main/resources/db/init_donaway.sql` | Desactualizado y con sintaxis orientada a H2/PostgreSQL en algunos puntos. | **No usarlo como fuente de verdad.** Sirve como referencia parcial. |
| `docs/esquema_bd_real.sql` | Volcado `SHOW CREATE TABLE` de la BD real (2026-10-07), con saltos `\n` escapados y separado por tabulaciones. | **Referencia del esquema real.** No es importable directamente; hay que reconstruir el DDL. |
| `src/main/resources/db/init_data_dpa.sql` | Solo contiene `INSERT` de la división política administrativa (DPA). | Requiere que las tablas DPA ya existan. Además empieza con `SET FOREIGN_KEY_CHECKS = 0;` y no lo reactiva. |

Tablas mínimas que deben existir:

- **Núcleo:** `ubicacion`, `institucion_medica`, `personal_medico`, `donante`, `donacion`, `datos_triaje`, `analisis_lab`, `unidad_hemoderivados`, `receptor`, `solicitud_transfusion`, `transfusion`.
- **DPA:** `regiones`, `provincias`, `municipios`, `distritos`, `secciones`, `barrios`, `sub_barrios`.
- **Vista:** `v_ubicacion_completa` (la usan los DAO de donante, personal médico e institución).

Requisitos del esquema real:

- Los identificadores UUID (`id_donacion`, `id_triaje`, `id_analisis`, `id_hemoderivada`, `id_solicitud`, `id_transfusion`, `id_ubicacion`) son `BINARY(16)` y se consultan con `UUID_TO_BIN(?, 1)` / `BIN_TO_UUID(..., 1)`.
- Los enums de MySQL deben coincidir exactamente con los valores que envía Java (ver [Refactorización](#refactorización-necesaria-para-ponerlo-funcional)).
- La vista `v_ubicacion_completa` debe devolver, al menos, `id_ubicacion`, `id_ubicacion_uuid`, `direccion_completa` y `detalle_direccion`.

> **Tarea pendiente del repositorio:** versionar un `schema.sql` ejecutable y un `seed.sql` reproducible. Mientras no exista, la puesta en marcha depende de restaurar el esquema real desde `docs/esquema_bd_real.sql` o desde un respaldo de la base de datos.

### 3. Crear el archivo `.env`

La aplicación lee **`DB_LOCAL_DONAWAY_*`** desde un archivo `.env` ubicado en la raíz del proyecto (es el directorio de trabajo desde el que se ejecuta). El archivo `.env` está en `.gitignore`, pero **no debe subirse al repositorio**.

Crea `.env` con este contenido y sustituye los valores:

```env
# Base de datos local (MySQL)
DB_LOCAL_DONAWAY_DRIVER=com.mysql.cj.jdbc.Driver
DB_LOCAL_DONAWAY_URL=jdbc:mysql://localhost:3306/donaway?useSSL=false&serverTimezone=America/Santo_Domingo&allowPublicKeyRetrieval=true
DB_LOCAL_DONAWAY_USER=tu_usuario
DB_LOCAL_DONAWAY_PASSWORD=tu_password
```

Notas importantes:

- El driver debe ser `com.mysql.cj.jdbc.Driver`. El `.env.example` actual tiene un error (`om.mysql.cj.jdbc.Driver`, sin la `c`) y apunta a H2, que no está en `pom.xml`.
- `DB_NUBE_DONAWAY_*` existe en `.env`/`.env.example`, pero **`ConexionDbDonaway` no las lee**. Solo se usa la configuración local.
- Si tu contraseña contiene caracteres especiales (`@`, `#`, `:`), codifícala en la URL o usa variables correctamente escapadas.
- La app **no crea la base de datos ni las tablas automáticamente**. Si no existe el esquema, el login fallará con error de conexión/SQL.

### 4. Crear un usuario para poder iniciar sesión

No hay un seed de usuarios en el repositorio. Debes insertar al menos un registro en `personal_medico` con:

- `document_id_personal`: el ID institucional que escribirás en el login.
- `nombre`, `cargo`, `departamento`, `codigo_carnet`, `jornada`, `estado`.
- `credencial_encriptada`: **la contraseña tal cual** (ver advertencia de seguridad).
- `rnc_institucion` e `id_ubicacion`: deben referenciar filas existentes o ser `NULL` si la columna lo permite.

Ejemplo orientativo (ajusta los datos a tu esquema real):

```sql
-- 1) Institución (si no existe)
INSERT INTO institucion_medica (rnc_institucion, id_ubicacion, nombre, contacto_emergencia, fecha_registro)
VALUES ('123456789', NULL, 'Hospital de Prueba', '809-000-0000', NOW());

-- 2) Usuario bioanalista (cargo que sí tiene ruta de navegación implementada)
INSERT INTO personal_medico (
  document_id_personal, rnc_institucion, nombre, sexo, fecha_nacimiento,
  id_ubicacion, cargo, departamento, rol, codigo_carnet, jornada,
  telefono, email, credencial_encriptada, fecha_registro, ultimo_acceso, estado
) VALUES (
  'DW-0001', '123456789', 'Usuario de Prueba', 'M', '1990-01-01',
  NULL, 'BIOANALISTA_PRINCIPAL', 'UNIDAD_EXTRACCION', 'BIOANALISTA', 'DW-HOSP-00001', 'MATUTINA',
  '809-000-0000', 'prueba@example.com', 'MiPassword123', NOW(), NOW(), 'ACTIVO'
);
```

> ⚠️ **Seguridad:** el código actual compara `credencial_encriptada` contra la contraseña en texto plano (`UserImpl.validarLogin`). Por eso el ejemplo guarda la contraseña literal. Esto es un riesgo y debe corregirse con BCrypt antes de cualquier uso real.

Para que el login muestre la pantalla principal correctamente, el `cargo` debe ser `BIOANALISTA_PRINCIPAL`. Los demás cargos tienen rutas incompletas o inválidas.

### 5. Verificar la conexión

Antes de abrir la app puedes probar la conexión:

```bash
mysql -h 127.0.0.1 -P 3306 -u tu_usuario -p donaway -e "SELECT 1;"
```

Si eso funciona y el esquema existe, la app debería conectar.

---

## Cómo ejecutarlo

### Ejecución con Maven (recomendada)

Desde la **raíz del proyecto** (donde están `pom.xml` y `.env`):

```bash
mvn clean javafx:run
```

Pasos equivalentes:

```bash
mvn clean compile
mvn javafx:run
```

El plugin `javafx-maven-plugin` está configurado con `com.donaway.main.Main` como clase principal.

### Ejecución desde un IDE

1. Importa el proyecto como proyecto Maven.
2. Asegúrate de usar **JDK 21**.
3. Ejecuta la clase `com.donaway.main.Main`.
4. Si el IDE no resuelve los módulos de JavaFX, ejecuta primero `mvn clean compile` y deja que Maven importe las dependencias; o configura las VM options de JavaFX.

Ejemplo de VM options si no usas el plugin de Maven:

```text
--module-path "RUTA_A_JAVAFX/lib" --add-modules javafx.controls,javafx.fxml
```

### Problemas comunes al ejecutar

| Síntoma | Causa probable | Solución |
|---|---|---|
| `mvn` no se reconoce | Maven no está en el `PATH`. | Instalar Maven o usar el Maven del IDE. |
| Error de conexión a MySQL | `.env` ausente, credenciales incorrectas o MySQL apagado. | Revisar `.env`, iniciar MySQL y probar con `mysql`. |
| `Unknown database 'donaway'` | No se creó la base. | Ejecutar `CREATE DATABASE donaway ...`. |
| `Table 'donaway.X' doesn't exist` | Esquema incompleto. | Reconstruir el esquema desde `docs/esquema_bd_real.sql`. |
| `Column ... doesn't exist` | El esquema real no coincide con lo que espera el DAO. | Revisar la sección de refactor. |
| Error al cargar `login.fxml` | Se ejecutó desde otro directorio o faltan recursos. | Ejecutar desde la raíz del proyecto. |
| La app abre pero el login falla | No hay usuario activo o la contraseña no coincide. | Insertar el usuario de prueba y revisar el estado `ACTIVO`. |

---

## Estado actual

### Lo que funciona

- Apertura de la aplicación y carga de `login.fxml`.
- Login contra MySQL para usuarios existentes (comparación de contraseña en texto plano).
- Redirección a la pantalla de **bioanalista principal**.
- Navegación básica, menús laterales, popovers y carga de algunas vistas.
- Existencia de servicios y DAO para donantes, donaciones, triaje, análisis, inventario, solicitudes y transfusiones (con validaciones y SQL nativo).
- Carga de gráficos y notificaciones (con datos de ejemplo).

### Lo que no está terminado

| Área | Situación |
|---|---|
| Formularios FXML | Más de 13 vistas no tienen `fx:controller` ni acciones `onAction`; sus botones “Enviar” no hacen nada. |
| Navegación por cargo | `cargarVistaSegunRol` solo tiene una ruta válida; otras apuntan a `""`, `"/"` o a archivos inexistentes. |
| Registro de donantes | `DonanteController` usa un `idubi` hardcodeado y no completa el flujo de ubicación. |
| Instituciones | `InstitucionServices` está vacío; no hay CRUD real desde la UI. |
| Receptores | `ReceptorImpl` tiene métodos vacíos (`modificarReceptor`, `obtenerTodo`, `buscarPorNombre`, `obtenerPorId`). |
| Inventario / solicitudes / transfusiones | Los servicios existen, pero las vistas no están conectadas a ellos. |
| Gráficos y notificaciones | Datos hardcodeados; no consultan la base de datos. |
| Perfil de usuario | Datos de ejemplo; el departamento muestra el cargo por error. |
| Seguridad | Contraseña visible (`TextField`), comparación en texto plano, sin logout real. |
| Calidad | Sin tests, sin logger, `printStackTrace`, código duplicado y clases vacías. |

---

## Refactorización necesaria para ponerlo funcional

Las tareas están ordenadas por prioridad. **P0** bloquea la ejecución de flujos reales; **P1** es necesario para que el sistema sea mantenible; **P2** es mejora progresiva.

### P0 — Bloqueantes

| # | Problema | Acción concreta | Archivos de referencia |
|---|---|---|---|
| 1 | No hay un esquema SQL reproducible ni una única fuente de verdad. `init_donaway.sql` está desactualizado y `docs/esquema_bd_real.sql` no es importable directamente. | Crear `db/schema.sql` ejecutable (tablas, enums, FKs, vista `v_ubicacion_completa`, tablas DPA) y `db/seed.sql` con datos mínimos. Versionarlos y documentar el orden de ejecución. | `src/main/resources/db/`, `docs/esquema_bd_real.sql` |
| 2 | `.env.example` apunta a H2 y tiene el driver mal escrito; `.env` con credenciales ha estado versionado. | Corregir `.env.example` a MySQL (`com.mysql.cj.jdbc.Driver`), eliminar `.env` del historial de Git, rotar credenciales y documentar el formato. | `.env.example`, `.gitignore` |
| 3 | Enums Java vs enums MySQL no coinciden. | Unificar valores: `Cargo.RECEPSIONISTA` vs `RECEPCIONISTA`, `Rol` vs `cargo`, `TipoTransfusion.ALOGENETICA` vs `ALOGENICA`, `EstadoSolicitud.EN_CAMINO` vs `EN CAMINO`, `TipoComponente.GLOBULOS_ROJOS` vs `GLOBULOS ROJOS`. Actualizar código y esquema a la vez. | `utils/constants/`, DAOs |
| 4 | Columnas/tablas que no coinciden con el esquema real. | Corregir `ReceptorImpl` (inserta `email` y `fechaRegistro` inexistentes), `UnidadHemoderivadoDaoImpl.cambiarEstado` (`id_hemorivada` → `id_hemoderivada`), `TransfusionDaoImpl` (`cantidad_administrada` → `cantidad_Sumistrada`), enum `receptor.tipo_sangre` (`Bio` → `O`). | `dao/impl/` |
| 5 | Login inseguro y navegación rota por cargo. | Usar `PasswordField`; hashear con BCrypt al registrar y verificar con `BCrypt.checkpw`; completar `cargarVistaSegunRol` con rutas válidas para todos los cargos y validar que la ruta no esté vacía. | `login.fxml`, `UserImpl`, `SeguridadEncrip`, `UserLoginController` |
| 6 | Formularios sin controlador ni acción. | Implementar `fx:controller` y `onAction` para los flujos núcleo: ubicación, donante, triaje, análisis, inventario, solicitud y transfusión. | `src/main/resources/views/` |
| 7 | Flujo de ubicación incompleto. | Crear el controlador de `form_ubicacion.fxml`, poblar los ComboBox desde `UbicacionService` y eliminar el `idubi` hardcodeado de `DonanteController`. | `form_ubicacion.fxml`, `DonanteController`, `UbicacionImpl` |
| 8 | Servicios/DAO incompletos. | Implementar `InstitucionServices` y los métodos vacíos de `ReceptorImpl`; completar CRUD de donantes, donaciones, personal e inventario. | `service/`, `dao/impl/` |
| 9 | Dependencias y build. | Quitar `jbcrypt` duplicado en `pom.xml`; agregar `javafx-base` y `javafx-graphics` explícitamente; mover la configuración del `javafx-maven-plugin` a nivel de plugin; agregar Maven Wrapper. | `pom.xml` |
| 10 | Código muerto y de prueba en producción. | Eliminar `ProbandoDaoController`, `ControllerAuxiliar`, `Admin`, el constructor temporal de `Donante` y bloques comentados. | `controller/`, `model/` |

### P1 — Necesario para que sea mantenible

| Área | Refactor |
|---|---|
| **Manejo de errores** | Capturar `ValidacionException` en los controladores y mostrar `Alerta`; reemplazar `printStackTrace` por un logger (SLF4J). |
| **Inyección de dependencias** | Dejar de hacer `new Impl()` dentro de cada servicio; introducir una fábrica/Service Locator o inyección por constructor. |
| **Testing** | Agregar JUnit 5 + Mockito, configurar Surefire y cubrir servicios/DAOs críticos con tests. |
| **Duplicación** | Unificar `ControllerPrincipal`, `ControllerInventario` y `ControllerSecretaria` (tienen lógica casi idéntica). |
| **Datos reales** | Reemplazar gráficos, notificaciones y perfil hardcodeados por consultas a servicios/DAO. |
| **Tipos de datos** | Cambiar `float` por `BigDecimal` en cantidades médicas; unificar `LocalDate`/`LocalDateTime`/`Timestamp`; evitar `setObject` en fechas. |
| **Mapeo** | Reemplazar constructores largos y frágiles por mappers explícitos o `ResultSet` tipados. |
| **Sesión y seguridad** | Implementar logout real, limpiar `SessionManager` y validar rol/autorización en cada caso de uso. |
| **Convenciones** | Corregir typos (`dirreccionCompleta`, `hasearpassword`, `fechaExtracion`, `dateNaciemiento`, `TipoDonancion`, `chacas`) o documentarlos como deuda técnica. |
| **Transacciones** | Revisar la transacción manual de `TransfusionDaoImpl` y asegurar rollback ante errores. |

### P2 — Mejoras progresivas

- Empaquetado con `jpackage` o `jlink` para distribuir la app sin requerir Maven.
- i18n de mensajes y etiquetas.
- Validación visual de formularios y mensajes de error consistentes.
- Alertas de stock y vencimiento en tiempo real.
- Documentación del esquema en `docs/ESQUEMA_BD.md`.
- Revisar compatibilidad de la codificación UTF-8 en `init_data_dpa.sql` (el volcado actual tiene caracteres corruptos).
- Reactivar `FOREIGN_KEY_CHECKS` al final de los scripts de inicialización.

### Definición de “listo para funcionar”

- [ ] `mvn clean test` compila y pasa.
- [ ] `mvn clean javafx:run` abre el login desde la raíz del proyecto.
- [ ] Login funciona para todos los cargos con rutas válidas.
- [ ] Registrar donante → triaje → análisis → inventario funciona contra MySQL.
- [ ] Crear solicitud → ejecutar transfusión → actualizar inventario funciona.
- [ ] Los formularios tienen controlador y botones con acción.
- [ ] No hay credenciales ni contraseñas en texto plano en el repositorio.
- [ ] Los gráficos y notificaciones usan datos reales.
- [ ] El esquema se puede recrear desde cero con scripts versionados.

> Para un plan de ejecución detallado por fases y responsables, consulta `PLAN_ACCION.md`.

---

## Documentación relacionada

| Documento | Contenido |
|---|---|
| `DIAGNOSTICO_GENERAL.md` | Inventario completo de problemas del código, SQL, FXML y arquitectura. |
| `AUDITORIA_DIAGNOSTICO.md` | Confronta el diagnóstico con la realidad y separa bugs verificables de los condicionales al entorno. |
| `docs/AUDITORIA_BD_REAL.md` | Comparación entre el esquema real de MySQL y las consultas de los DAO. |
| `docs/esquema_bd_real.sql` | Volcado del esquema real de la base de datos (referencia, no importable directamente). |
| `PLAN_ACCION.md` | Plan de trabajo por fases para llevar el proyecto a funcional. |

> Los diagnósticos están fechados el **2026-10-07**. El código pudo haber cambiado desde entonces; verifica cada hallazgo antes de darlo por cierto.

---

## Convenciones del equipo

- **Idioma del código:** español para identificadores y mensajes; nombres técnicos (`Controller`, `Service`, `DAO`, `DTO`) en inglés.
- **Paquetes:** `com.donaway.<módulo>`.
- **Clases:** PascalCase (`RegistrarDonante`, `DonanteDao`).
- **Variables:** camelCase (`documentoId`, `fechaNacimiento`).
- **Casos de uso:** verbo en infinitivo + sustantivo (`RegistrarDonante`, `LoginUsuario`).
- **Commits:** Conventional Commits (`feat:`, `fix:`, `refactor:`, `test:`, `chore:`).
- **Ramas:** `feat/<descripcion>`, `fix/<descripcion>`, `refactor/<descripcion>`.
- **Regla de oro:** no se integra a `main` si no compila y no pasa las pruebas.

---

## Aviso

Este repositorio contiene datos y credenciales de ejemplo en archivos de configuración y scripts. **No lo uses en producción tal cual.** Antes de desplegarlo, completa las tareas P0 de la sección de refactorización, rota las credenciales y elimina cualquier secreto del historial de Git.
