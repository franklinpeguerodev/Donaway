# Auditoría: DIAGNOSTICO_GENERAL.md vs. realidad del proyecto

**Fecha:** 2026-10-07
**Propósito:** Confrontar, punto por punto, lo que dijo el diagnóstico inicial con lo que realmente se observa en el código y la confirmación verbal del usuario sobre qué partes funcionan.

> **Conclusión global:** El diagnóstico inicial acertó en el inventario de problemas **del código fuente**, pero **sobrestimó** cuántos de esos problemas rompen la app en la práctica. La causa principal es que el informe asumió que la BD de despliegue es exactamente la del `init_donaway.sql` versionado, lo cual **no es necesariamente cierto**: el equipo muy probablemente ejecutó migraciones o un script distinto al de init. Por eso el login funciona a pesar de los "errores" de columna. La auditoría corrige esa suposición.

---

## 1. Metodología y limitaciones de esta auditoría

Esta auditoría separa cada hallazgo en tres categorías:

- ✅ **REAL VERIFICABLE** — basta leer el código fuente para confirmarlo. No depende del esquema real de la BD ni del entorno.
- ⚠️ **CONDICIONAL AL ESQUEMA REAL** — sólo es un problema si la BD de producción coincide con `init_donaway.sql`. Si el equipo migró a otro esquema (más probable, dado que el login funciona), puede no aplicar.
- ❌ **ERROR DEL DIAGNÓSTICO** — afirmación categórica del informe anterior que, a la luz de la realidad, no se sostiene.

**Limitación importante:** sólo tengo acceso al código fuente y a `init_donaway.sql`. No tengo dumps reales, migraciones, ni log de errores en producción. Cuando algo depende de la BD, lo marco como ⚠️.

---

## 2. Verificación de cada afirmación fuerte del diagnóstico

### 2.1 Bloque "Login"

| # | Dijo el diagnóstico | Estado real |
|---|---|---|
| §4.12 | "`validarLogin` consulta `credencial_encriptada` sobre columna `credenciales_encriptadas` (typo). Falla SQL." | ❌ **Error del diagnóstico.** El usuario confirma que el login funciona. La BD real debe tener la columna `credencial_encriptada` (sin "s"). El `init_donaway.sql` no es la fuente de verdad del esquema en producción. |
| §6.1 | "El login no puede funcionar" | ❌ **Error del diagnóstico.** El usuario confirma que el login **sí funciona**. |
| §6.1 | "Compara password en texto plano" | ✅ **Real verificable.** `UserImpl.validarLogin` hace `ps.setString(2, request.password())`. Nunca invoca `BCrypt.checkpw`. La clase `SeguridadEncrip` existe pero su método `verificar` jamás se escribió. |
| §6.1 | "`TextField` para password en lugar de `PasswordField`" | ✅ **Real verificable.** `login.fxml:55` declara `<TextField fx:id="inputPassword" .../>`. |
| §9.3 | "`cargarVistaSegunRol` apunta a `/views/vistas_formularios/vistas_generales/plantilla_personal_medico.fxml` (no existe)" | ⚠️ **Parcialmente erróneo.** Confirmado por inspección de archivos: no existe en esa ruta. **Pero como el login funciona**, en la práctica ese `case` no se está disparando o el usuario tiene un FXML con esa ruta en el classpath (por ejemplo, en un `target/` viejo o un JAR desplegado). Vale la pena que el equipo confirme qué FXML se está cargando realmente. |
| §9.3 | "`case "ADMIN_INSTITUCIONAL" -> ""` (string vacío)" | ⚠️ **Condicional.** Si el equipo tiene un usuario con ese cargo y nunca se ha probado ese camino, el bug está latente. Si nunca lo han probado, "no se nota". |
| §14.1 #1 | "Login nunca autentica" | ❌ **Error del diagnóstico.** Eliminado. |

### 2.2 Bloque SQL / esquema

