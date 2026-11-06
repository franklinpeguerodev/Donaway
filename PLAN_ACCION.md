# PLAN DE ACCIÓN — Donaway-app

**Equipo:** 2 developers (tú + compañero)
**Objetivo:** Poner a funcionar el 100% de la app + sentar la base arquitectónica para crecer sin tener que re-refactorizar
**Stack:** Java 21, JavaFX 21, Maven, MySQL 8

---

## 0. Contexto y referencias

Antes de empezar, todo el equipo debe leer:

- `DIAGNOSTICO_GENERAL.md` — inventario completo de problemas.
- `AUDITORIA_DIAGNOSTICO.md` — qué hallazgos son reales y cuáles dependen del entorno.
- Este documento.

**Reglas del juego:**

1. Cada cambio va con commit + mensaje claro.
2. No se mergea a `main` si no compila (`mvn compile`) y pasa los tests (cuando los haya).
3. Una feature por branch. Nombre: `fix/<descripcion>` o `feat/<descripcion>`.
4. Antes de hacer un cambio "grande", comentar primero si conviene.

---

## Fase 0 — Setup (medio día)

| Tarea | Responsable | Tiempo |
|---|---|---|
| Crear rama `refactor/fase-0-setup` desde `main` | Tú | 5 min |
| Crear rama `refactor/fase-0-setup` desde `main` (la misma) | Compañero | 5 min |
| `mvn clean install -DskipTests` debe terminar sin errores | Ambos | 5 min |
| Crear `docs/CONVENCIONES.md` con: estilo Java (Eclipse actual), convención de nombres, formato de commits | Tú | 30 min |
| Borrar archivos muertos: `ProbandoDaoController.java`, `ControllerAuxiliar.java`, `Admin.java` | Tú | 10 min |
| Borrar bloque comentado al final de `ControllerInventarioTabla.java` (líneas 182-259) | Tú | 5 min |
| Compilar y commit "chore: limpieza inicial de archivos muertos" | Tú | 5 min |

**Entregable:** commit limpio en main, sin código muerto.

---

## Fase 1 — Hacer que el login funcione (1-2 días)

**Objetivo:** un usuario puede loguearse como bioanalista y ver la pantalla principal. Cualquier otro cargo abre su pantalla correspondiente o muestra error claro.

### 1.1 Backend del login (1 día)

| Tarea | Responsable | Archivo |
|---|---|---|
| Quitar dependencia `jbcrypt` duplicada | Tú | `pom.xml` |
| Cambiar `UserImpl.validarLogin` para usar `BCrypt.checkpw` | Tú | `dao/impl/user/UserImpl.java` |
| Agregar método `verificarPassword(plain, hash)` a `SeguridadEncrip` | Tú | `utils/security/SeguridadEncrip.java` |
| Quitar `.env` del repo y reemplazarlo por `.env.example` apuntando a MySQL | Tú | `.gitignore`, `.env` |
| Rotar las credenciales del `.env` (cambiar `admin`) | Tú | `.env` |

### 1.2 Frontend del login (medio día)

| Tarea | Responsable | Archivo |
|---|---|---|
| Cambiar `TextField` por `PasswordField` para password | Tú | `views/vistas_formularios/vistas_generales/login.fxml` |
| Renombrar `inpuntUser` a `inputDocumentoUsuario` (typo) | Tú | `login.fxml`, `UserLoginController.java` |
| Corregir el switch en `UserLoginController`: agregar TODOS los cargos del enum con paths válidos | Tú | `UserLoginController.java` líneas 77-83 |
| Cambiar `"ACTIVO".equalsIgnoreCase(sesion.estado().name())` por `sesion.estado() == EstadoPersonal.ACTIVO` | Tú | `UserLoginController.java` línea 46 |
| Cambiar `catch (IOException e)` por `catch (Exception e)` en `cargarVistaSegunRol` | Tú | `UserLoginController.java` líneas 95-98 |
| Agregar validación: si `rutaFxml` está vacía, mostrar alerta y no intentar cargar | Tú | `UserLoginController.java` línea 85 |

### 1.3 Corrección de enums con typos (medio día)

