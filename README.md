# AlbaLab Mobile

Aplicación Android desarrollada en **Kotlin y Jetpack Compose** como propuesta digital para **AlbaLab**, en el contexto de la asignatura **Desarrollo de Aplicaciones Móviles (DSY1105)**.

El proyecto integra catálogo de productos, carrito de compras, autenticación, administración, donaciones, colaboradores, solicitudes de contacto y pagos mediante **Webpay Plus**, utilizando una arquitectura **MVVM**, persistencia local **Room**, consumo de APIs externas mediante **Retrofit** y un backend propio de **microservicios Spring Boot**.

> **Alcance:** proyecto académico. Los pagos y servicios remotos requieren un backend disponible y configurado; la integración de Webpay debe utilizarse con el entorno autorizado de pruebas.

## Integrantes

- **Matias Acevedo**
- **Felipe Marchant**

## Repositorio

- **Aplicación Android:** <https://github.com/Matiiki/AlbaLab-Mobile>
- **Backend:** proyecto separado `albalab-backend` (agregar aquí el enlace público definitivo del repositorio).

## Tecnologías

| Componente | Tecnologías |
| --- | --- |
| Aplicación móvil | Kotlin, Android Studio, Jetpack Compose, Material 3 |
| Arquitectura | MVVM, ViewModel, Repository, StateFlow, corrutinas |
| Red | Retrofit, OkHttp, Gson, HTTP/REST, JSON |
| Datos locales | Room (SQLite) |
| Backend | Java, Spring Boot, Spring Cloud Gateway, Spring Data JPA, MySQL |
| Seguridad y recursos nativos | Autenticación con token JWT, BiometricPrompt, cámara y FileProvider |
| Pagos | Webpay Plus mediante `webpay-service` |
| Pruebas | JUnit y pruebas de ViewModel en Android; JUnit/Mockito y pruebas de controladores y servicios en backend |
| Colaboración | Git, GitHub y Trello |

## Funcionalidades de la aplicación

### Catálogo, carrito y compras

- Consulta y visualización de productos provenientes del backend.
- Carrito con incremento y disminución de cantidades, eliminación de productos y cálculo de subtotales y total.
- Resumen de compra con datos del comprador y consulta de cotización aproximada en USD mediante una API externa.
- Inicio del pago mediante Webpay, confirmación del resultado desde el backend y pantalla de confirmación cuando corresponde.
- Historial de compras asociado al usuario mediante `historial-service`, con apoyo de persistencia local Room.
- Estados de carga, mensajes de error y animaciones de interfaz.

**Flujo de compra:** `Productos → Carrito → Resumen de compra → Webpay → Confirmación`.

> La aplicación **no considera aprobado un pago solo por el retorno del navegador**: `WebpayViewModel` solicita la confirmación al backend y evalúa el estado del pago.

### Donaciones y colaboradores

- Formulario de donación con validaciones, monto, nombre, fecha y opción de anonimato.
- Captura opcional de fotografía y confirmación biométrica cuando corresponde.
- Flujo de pago de donaciones por Webpay; el registro remoto de la colaboración se coordina tras una confirmación de pago aprobada.
- Consulta de colaboraciones del usuario autenticado y listado público de colaboradores desde el backend.
- Persistencia local Room para los registros definidos en el proyecto.

### Contacto

- Formulario con nombre, motivo, cantidad de participantes, fecha, lugar, aceptación de términos y opción de seguimiento.
- Validaciones en ViewModel.
- Almacenamiento local mediante Room.

### Autenticación y perfil

- Registro e inicio de sesión de usuarios mediante el backend.
- Perfil y cierre de sesión.
- Gestión de estados de sesión y respuesta ante errores de autenticación.

### Administración

- Acceso de administración mediante autenticación específica.
- Panel administrativo con **CRUD de productos**: crear, consultar, actualizar y eliminar.
- Gestión de administradores mediante endpoints del servicio de administración.
- Secciones para administrar contenido de la aplicación.

### Recursos visuales y nativos