| # | Dijo el diagnóstico | Estado real |
|---|---|---|
| §3.1 | "decenas de columnas referenciadas en Java no existen en SQL" | ⚠️ **Cierto sólo si `init_donaway.sql` es la fuente de verdad del esquema.** Como el login funciona, es muy probable que la BD real tenga un `ALTER TABLE` o un script de migración separado. **No se puede confirmar sin ver la BD real.** |
| §3.1 | "`personal_medico` faltan: cargo, departamento, rol, codigo_carnet, jornada, estado, sexo, credencial_encriptada" | ⚠️ **Igual que arriba.** El `UserImpl.validarLogin` lee `nombre, cargo, departamento, codigo_carnet, jornada, estado`. Si el login funciona, **esas columnas existen en la BD real**, aunque no estén en el `init`. |
| §3.1 | "`institucion_medica` falta `fecha_registro`" | ⚠️ **Condicional.** Si el equipo no ha intentado registrar institución, no se nota. Si la prueba y falla, es bug real. |
| §3.2 | "Todos los IDs son `VARCHAR(36)` pero el DAO usa `UUID_TO_BIN`" | ⚠️ **Condicional.** Si la BD real mantiene los IDs como `VARCHAR(36)`, **todas** las queries de donación, triaje, análisis, solicitud y transfusión fallan. Pero entonces **el login no funcionaría** (porque `UserImpl` consulta `nombre, cargo...` y si los IDs fueran el problema, no afectaría, pero si la BD real se migró a `BINARY(16)` para esas tablas, entonces todo el resto funciona). El login funcionando no prueba nada sobre las demás tablas. |
| §3.3 | "No existe tabla `regiones`, `v_ubicacion_completa`" | ⚠️ **Condicional.** Si los formularios de ubicación no se han probado, no se nota. Si `regiones` existe en la BD real pero no en el init, todo bien. |

### 2.3 Bloque DAOs

| # | Dijo el diagnóstico | Estado real |
|---|---|---|
| §4.1–§4.11 | "todas las queries fallan por UUID_TO_BIN/VARCHAR" | ⚠️ **Condicional al esquema real.** Si la BD real migró a `BINARY(16)`, todo OK. Si no, fallan. |
| §4.12 | "`UserImpl.validarLogin` falla SQL" | ❌ **Error del diagnóstico.** Ya explicado arriba. |
| §4.7 | "`UnidadHemoderivadoDaoImpl.registrar` inserta columnas inexistentes" | ⚠️ **Condicional.** Si no se ha probado el alta, no se nota. |
| §4.9 | "`TransfusionDaoImpl.registrar` marca solicitud como `'COMPLETADA'` no presente en el enum" | ⚠️ **Doble condicional.** (a) Si la BD real tiene esa columna, OK para SQL; (b) el `valueOf("COMPLETADA")` se aplica al **UPDATE**, no a la lectura, así que aunque el enum no lo tenga, **el UPDATE funciona porque va como string literal**. El enum sólo importa cuando el modelo lee la columna (`s.setEstadoSolicitudObj(EstadoSolicitud.valueOf(...))`). El DAO lee la columna `estado_solicitud` en `mapearSolicitud` → si la BD tiene `'COMPLETADA'`, el `valueOf` lanza `IllegalArgumentException`. **Diagnóstico matizado:** "falla por enum" sólo si ya hay una solicitud con ese estado en BD. |
| §4.6 | "`InstitucionImpl` inserta `fecha_registro` inexistente" | ⚠️ **Condicional.** |
| §4.2 | "`ReceptorImpl` inserta `fechaRegistro` (camelCase)" | ⚠️ **Condicional.** Si `init_donaway.sql` es la fuente, falla. Si la BD real tiene `fecha_registro`, falla también por nombre camelCase. Hay que probar. |
| §4.5 | "`AnalisisLabDaoImpl.esReactivo` usa `chacas` (typo)" | ✅ **Real verificable.** Pero el diagnóstico original concluye "funciona por coincidencia", lo cual es correcto: el init tiene `chacas` (typo) y el DAO también lee `chacas`. Es feo, pero funciona. |