| Tarea | Responsable | Archivos |
|---|---|---|
| Renombrar `Cargo.RECEPSIONISTA` → `Cargo.RECEPCIONISTA` (incluye todos los `case` y valueOf) | Tú | `Cargo.java`, todos los call-sites |
| Renombrar `Departamento.RECEPSIONISTA` → `Departamento.RECEPCIONISTA` | Tú | `Departamento.java`, call-sites |
| Renombrar `TipoDonancion` → `TipoDonacion` (incluye archivos referenciados) | Tú | `TipoDonancion.java`, call-sites |
| Renombrar `donreccionCompleta` → `direccionCompleta` en `Institucion.java`, `PersonalMedico.java`, `Donante.java` y sus DAOs | Tú | 6 archivos |
| Renombrar `SeguridadEncrip.hasearpassword` → `encriptarPassword` (typo) | Tú | `SeguridadEncrip.java`, call-sites |
| Renombrar `Donacion.fechaExtracion` → `fechaExtraccion` | Tú | `Donacion.java`, call-sites |
| Renombrar `Time.dateTimeNow` → `getDateTimeNow` (no-static → static) | Tú | `Time.java`, call-sites |

**Pruebas de la Fase 1:**

- [ ] Login con bioanalista → ve `Plantilla_personal_medico.fxml`.
- [ ] Login con admin → ve `Plantilla_secretaria.fxml`.
- [ ] Login con gestor inventario → ve `Plantilla_personal_inventario.fxml`.
- [ ] Login con password incorrecta → muestra "Credenciales incorrectas".
- [ ] Password está oculta en pantalla.
- [ ] `mvn compile` sin warnings de SLF4J.

---

## Fase 2 — Patrón arquitectónico CasoDeUso + Resultado (1-2 días)

**Objetivo:** introducir el patrón con UN caso piloto (login) y validar la ergonomía antes de migrar el resto.

### 2.1 Crear el kernel (medio día) — Responsable: Tú

Crear 3 archivos nuevos:

```
src/main/java/com/donaway/kernel/
├── Resultado.java
├── TipoError.java
└── CasoDeUso.java
```

**`Resultado.java`**:

```java
package com.donaway.kernel;

public sealed interface Resultado<T> permits Resultado.Exito, Resultado.Fallo {

    record Exito<T>(T datos) implements Resultado<T> {}
    record Fallo(String mensaje, TipoError tipo) implements Resultado<T> {}

    static <T> Resultado<T> exito(T datos) {
        return new Exito<>(datos);
    }
    static <T> Resultado<T> fallo(String mensaje) {
        return new Fallo<>(mensaje, TipoError.GENERAL);
    }
    static <T> Resultado<T> fallo(String mensaje, TipoError tipo) {
        return new Fallo<>(mensaje, tipo);
    }

    default boolean esExito() { return this instanceof Exito<T>; }
    default T datos() { return ((Exito<T>) this).datos(); }
}
```

**`TipoError.java`**:

```java
package com.donaway.kernel;

public enum TipoError {
    GENERAL, VALIDACION, CONEXION,
    AUTENTICACION, AUTORIZACION, NO_ENCONTRADO, DUPLICADO
}
```

**`CasoDeUso.java`**:

```java
package com.donaway.kernel;

@FunctionalInterface
public interface CasoDeUso<I, O> {
    Resultado<O> ejecutar(I entrada);
}
```

### 2.2 Crear el caso piloto `LoginUsuario` (medio día) — Responsable: Tú

```
src/main/java/com/donaway/cuenta/casos/LoginUsuario.java
```

El caso de uso:

- Recibe `Entrada(documentoId, passwordPlano)`.
- Valida campos no vacíos → `Resultado.fallo(...)`.
- Llama al DAO → si falla SQL → `Resultado.fallo(..., TipoError.CONEXION)`.
- Si la BD devuelve null → `Resultado.fallo("Credenciales incorrectas", TipoError.AUTENTICACION)`.
- Si el estado no es `ACTIVO` → `Resultado.fallo("Usuario inactivo", TipoError.AUTORIZACION)`.
- Si todo OK → `Resultado.exito(new Sesion(...))`.
- Verifica el hash con `SeguridadEncrip.verificarPassword(plain, hash)`.

