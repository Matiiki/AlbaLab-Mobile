# AlbaLab Mobile

Aplicación móvil desarrollada en **Android Studio** utilizando **Kotlin** y **Jetpack Compose**.

El proyecto busca representar una solución móvil para **Alba Lab**, permitiendo gestionar productos, donaciones, colaboradores, solicitudes de contacto, autenticación de usuarios y un flujo de compra con integración a Webpay.

---

## Integrantes

- **Matias Acevedo**
- **Felipe Marchant**

---

## Tecnologías utilizadas

- Kotlin
- Android Studio
- Jetpack Compose
- Material 3
- MVVM
- Room
- Retrofit
- Coroutines
- StateFlow
- BiometricPrompt
- Cámara del dispositivo
- Webpay mediante backend
- Git y GitHub

---

## Funcionalidades principales

### Inicio

Pantalla principal de la aplicación desde donde el usuario puede acceder a las distintas secciones de AlbaLab.

### Productos

Permite visualizar los productos disponibles y agregarlos al carrito de compras.

### Carrito

Permite:

- Visualizar productos agregados.
- Aumentar o disminuir cantidades.
- Eliminar productos.
- Calcular automáticamente subtotales y total.
- Mostrar animaciones al modificar las cantidades.
- Confirmar la identidad mediante biometría cuando está disponible.
- Continuar al proceso de pago.

### Webpay

La aplicación utiliza Retrofit para comunicarse con un backend encargado de crear la transacción de pago.

Flujo esperado:

`Productos → Carrito → Webpay → Confirmación → Inicio`

> Para utilizar Webpay, el backend debe estar ejecutándose y la URL configurada en `RetrofitClient.kt` debe apuntar correctamente al servidor.

### Confirmación de compra

Después de completar el proceso de pago se muestra una pantalla de confirmación con:

- Total de la compra.
- Mensaje de operación exitosa.
- Animación de entrada.
- Botón para volver al inicio.

### Donaciones

Formulario para registrar una donación.

Incluye:

- Monto.
- Nombre del colaborador.
- Fecha.
- Donación anónima.
- Uso opcional de cámara.
- Confirmación biométrica cuando está disponible.
- Validaciones básicas.
- Persistencia local mediante Room.

### Colaboradores

Muestra los colaboradores registrados a partir de las donaciones almacenadas localmente.

Los datos permanecen guardados aunque la aplicación sea cerrada.

### Contacto

Formulario destinado a personas, empresas u organizaciones que quieran comunicarse con AlbaLab o coordinar una actividad.

Incluye:

- Nombre de contacto.
- Motivo de contacto.
- Cantidad de participantes.
- Fecha de actividad.
- Comuna o lugar.
- Aceptación de términos.
- Solicitud de seguimiento por parte de AlbaLab.
- Validaciones básicas.
- Persistencia local con Room.

Este formulario incorpora diferentes tipos de datos y componentes:

- `String`
- `Int`
- Fecha
- `Checkbox`
- `Switch`
- `Button`

### Nosotros

Sección informativa relacionada con AlbaLab.

### Registro e inicio de sesión

La aplicación cuenta con pantallas de:

- Registro.
- Inicio de sesión.
- Perfil.
- Cierre de sesión.

---

## Persistencia local con Room

La aplicación utiliza una base de datos local llamada:

```text
albalab_database
```

Entre las tablas implementadas se encuentran:

```text
colaboradores
donaciones
contactos
```

La arquitectura utilizada sigue la separación:

```text
Screen
   ↓
ViewModel
   ↓
Repository
   ↓
DAO
   ↓
Room
```

Esto permite separar la interfaz de usuario de la lógica y del acceso a datos.

---

## Recursos nativos utilizados

### Cámara

La aplicación puede solicitar permiso para utilizar la cámara y asociar una fotografía a determinados registros.

### Biometría

Se utiliza `BiometricPrompt` para confirmar la identidad del usuario en operaciones como compras o donaciones.

Si el dispositivo no tiene biometría configurada, la aplicación informa al usuario y permite continuar según el flujo definido.

---

## Animaciones

La aplicación incorpora animaciones con Jetpack Compose para mejorar la experiencia del usuario.

Entre ellas:

- Transiciones entre pantallas.
- `fadeIn` y `fadeOut`.
- Deslizamiento horizontal entre vistas.
- Animación al modificar cantidades del carrito.
- Animación de subtotales y total.
- Animación de confirmación de compra.
- `AnimatedContent`.
- `AnimatedVisibility`.
- `animateContentSize`.
- `scaleIn`.

---