- Navegación entre pantallas con animaciones Compose.
- Cámara del dispositivo, permisos en tiempo de ejecución y `FileProvider`.
- Biometría mediante `BiometricPrompt`, cuando el dispositivo y el flujo lo permiten.

## Arquitectura

La aplicación utiliza la separación de responsabilidades **MVVM**.

```text
UI / Jetpack Compose (Screen)
          ↓ eventos y estado
ViewModel
          ↓ operaciones
Repository / cliente Retrofit / DAO
          ├── API externa (Retrofit)
          ├── API Gateway → microservicios Spring Boot → MySQL
          └── Room (SQLite local)
```

**Paquetes principales** bajo `app/src/main/java/com/example/albalab/`:

```text
com.example.albalab/
├── MainActivity.kt
├── ui/
│   ├── App.kt
│   ├── components/           # Barra superior, drawer y componentes reutilizables
│   ├── screens/              # Inicio, productos, carrito, Webpay, donaciones, etc.
│   └── theme/
├── viewmodel/                # Estados de aplicación, compras, usuarios y administración
├── data/
│   ├── local/                # Room: entidades, DAO y base de datos
│   └── repository/           # Repositorios locales y remotos
├── model/                    # Modelos y DTO
└── network/                  # Interfaces Retrofit y configuración de clientes
```

## Persistencia local y remota

La base de datos local Room se denomina `albalab_database` y contiene entidades relacionadas con **colaboradores, donaciones, contactos y compras**. El proyecto incluye migraciones de esquema.

Los datos remotos de **usuarios, productos, administradores, colaboradores, transacciones e historial** se gestionan en el backend mediante Spring Data JPA y MySQL. No deben confundirse los datos locales de Room con las operaciones CRUD remotas.

## Backend de microservicios

El backend se distribuye en proyectos Spring Boot independientes:

| Servicio | Puerto predeterminado | Responsabilidad |
| --- | ---: | --- |
| `api-gateway` | `9090` | Entrada principal y enrutamiento de solicitudes |
| `usuario-service` | `9091` | Registro, login, usuarios y notificaciones |
| `admin-service` | `9092` | Autenticación y administración |
| `colaborador-service` | `9093` | Colaboraciones y listados públicos/privados |
| `webpay-service` | `9094` | Creación, confirmación, consulta y reembolso de pagos |
| `product-service` | `9095` | CRUD de productos |
| `historial-service` | `9096` | Registro y consulta del historial de compras |

Los servicios requieren su configuración correspondiente y, cuando aplica, bases de datos MySQL activas. Los puertos pueden cambiarse mediante variables de entorno.

### Endpoints principales

| Método | Ruta | Función |
| --- | --- | --- |
| `POST` | `/api/auth/registro` | Registrar usuario |
| `POST` | `/api/auth/login` | Autenticar usuario |
| `GET` | `/api/productos` | Consultar productos |
| `POST` | `/api/productos` | Crear producto (administración) |
| `PUT` | `/api/productos/{id}` | Modificar producto (administración) |
| `DELETE` | `/api/productos/{id}` | Eliminar producto (administración) |
| `POST` | `/api/admin/auth/login` | Inicio de sesión de administrador |
| `GET / POST / PUT / DELETE` | `/api/admin` y `/api/admin/{id}` | Gestión de administradores |
| `GET` | `/api/colaboradores/publicos` | Consultar colaboradores públicos |
| `GET` | `/api/colaboradores/mios` | Consultar colaboraciones propias |
| `POST` | `/api/colaboradores` | Registrar colaboración |
| `POST` | `/api/webpay/crear` | Crear transacción |
| `POST` | `/api/webpay/confirmar` | Confirmar transacción |
| `GET` | `/api/webpay/estado/{token}` | Consultar estado del pago |
| `POST` | `/api/webpay/reembolsar` | Solicitar reembolso desde el backend |
| `POST` | `/api/historial/compras` | Registrar compra |
| `GET` | `/api/historial/compras` | Consultar historial de compras |
| `POST` | `/api/usuarios/notificaciones/email` | Solicitar notificación por correo |