### 2.3 Adaptar `UserLoginController` al patrón (medio día) — Responsable: Tú

El controller ya no llama a DAOs ni lanza `Alerta.mostrar` condicional. Solo:

```java
Resultado<LoginUsuario.Sesion> resultado = loginUsuario.ejecutar(entrada);

if (resultado instanceof Resultado.Exito<LoginUsuario.Sesion> exito) {
    SessionManager.getInstance().setSesion(exito.datos());
    cargarVistaSegunRol(exito.datos().cargo());
    cerrarVentana();
} else {
    Resultado.Fallo fallo = (Resultado.Fallo) resultado;
    Alerta.mostrarAlerta(tituloPara(fallo.tipo()), fallo.mensaje());
}
```

### 2.4 Documentar el patrón (1 hora) — Responsable: Compañero

Crear `docs/PATRON_CASO_DE_USO.md` con:
- Por qué lo adoptamos (cita la conversación).
- Anatomía de un caso (Entradas, Salidas, Resultado, Errores tipados).
- Convenciones: nombres (`LoginUsuario`, `RegistrarDonante`), ubicación en paquetes (`com.donaway.<modulo>.casos`), tests.

### 2.5 Decisión go/no-go (15 min) — Ambos

Después de validar el login funcionando con el patrón, decidir:
- **GO**: seguir con Fase 3.
- **NO-GO**: documentar por qué no y parar el refactor (los fixes de Fase 1 ya están).

**Entregable:** commit + decisión documentada en `docs/DECISIONES.md`.

---

## Fase 3 — Migración gradual por módulo (1-2 semanas)

**Regla:** una acción a la vez, en el orden que la necesites funcional.

### 3.1 Módulo Donante (1 día) — Responsable: Tú

Crear los casos:
- `RegistrarDonante` (reemplaza `DonanteServices.procesarNuevoDonante`).
- `ActualizarDonante`.
- `BuscarDonantesPorSangre(TipoSangre, FactorRh)`.
- `ObtenerHistorialDonaciones(String documentoId)`.
- `ValidarAptitudParaDonar(String documentoId)`.

Eliminar `DonanteServices` (o mantenerlo como fachada si tiene sentido).

Adaptar `DonanteController` para usar los casos.

### 3.2 Módulo Personal Médico (1 día) — Responsable: Compañero

Crear los casos:
- `RegistrarPersonalMedico`.
- `ModificarPersonalMedico`.
- `BuscarPorInstitucion(String rnc)`.
- `BuscarPorJornada(Jornada)`.
- `ObtenerActivos`.
- `ValidarCredenciales(String email, String password)`.

Adaptar `PersonalMedicoController` cuando exista.

### 3.3 Módulo Institución (medio día) — Responsable: Tú

- `RegistrarInstitucion`.
- `ObtenerInstituciones`.
- `ObtenerPorRnc(String rnc)`.
- `ModificarInstitucion`.
- `GenerarCarnetInstitucional(String nombre)`.

`InstitucionServices` está vacío actualmente, así que crear desde cero.

### 3.4 Módulo Ubicación (1 día) — Responsable: Compañero

Es el módulo más fragmentado (Región, Provincia, Municipio, Distrito, Sección, Barrio, SubBarrio). Crear:

- `ListarRegiones`.
- `ListarProvinciasPorRegion(int idRegion)`.
- `ListarMunicipiosPorProvincia(int idProvincia)`.
- ... etc.
- `RegistrarUbicacion(Ubicacion u)`.

### 3.5 Módulo Donación / Triaje / Análisis (2-3 días) — Responsable: Ambos (dividir por submódulo)

Submódulo Triaje (Tú):
- `RegistrarTriaje(DatosTriaje)`.
- `ObtenerTriajePorDonacion(String idDonacion)`.
- `EsAptoParaDonar(String idDonacion)`.

Submódulo Análisis (Compañero):
- `RegistrarResultados(AnalisisLaboratorio)`.
- `EsSangreSegura(String idDonacion)`.
- `ObtenerAnalisisPorCodigoReferencia(String codigo)`.