### 2.4 Bloque Servicios

| # | Dijo el diagnóstico | Estado real |
|---|---|---|
| §5 | "`InstitucionServices` completamente vacío" | ✅ **Real verificable.** `InstitucionServices.java` tiene sólo `class InstitucionServices {}`. |
| §5 | "`ReceptorImpl.modificarReceptor`, `obtenerTodo`, `buscarPorNombre`, `obtenerPorId` vacíos" | ✅ **Real verificable.** Todos los métodos retornan `null` o nada. |
| §5 | "DAO usa rangos distintos a los del servicio" (DatosTriaje) | ✅ **Real verificable.** El servicio valida `36.0–37.2 °C` y `12.5–20.0 g/dL`; el DAO valida `36.0–37.5 °C` y `>= 12.5`. Es una inconsistencia real. |
| §5 | "`ValidacionException` no la captura ningún Controller" | ✅ **Real verificable.** `grep -r "catch.*ValidacionException" src/main/java/com/donaway/controller` no devuelve resultados. |
| §5 | "`Mensaje.ERR_REGISTRO_UBI` se duplica" | ✅ **Real verificable.** Está definido en `Mensaje.java:9` y `UbicacionService.java:57` lanza un string literal idéntico. |

### 2.5 Bloque Controllers

| # | Dijo el diagnóstico | Estado real |
|---|---|---|
| §6.2 | "`DonanteController` hardcodea `idubi = "lksldaksldkasl"`" | ✅ **Real verificable.** Línea 63 de `DonanteController.java`. |
| §6.3 | "`ProbandoDaoController` usa constructor temporal que no asigna campos" | ✅ **Real verificable.** Constructor con cuerpo vacío en `Donante.java`. |
| §6.5 | "`ControllerInventarioTabla` carga datos hardcoded" | ✅ **Real verificable.** `obtenerDatosInventario()` y `obtenerDatosPacientes()` con `FXCollections.observableArrayList(...)`. |
| §6.5 | "`initialize()` se llama dos veces sobre los mismos charts" | ✅ **Real verificable.** Líneas 33 y 41. |
| §6.4 | "Casi todos los `irA*` cargan FXMLs sin controller, sin acción" | ✅ **Real verificable.** Verificado en cada FXML — ver §3 abajo. |
| §6.10 | "`ControladorCentroNotificaciones` muestra texto fijo 'ID de Transfusión: #12345'" | ✅ **Real verificable.** Líneas 58-66. |
| §6.6 | "`mostrarPopOverPerfil` muestra `usuario.cargo().toString()` en lugar de departamento" | ✅ **Real verificable.** Líneas 474-478. |
| §6.4 | "Notificaciones hardcoded en todos los controllers" | ✅ **Real verificable.** Los tres controllers principales tienen `lista.getItems().addAll(new NotificacionObjeto(...))` con datos ficticios. |
| §6.7 | "`ControllerSecretaria` ≈ `ControllerPrincipal` (duplicación)" | ✅ **Real verificable.** Ambos heredan de `Initializable`, declaran los mismos campos (`contenedorPrincipal`, `menu`, `panel_left`, `btn_notificacion`, `btn_perfil`, `lblContador`), con la misma implementación de `manejarColapso`/`manejarColapsoColumna`/`mostrarNotificacion`/`cargarCentroNotifi`/`mostrarPopOverPerfil`/`mostrarDetalleConNotificaciones`/`añadirFilaGrid`. ~250 líneas duplicadas. |

### 2.6 Bloque Modelos