Las rutas pasan por el **Gateway `9090`** cuando se utilizan las interfaces estándar de la app. En el código revisado, el cliente de administración también define acceso directo a **`admin-service` en el puerto `9092`**. Algunas rutas requieren autenticación y autorización.

Los contratos completos, DTO y validaciones deben consultarse en las interfaces Retrofit y controladores del backend.

## Consumo de API externa con Retrofit

Además de los microservicios propios, la aplicación consume una API pública de **tipos de cambio**:

- **Proveedor:** ExchangeRate-API, endpoint público `open.er-api.com`.
- **Solicitud utilizada:** `GET https://open.er-api.com/v6/latest/CLP`.
- **Archivos:** `network/ExchangeRateApi.kt`, `data/repository/MonedaRepository.kt` y `viewmodel/ResumenCompraViewModel.kt`.
- **Uso visual:** mostrar una referencia aproximada del total de compra en dólares estadounidenses (USD).

El proyecto también incluye clientes para validación de correo/dominio y carga de imágenes; algunas integraciones son opcionales y requieren credenciales o configuración adicional. No es necesario activarlas para demostrar la API externa de cotización.

## Requisitos para ejecutar el proyecto

**Android:**

- Android Studio y Android SDK instalados.
- Emulador o teléfono Android (el proyecto define `minSdk = 24`).
- Conexión a Internet para APIs externas y servicios remotos.

**Backend:**

- JDK 21 o compatible con los proyectos Spring Boot.
- MySQL configurado y disponible.
- Variables de entorno y configuración de cada servicio revisadas.
- Maven Wrapper incluido en cada microservicio.

### 1. Clonar y abrir la aplicación

```powershell
git clone https://github.com/Matiiki/AlbaLab-Mobile.git
```

Abre la carpeta clonada en Android Studio, espera **Gradle Sync**, selecciona un dispositivo y ejecuta **Run**.

### 2. Iniciar el backend

Desde el proyecto de backend, asegúrate de iniciar los servicios necesarios: `usuario-service`, `admin-service`, `colaborador-service`, `product-service`, `webpay-service`, `historial-service` y `api-gateway`.

Ejemplo en **una terminal de PowerShell** (repite en terminales separadas para cada servicio):

```powershell
cd usuario-service
.\mvnw.cmd spring-boot:run -s ..\configuracion-maven\settings.xml
```

> **Atención:** en la versión revisada, `start-all.bat` no inicia todos los microservicios. Además, antes de utilizar scripts de inicio o compartir el backend, se deben retirar las credenciales incrustadas y usar variables de entorno.

### 3. Configurar la IP del backend en Android

Archivo: `app/src/main/java/com/example/albalab/network/RetrofitClient.kt`.

La versión revisada utiliza una variable `HOST` con una IP local. Ajustarla al entorno:

```kotlin
// Ejemplo: emulador Android Studio con backend en el mismo PC
private const val HOST = "10.0.2.2"

const val BASE_URL = "http://$HOST:9090/"
const val ADMIN_BASE_URL = "http://$HOST:9092/"
```

En un **teléfono físico**, utiliza la IPv4 accesible del computador donde corre el backend y verifica que ambos dispositivos estén en la misma red y los puertos no estén bloqueados por el firewall.

El hostname del emulador `10.0.2.2` **no se utiliza para conectar teléfonos físicos**.

## Pruebas automatizadas

La app incluye pruebas unitarias en `app/src/test/`, entre ellas pruebas para `AppViewModel`, `AuthViewModel`, `ProductoViewModel`, `AdminViewModel`, `AdminProductoViewModel`, `FormularioViewModel2`, `ResumenCompraViewModel`, `WebpayViewModel` y componentes de red.

**Ejecutar las pruebas locales de Android** desde la raíz del repositorio:

```powershell
.\gradlew.bat testDebugUnitTest
```

**Ejecutar pruebas de un servicio backend** desde la raíz del repositorio backend:

```powershell
cd usuario-service
.\mvnw.cmd test -s ..\configuracion-maven\settings.xml
```