Submódulo Donación (Ambos):
- `RegistrarDonacion(Donacion)`.
- `BuscarPorReferencia(String codigo)`.
- `ObtenerHistorialDonante(String documentoId)`.

### 3.6 Módulo Solicitud y Transfusión (1-2 días) — Responsable: Tú

- `CrearSolicitud(SolicitudTransfusion)`.
- `VerPendientesPorPrioridad(PrioridadSolicitud)`.
- `EjecutarTransfusion(Transfusion)` (con transacción SQL).
- `ActualizarEstadoSolicitud(String id, EstadoSolicitud)`.
- `ReporteTransfusiones(String rnc, LocalDate inicio, LocalDate fin)`.

### 3.7 Módulo Inventario (1 día) — Responsable: Compañero

- `IngresarAlInventario(UnidadHemoderivados)`.
- `ConsultarDisponibilidad(TipoComponente, TipoSangre, FactorRh)`.
- `AlertasVencimiento(int diasMargen)`.
- `CambiarEstado(String idUnidad, EstadoUnidad)`.

### 3.8 Módulo Ubicación dentro de donación (integración) — Responsable: Ambos

- `DonanteServices.registrarDonante` debe orquestar: crear ubicación → registrar donante → crear registro de triaje vacío → registrar donación.

Esto requiere coordinar DAO `Ubicacion` + DAO `Donante` + DAO `Donacion`. El caso de uso `RegistrarDonanteCompleto` orquesta.

---

## Fase 4 — Inyección de dependencias (1 día) — Responsable: Ambos

### 4.1 Crear un ServiceLocator simple

```java
package com.donaway.kernel;

public class Fabrica {
    private static final UserDao userDao = new UserImpl();
    private static final LoginUsuario loginUsuario = new LoginUsuario(userDao);
    // ...


    public static LoginUsuario loginUsuario() { return loginUsuario; }
}
```

### 4.2 Adaptar controllers

```java
public class UserLoginController {
    private final LoginUsuario loginUsuario = Fabrica.loginUsuario();
    // ...
}
```

---

## Fase 5 — Tests (1-2 semanas, continuo)

**Regla:** todo caso de uso nuevo se entrega CON su test.

### Estructura

```
src/test/java/com/donaway/<modulo>/casos/<CasoDeUso>Test.java
```

### Tests mínimos del kernel

- `ResultadoTest`: crear exito/fallo, verificar esExito() y datos().
- `CasoDeUsoTest`: alguna implementación de prueba.

### Tests de cada caso

- Test del camino feliz.
- Test de cada validación que falle → debe dar `Resultado.fallo` con el `TipoError` correcto.
- Test con DAO mockeado para errores de conexión.

---

## Fase 6 — Seguridad (1 día)

| Tarea | Responsable | Archivo |
|---|---|---|
| Quitar `.env` del control de versiones (`git rm --cached .env`) | Tú | `.gitignore` |
| Agregar `git filter-repo` para limpiar el historial si es necesario (recomendado) | Tú | repo |
| Cambiar contraseña de BD en uso | Tú | `.env` |
| Implementar logout real (botón con `onAction` que limpia `SessionManager`) | Tú | `ControllerPrincipal.java`, `ControladorCentroNotificaciones.java` |
| Validar que cada caso de uso verifica el rol del usuario (`Sesion.cargo()`) | Ambos | cada caso |

---

## Fase 7 — FXMLs y UI (continuo)

Por cada FXML que se agregue en una fase, debe tener `fx:controller` y `onAction` correctos.

### Lista de FXMLs sin controller que necesitan atención (priorizado):

| FXML | Responsable | Acción |
|---|---|---|
| `form_ubicacion.fxml` | Tú | Crear `FormUbicacionController.java` |
| `form_solicitud.fxml` (×2) | Tú | Crear controller |
| `registro_extraccion.fxml` | Compañero | Crear controller |
| `registro_resultado_lab.fxml` | Compañero | Crear controller |
| `registro_datos_triaje.fxml` | Tú | Crear controller |
| `form_datos_transfusion.fxml` | Tú | Crear controller |
| `form_buscar_solicitud.fxml` (×2) | Compañero | Crear controller |
| `form_busqueda_donacion.fxml` | Tú | Crear controller |
| `form_busqueda_donante.fxml` (×2) | Compañero | Crear controller |
| `form_busqueda_solicitudes.fxml` | Tú | Crear controller |
| `form_busqueda_donaciones.fxml` | Tú | Crear controller |
| `form_registro_institucional.fxml` | Tú | Crear controller |
| `form_unidad_hermoderivados.fxml` | Compañero | Crear controller |