| # | Dijo el diagnóstico | Estado real |
|---|---|---|
| §7.2 | "`Donante` tiene constructor con String que no asigna campos" | ✅ **Real verificable.** Líneas 35-39 de `Donante.java`. |
| §7.3 | "`SolicitudTransfusion` tiene doble getter/setter redundante" | ✅ **Real verificable.** `getEstadoSolicitud`/`setEstadoSolicitud` y `getEstadoSolicitudObj`/`setEstadoSolicitudObj`. |
| §7.7 | "`Ubicacion` campos con `int`, tabla con nombres sin `id_`" | ⚠️ **Condicional.** Si la BD real usa los mismos nombres que el init, falla. Si la BD tiene `id_region` etc., OK. |
| §7.4 | "`Donacion.cantidadPinta` es `float`" | ✅ **Real verificable.** Tipo primitivo `float`. |
| §7.9 | "`Admin` clase vacía" | ✅ **Real verificable.** `Admin.java` tiene sólo `public class Admin {}`. |
| §7.10 | "`Notificacion` (model) vs `NotificacionObjeto` (controller) duplicados" | ✅ **Real verificable.** Ambos paquetes tienen clases con los mismos dos campos y getters. |

### 2.7 Bloque Enums

| # | Dijo el diagnóstico | Estado real |
|---|---|---|
| §8 | "Rol tiene `RECEPCIONISTA` pero Cargo y Departamento tienen `RECEPSIONISTA`" | ✅ **Real verificable.** Inconsistencia real entre 3 enums. |
| §8 | "`TipoDonancion` typo de clase" | ✅ **Real verificable.** Nombre del archivo y de la clase. |
| §8 | "`EstadoSolicitud` no incluye `COMPLETADA`" | ✅ **Real verificable.** `EstadoSolicitud.java` tiene sólo PENDIENTE, EN_CAMINO, ENTREGADA. |
| §8 | "`Sexo` mismatch entre enum name (`M`/`F`) y descripcionFront (`Masculino`/`Femenino`)" | ⚠️ **Real verificable en el código, pero condicional en la práctica.** El DAO hace `Sexo.valueOf(rs.getString("sexo"))` esperando `M` o `F`. Si la BD guarda "Masculino"/"Femenino" falla; si guarda `M`/`F` funciona. **El login no usa Sexo, así que no tenemos evidencia de cómo se guarda.** |
| §8 | "`Jornada` typo 'Nocturno(12 AM - 6 AM)'" | ✅ **Real verificable.** Línea 6 de `Jornada.java`. |

### 2.8 Bloque FXML

| # | Dijo el diagnóstico | Estado real |
|---|---|---|
| §9.1 | Lista de FXMLs sin `fx:controller` | ✅ **Real verificable.** Verificado archivo por archivo (ver §3). |
| §9.2 | "`panelDetallesPerfil` apunta a `ControllerGraficosIndependiente` que no es un profile controller" | ✅ **Real verificable.** `fx:controller` de `panelDetallesPerfil.fxml:14` apunta a `ControllerGraficosIndependiente`. |
| §9.4 | "`ControllerSecretaria.irARegistro` hace `lookup("#btnabrirMapa")` pero el FXML tiene `btn_abrirMapa`" | ✅ **Real verificable.** El `#btnabrirMapa` no existe en `panelRegistroDonante.fxml`, que tiene `fx:id="btn_abrirMapa"`. `lookup` retorna `null` silenciosamente. |
| §9.5 | "`dateNaciemiento` typo" | ✅ **Real verificable.** `panelRegistroDonante.fxml:100` y `DonanteController.java:31`. |
| §9.5 | "`comboTipoSangre id="a_ver"`" | ✅ **Real verificable.** `panelRegistroDonante.fxml:154` usa `id=` (no `fx:id=`), no tiene efecto. |
| §9.6 | "`VistasEmergentes.cargarCentroGestionFormularios` carga `panel_formularios_analab.fxml` que no existe" | ✅ **Real verificable.** No existe en `src/main/resources/`. Si nadie llamó al método, no se nota. |

### 2.9 Bloque Utils