Los demás servicios se prueban de forma análoga. El backend incluye pruebas con JUnit y Mockito, y configuración de cobertura JaCoCo en varios módulos.

> **Cobertura:** la pauta de evaluación exige al menos **80% de la lógica**. La existencia de pruebas no acredita ese porcentaje. **Pendiente adjuntar reporte de cobertura y registrar el porcentaje real medido** tras ejecutarlas correctamente.

## APK `release` firmado

La aplicación cuenta con un **APK `release` generado, instalado y probado en emulador**. La firma se creó con una llave de tipo JKS y se verificó con `apksigner`.

- **APK:** [`app/release/app-release.apk`](app/release/app-release.apk)
- **Application ID:** `com.example.albalab`
- **Build variant:** `release`
- **Alias de la llave:** `albalab`
- **Firma verificada:** `Verifies`
- **Esquema de firma APK V2:** `true`
- **Algoritmo de clave:** RSA, 2048 bits
- **Cantidad de firmantes:** 1

### Procedimiento utilizado

1. Abrir el proyecto en Android Studio.
2. Seleccionar **Build → Generate Signed App Bundle / APK → APK**.
3. Crear o seleccionar un almacén de claves JKS y configurar alias y contraseñas.
4. Seleccionar el tipo de compilación **`release`** y generar el APK.
5. Instalar el APK generado en un emulador Android y verificar su funcionamiento.
6. Ejecutar `apksigner verify --verbose --print-certs` sobre el APK y comprobar el resultado `Verifies` y firma V2 válida.

Ejemplo de verificación (ajustar ruta de Android SDK según el equipo):

```powershell
& "G:\Android\Sdk\build-tools\36.0.0\apksigner.bat" verify --verbose --print-certs ".\app\release\app-release.apk"
```

### Evidencias

- **Captura de verificación de firma:** [Ver evidencia](Evidencias/evidencia-firma-apk.png).
- **APK instalable:** [Descargar desde el repositorio](app/release/app-release.apk).
- **Captura del APK funcionando en emulador:** pendiente de agregar a `Evidencias/`.
- **Captura que acredite la existencia del archivo `.jks`:** pendiente de agregar, sin mostrar datos sensibles.

> **Seguridad de la firma:** el archivo `albalab-release.jks` y sus contraseñas se mantienen fuera del repositorio público. El docente debe indicar un canal **privado y autorizado** para la entrega de la llave, solicitada por la rúbrica. No publicar llaves privadas, contraseñas ni tokens en el README o en GitHub.

## Trabajo colaborativo: GitHub y Trello

- **Control de versiones:** historial de commits en [GitHub](https://github.com/Matiiki/AlbaLab-Mobile/commits/main).

## Estado de la entrega (Evaluación Parcial 3)

| Criterio | Evidencia disponible | Verificación pendiente |
| --- | --- | --- |
| Pantallas y formularios | Código Compose y ViewModels | Demostración completa sin errores |
| Microservicios Spring Boot | Código de servicios, controladores y persistencia | Arranque de todos los servicios y conexión MySQL |
| Integración y CRUD | Interfaces Retrofit y funcionalidades administrativas | Ejecutar flujos completos en vivo |
| API externa | API de tipos de cambio con Retrofit | Demostrar consulta en pantalla |
| Pruebas unitarias | Pruebas Android y backend | Ejecución exitosa y cobertura real ≥80% |
| APK firmado | APK `release`, ejecución en emulador y verificación V2 | Adjuntar capturas complementarias y acordar entrega privada de `.jks` |
| GitHub y Trello | Repositorio público | Evidencia de commits por integrante y tablero Trello |

## Consideraciones de seguridad y uso

- No publicar contraseñas, claves JKS, JWT secrets, API keys ni tokens de pago.
- Las IP locales y variables de entorno deben adaptarse al equipo donde se ejecute el proyecto.
- Las funcionalidades remotas dependen de que los servicios estén disponibles.
- Este proyecto es una **demostración académica**; no equivale a la publicación comercial de una aplicación de pagos.

---

**Proyecto académico — Desarrollo de Aplicaciones Móviles (DSY1105).**