### Cada controller nuevo debe:

- Inyectar el caso de uso via `Fabrica`.
- Validar campos con `Resultado<T>` del caso.
- Mostrar errores con `Alerta.mostrarAlerta`.
- No contener SQL ni acceso a DAOs directamente.

---

## Fase 8 — SQL (1-2 días)

| Tarea | Responsable | Archivo |
|---|---|---|
| Comparar `init_donaway.sql` con las queries de los DAOs | Tú | `init_donaway.sql` |
| Decidir un esquema final y aplicarlo (si difiere del init versionado) | Tú | migración SQL |
| Crear tabla `regiones` con datos básicos | Tú | migración SQL |
| Crear vista `v_ubicacion_completa` | Tú | migración SQL |
| Cambiar columnas UUID a `BINARY(16)` si decides usar funciones `UUID_TO_BIN` | Tú | migración SQL |
| Documentar el esquema en `docs/ESQUEMA_BD.md` | Tú | nuevo archivo |

---

## Resumen ejecutivo

| Fase | Qué | Quién | Duración estimada |
|---|---|---|---|
| 0 | Setup | Tú + Compañero | 0.5 día |
| 1 | Login funciona + typos | Tú | 1-2 días |
| 2 | Patrón CasoDeUso + login piloto | Tú | 1-2 días |
| 3 | Migración gradual de CRUDs | Tú + Compañero | 1-2 semanas |
| 4 | Inyección de dependencias | Ambos | 1 día |
| 5 | Tests | Ambos | continuo |
| 6 | Seguridad | Tú | 1 día |
| 7 | FXMLs | Ambos | continuo |
| 8 | SQL | Tú | 1-2 días |
| **TOTAL hasta app funcional** | — | — | **~3 semanas** |

---

## Convenciones que debe acordar el equipo

| Tema | Convención sugerida |
|---|---|
| Idioma del código | Español (identificadores, mensajes de error, comentarios de sección). Excepciones: nombre de patrones técnicos (`Controller`, `Service`, `DAO`, `DTO`, `Record`). |
| Nombre de packages | `com.donaway.<modulo>` para módulos, `com.donaway.kernel` para piezas reutilizables. |
| Nombre de clases | PascalCase en español: `LoginUsuario`, `RegistrarDonante`, `DonanteDao`. |
| Nombre de variables | camelCase en español: `documentoId`, `fechaNacimiento`. |
| Nombre de casos de uso | Verbo infinitivo + sustantivo: `LoginUsuario`, `RegistrarDonante`, `GenerarCodigoBarras`. |
| Tests | Mismo nombre + `Test` sufijo: `LoginUsuarioTest`. |
| Commits | Conventional Commits: `feat:`, `fix:`, `chore:`, `refactor:`, `test:`, `style:`. |
| Branches | `feat/login-password-field`, `fix/cargo-typo`, etc. |

---

## Lo que NO se hace (anti-objetivos)

- Reescribir todo de golpe. Migración incremental.
- CQRS completo con Bus/Mediator. Demasiado para esta app.
- Hexagonal/Ports & Adapters. Overkill para JavaFX desktop.
- Migración de framework (Spring/Quarkus). No aporta para este tamaño.
- Microservicios. Es una app de escritorio.

---

## Orden sugerido de ejecución

1. Crear el archivo `docs/PLAN_ACCION.md` (ya hecho — este documento).
2. Fase 0: limpieza inicial + ramas.
3. Fase 1: login funciona (este es el bloqueador más urgente).
4. Fase 2: patrón CasoDeUso + login piloto.
5. Continuar Fase 3 en adelante.

Si en cualquier momento te atascas, vuelve a consultarme con el contexto de qué fase estás haciendo.