## Arquitectura del proyecto

Estructura general:

```text
com.example.albalab
│
├── components
│   ├── AppDrawer.kt
│   └── AppTopBar.kt
│
├── data
│   ├── local
│   │   ├── AlbaLabDao.kt
│   │   ├── AlbaLabDatabase.kt
│   │   ├── ColaboradorEntity.kt
│   │   ├── DonacionEntity.kt
│   │   ├── ContactoEntity.kt
│   │   └── ColaboradorConDonacion.kt
│   │
│   └── repository
│       ├── DonacionRepository.kt
│       └── ContactoRepository.kt
│
├── model
│   ├── Colaborador.kt
│   ├── FormularioModel.kt
│   ├── FormularioModelDonaciones.kt
│   ├── ProductoModel.kt
│   ├── ResumenCompraModel.kt
│   ├── Usuario.kt
│   └── WebpayModels.kt
│
├── network
│   ├── AlbalabApi.kt
│   └── RetrofitClient.kt
│
├── ui
│   └── screens
│       ├── CarritoScreen.kt
│       ├── ColaboradoresScreen.kt
│       ├── ConfirmacionCompraScreen.kt
│       ├── ContactoScreen.kt
│       ├── DonacionesScreen.kt
│       ├── LoginScreen.kt
│       ├── PerfilScreen.kt
│       ├── RegistroScreen.kt
│       ├── ResumenCompraScreen.kt
│       └── WebpayScreen.kt
│
├── viewmodel
│   ├── AuthViewModel.kt
│   ├── FormularioViewModel.kt
│   ├── FormularioViewModel2.kt
│   └── ResumenCompraViewModel.kt
│
├── App.kt
└── MainActivity.kt
```

---

## Requisitos para ejecutar el proyecto

Se recomienda utilizar:

- Android Studio.
- SDK de Android configurado.
- Dispositivo físico Android o emulador.
- Conexión a Internet para funciones que utilizan el backend.
- Backend de AlbaLab ejecutándose para probar Webpay.

---

## Ejecución

1. Clonar el repositorio:

```bash
git clone https://github.com/Matiiki/AlbaLab-Mobile.git
```

2. Abrir Android Studio.

3. Seleccionar:

```text
Open
```

4. Abrir la carpeta:

```text
AlbaLab-Mobile
```

5. Esperar la sincronización de Gradle.

6. Seleccionar un emulador o conectar un dispositivo Android.

7. Ejecutar la aplicación con:

```text
Run ▶
```

---

## Configuración de Webpay

La conexión con el backend se configura en:

```text
network/RetrofitClient.kt
```

Si se utiliza un teléfono físico, se debe utilizar la dirección IPv4 del computador donde está ejecutándose el backend.

Ejemplo:

```kotlin
private const val BASE_URL =
    "http://192.168.1.100:9091/"
```

Si se utiliza el emulador estándar de Android Studio y el backend está ejecutándose en el mismo computador:

```kotlin
private const val BASE_URL =
    "http://10.0.2.2:9091/"
```

La IP debe modificarse según el entorno utilizado.

---

## Permisos

La aplicación puede requerir permisos como:

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.CAMERA" />
```

El permiso de cámara se solicita durante la ejecución cuando corresponde.

---

## Validaciones

Los formularios cuentan con validaciones básicas separadas de la interfaz mediante ViewModels.

Ejemplos:

- Campos obligatorios.
- Cantidades mayores a cero.
- Monto mínimo de donación.
- Selección de fecha.
- Selección de lugar.
- Aceptación de términos.

Los errores se muestran directamente junto al campo correspondiente.

---

## Control de versiones

El proyecto utiliza Git y GitHub para el control de versiones.

Repositorio:

```text
https://github.com/Matiiki/AlbaLab-Mobile
```

Antes de realizar la entrega final se recomienda verificar que:

- El proyecto compile correctamente.
- No existan conflictos pendientes.
- La rama `main` contenga la versión final.
- El código de GitHub sea el mismo código entregado en la plataforma de evaluación.

---

## Estado del proyecto

Actualmente se encuentran implementadas las principales funcionalidades solicitadas para la evaluación:

- Navegación entre pantallas.
- Formularios con validaciones.
- Arquitectura MVVM.
- Persistencia local con Room.
- Recursos nativos.
- Biometría.
- Cámara.
- Carrito de compras.
- Integración con backend.
- Webpay.
- Animaciones funcionales.
- Uso de Git y GitHub.

---

## Proyecto académico

Proyecto desarrollado con fines académicos para la asignatura de desarrollo de aplicaciones móviles.