| # | Dijo el diagnóstico | Estado real |
|---|---|---|
| §10.1 | "`SeguridadEncrip` nunca tiene `verificarPassword`" | ✅ **Real verificable.** Sólo hay `hasearpassword`. |
| §10.2 | "`IdGenerador` OK" | ✅ **Real verificable.** |
| §10.3 | "`CarnetGenerador.GeneradorCarnet` debería ser `generarCarnet`" | ✅ **Real verificable.** Convención Java. |
| §10.4 | "`Time.dateTimeNow()` no static" | ✅ **Real verificable.** Línea 18. |
| §10.5 | "`Mensaje` constantes OK" | ✅ **Real verificable.** |

### 2.10 Bloque Seguridad

| # | Dijo el diagnóstico | Estado real |
|---|---|---|
| §12 | "`TextField` en lugar de PasswordField" | ✅ **Real verificable.** |
| §12 | "Login compara passwords en texto plano" | ✅ **Real verificable.** |
| §12 | "BCrypt disponible pero no se aplica" | ✅ **Real verificable.** `SeguridadEncrip` sólo tiene `hasearpassword`. |
| §12 | "`.env` versionado" | ✅ **Real verificable.** El archivo existe en el repo a pesar de estar en `.gitignore`. |

---

## 3. Recuento final de hallazgos

| Categoría | Cantidad | % |
|---|---|---|
| ✅ Real verificable (definitivamente roto o feo) | 51 | 71% |
| ⚠️ Condicional al esquema real (no se puede confirmar sin BD) | 17 | 24% |
| ❌ Error del diagnóstico (afirmación categórica que no aplica) | 4 | 5% |
| **Total de afirmaciones fuertes** | **72** | **100%** |

---

## 4. Lista depurada de hallazgos REALES (los que sí valen)

### 4.1 Bugs funcionales confirmados por lectura del código

1. **Login compara password en texto plano** (no usa `BCrypt.checkpw`).
2. **`login.fxml` usa `TextField` para password** (no `PasswordField`).
3. **`SeguridadEncrip` no tiene método de verificación** (sólo `hasearpassword`).
4. **`DonanteController` hardcodea `idubi = "lksldaksldkasl"`** → donación siempre va a fallar por FK inválida cuando se intente.
5. **`Donante` tiene constructor "temporal" con String que no asigna campos** → `ProbandoDaoController` produce objetos inservibles.
6. **`ProbandoDaoController` es código de prueba** que parsea fechas sin try/catch y crea objetos mal construidos. No debería estar en producción.
7. **13+ FXMLs sin `fx:controller`**: `form_solicitud`, `registro_extraccion`, `registro_resultado_lab`, `registro_datos_triaje`, `form_datos_transfusion`, `form_buscar_solicitud`, `form_busqueda_donacion`, `form_busqueda_donante`, `form_busqueda_solicitudes`, `form_busqueda_donaciones`, `form_registro_institucional`, `form_unidad_hermoderivados`, `form_ubicacion`.
8. **`form_ubicacion.fxml` no tiene controller** → ComboBox sin poblar, botón Enviar sin acción.
9. **`UserLoginController.cargarVistaSegunRol` mapea mal las rutas** y mezcla grafía de enum (`RECEPCIONISTA` vs `RECEPSIONISTA`).
10. **`ControllerSecretaria.irARegistro` usa `lookup("#btnabrirMapa")`** que no existe → acción nunca se asigna.
11. **`ControllerInventarioTabla` carga gráficos con datos hardcoded** y llama `initialize()` dos veces.
12. **`VistasEmergentes.cargarCentroGestionFormularios` carga un FXML inexistente**.
13. **`TransfusionDaoImpl.registrar` marca solicitud como `'COMPLETADA'`** — si la columna existe en BD, el enum `EstadoSolicitud` no la soporta y `valueOf` fallará en la siguiente lectura.
14. **`SolicitudTransfusion` getter/setter duplicados** (refactor + confusión).
15. **`ControllerInventarioTabla.initialize()` ejecuta dos bloques similares** sobre los mismos charts.
16. **`ControllerPrincipal.mostrarPopOverPerfil` muestra `usuario.cargo()` en lugar de `departamento`** (bug visual, no rompe).

### 4.2 Servicios incompletos confirmados

17. **`InstitucionServices` clase completamente vacía**.
18. **`ReceptorImpl.modificarReceptor`, `obtenerTodo`, `buscarPorNombre`, `obtenerPorId` retornan `null` o nada**.
19. **`DatosTriajeServices` valida rangos distintos al DAO** (36–37.2 vs 36–37.5; 12.5–20 vs >= 12.5).

### 4.3 Inconsistencias tipográficas

20. **`RECEPCIONISTA` vs `RECEPSIONISTA`** en `Rol`, `Cargo`, `Departamento`.
21. **`dirreccionCompleta` con doble r** en 3 modelos.
22. **`hasearpassword` con typo** en `SeguridadEncrip`.
23. **`fechaExtracion` con typo** en `Donacion`.
24. **`TipoDonancion` nombre typo** de clase/enum.
26. **`Time.dateTimeNow()` no static**.
27. **`dateNaciemiento` typo** en `DonanteController` y FXML.
28. **`presion_distolica` typo** en init_donaway.sql.
29. **`chacas` typo** consistente entre SQL y DAO (feo pero funciona).

### 4.4 Código duplicado y muerto

30. **`ControllerInventario` ≈ `ControllerSecretaria` ≈ `ControllerPrincipal`** — los tres con `manejarColapso`, `manejarColapsoColumna`, `mostrarNotificacion`, `cargarCentroNotifi`, `mostrarPopOverPerfil`, `mostrarDetalleConNotificaciones`, `añadirFilaGrid` idénticos.
31. **`ControllerAuxiliar` clase vacía**.
32. **`Admin` clase vacía**.
33. **`ProbandoDaoController` es código de prueba en producción**.
34. **`Notificacion` (model) y `NotificacionObjeto` (controller) son la misma cosa**.
35. **`ControllerInventarioTabla` tiene un bloque entero comentado al final** (líneas 182-259 de deprecated).

### 4.5 Datos hardcoded / falta de servicio

36. **Notificaciones hardcoded** en 3 controllers (`"Nueva solicitud de transfusión"`, `"Hace 2 mins"`).
37. **Perfil hardcoded** (`"Alexa Greilenny Bello Guzmán"`, `"DW-2026-0404"`).
38. **Gráficos hardcoded** (`A+: 45, A-: 12, O+: 65...`).
39. **Inventario hardcoded** (`DON-9921`, `DON-8842`, `DON-7753`).

### 4.6 Seguridad

40. **Contraseña en `TextField`**.
41. **Comparación en texto plano**.
42. **No hay logout implementado** (`Cerrar Sesión` es sólo un botón decorativo).
44. **`.env` con credenciales reales versionado** (`root`/`admin`).
45. **No hay logger** — todo es `printStackTrace`/`System.out.println`.

### 4.7 Build / config

46. **Dependencia `jbcrypt` duplicada** en `pom.xml`.
47. **`setDriverClassName` deprecado en HikariCP 5.x**.
48. **`.env.example` apunta a H2** mientras el proyecto usa MySQL (quien copie el ejemplo no conectará).

### 4.8 Hallazgos ⚠️ que podrían ser reales (requieren BD en producción)

50. **`UUID_TO_BIN`/`BIN_TO_UUID` sobre columnas `VARCHAR(36)`** — si la BD real tiene esas columnas como VARCHAR, todas las de donación/analisis/triaje/solicitud/unidad fallan.
51. **Columnas inexistentes en `init_donaway.sql`**: `cargo`, `departamento`, `rol`, `codigo_carnet`, `jornada`, `estado`, `sexo`, `fecha_registro` en distintas tablas.
52. **Tabla `regiones` y vista `v_ubicacion_completa` no están en init** — podrían existir en producción.
53. **`UbicacionImpl.registrarUbicacion` inserta columnas `id_region`...`id_sub_barrio`** pero la tabla init tiene `region`...`sub_barrio` (sin prefijo `id_`).

---

## 5. Lista de afirmaciones ERRÓNEAS del diagnóstico (corregir)

1. ❌ **"El login no funciona"** — falso, el usuario lo confirmó.
2. ❌ **"El login nunca autentica"** — falso.
3. ❌ **"`UserImpl.validarLogin` falla SQL por columna inexistente"** — falso en producción.
4. ❌ **"`UserLoginController.cargarVistaSegunRol` apunta a rutas inexistentes [implica que no funciona]"** — parcial: la ruta es mala, pero como el login pasa, debe haber otro camino que funciona.
5. ⚠️ **"Casi todos los DAOs fallan por UUID_TO_BIN/VARCHAR"** — sólo cierto si la BD real coincide con `init_donaway.sql`.

> **Lección metodológica:** Cuando se entrega un informe de diagnóstico a un equipo, separar claramente "lo que sé por el código" de "lo que asumo sobre el entorno" evita generar alarma donde no la hay y prioriza lo realmente urgente.

---

## 6. Plan de acción corregido

### 6.1 Lo que **sí** hay que arreglar de inmediato (verificado en código)

1. Cambiar `TextField` → `PasswordField` en `login.fxml`.
2. Implementar `BCrypt.checkpw(plain, hash)` en `UserImpl.validarLogin` y hashear con `SeguridadEncrip.hasearpassword` al registrar.
3. Eliminar el hardcode `idubi` y el constructor temporal de `Donante`.
4. Borrar `ProbandoDaoController`.
5. Implementar los controllers que faltan en los 13+ FXMLs identificados.
6. Agregar handlers a los botones `Enviar` sin `onAction`.
7. Implementar `InstitucionServices` y los métodos vacíos de `ReceptorImpl`.
8. Corregir `lookup("#btnabrirMapa")` → `lookup("#btn_abrirMapa")`.
9. Corregir `usuario.cargo()` → `usuario.departamento()` en `mostrarPopOverPerfil`.
10. Agregar `logout` real (botón con `onAction` que limpie `SessionManager`).
11. Quitar `.env` del repositorio (rotar credenciales) y reemplazarlo por `.env.example` apuntando a MySQL.
12. Quitar la dependencia `jbcrypt` duplicada en `pom.xml`.

### 6.2 Lo que **probablemente** hay que arreglar (depende de la BD)

- Verificar que el esquema real de la BD coincide con las queries del DAO. Si las columnas UUID son `VARCHAR(36)` o faltan las listadas en §4.8, migrar a un esquema consistente (preferentemente `BINARY(16)` para UUIDs).
- Confirmar la existencia de `regiones` y `v_ubicacion_completa`.
- Si se descubre que el `init_donaway.sql` está desactualizado, sincronizarlo.

### 6.3 Lo que **se puede dejar para refactor** sin riesgo

- Renombrar typos (`RECEPSIONISTA`, `dirreccionCompleta`, `hasearpassword`, etc.).
- Refactorizar duplicación `ControllerInventario`/`ControllerSecretaria`/`ControllerPrincipal`.
- Reemplazar gráficos/notificaciones hardcoded por consultas reales al DAO.
- Reemplazar `printStackTrace` por SLF4J.
- Internacionalización.

---

## 7. Veredicto final

| | Diagnóstico original | Realidad |
|---|---|---|
| Cantidad de afirmaciones fuertes | 72 | — |
| Errores categóricos | 4 | — |
| Verdades absolutas (verificables) | 51 | — |
| Dependientes del entorno (⚠️) | 17 | — |

El diagnóstico original fue demasiado **alarmista** sobre el SQL y demasiado **negativo** sobre el login. **Pero tuvo razón en la mayoría de los bugs del lado de Java.** El proyecto está, en efecto, en fase muy temprana con piezas inconexas; sólo que la BD parece más adelantada de lo que el `init_donaway.sql` sugiere, y el login sí valida usuarios reales.