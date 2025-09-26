# 📦 Kibernunm Academy REST API

Este proyecto es una API REST construida con Spring Boot, diseñada para validar flujos integrados, arquitectura de componentes y automatización de pruebas en entornos CI/CD. Utiliza PostgreSQL como base de datos y permite configuración dinámica vía variables de entorno. Ahora incluye autenticación basada en JWT para proteger los endpoints.


## 🚀 Inicio rápido

### Requisitos

- Java 17+  
- Maven 3.8+  
- PostgreSQL  
- Variables de entorno definidas:  
  - `DB_URL`  
  - `DB_USER`  
  - `DB_PASS`  
  - `JWT_SECRET`  
  - `JWT_EXPIRATION_MS`

### Ejecución local

```bash
export DB_URL=jdbc:postgresql://localhost:5432/mi_db
export DB_USER=usuario
export DB_PASS=clave
export JWT_SECRET=mi_clave_secreta
export JWT_EXPIRATION_MS=3600000

mvn spring-boot:run
```


## 🧩 Estructura del proyecto

```
src/main/java/cl/kibernunmacademy/rest/
├── auth/              # Controladores y servicios de autenticación
├── config/            # Inicialización de base de datos y configuración
├── controller/        # Controladores REST protegidos
├── domain/            # Modelos de dominio
├── dto/               # Objetos de transferencia de datos
├── exception/         # Manejo de errores personalizados
├── filter/            # Filtro JWT para validación de tokens
├── jwt/               # Utilidades para generación y validación de JWT
├── model/             # Entidades JPA
├── repository/        # Interfaces de acceso a datos
├── service/           # Lógica de negocio
└── RestApplication.java # Punto de entrada
```


## 🔐 Seguridad JWT

La autenticación se gestiona mediante tokens JWT. Los usuarios deben autenticarse vía `/auth/login` para obtener un token válido. Este token debe incluirse en el header `Authorization` como `Bearer <token>` para acceder a endpoints protegidos.

### Flujo de autenticación

1. El usuario envía credenciales a `/auth/login`.
2. Se genera un JWT firmado con `JWT_SECRET`.
3. El token se valida en cada solicitud mediante el filtro `JwtAuthenticationFilter`.


## ⚙️ Configuración

La configuración se gestiona vía `application.yml`:

```yaml
spring:
  datasource:
    url: ${DB_URL}
    username: ${DB_USER}
    password: ${DB_PASS}
    driver-class-name: org.postgresql.Driver
  jpa:
    hibernate:
      ddl-auto: update
    database-platform: org.hibernate.dialect.PostgreSQLDialect
    show-sql: true

jwt:
  secret: ${JWT_SECRET}
  expirationMs: ${JWT_EXPIRATION_MS}
```


## 🧪 Validación de conexión

La clase `DatabaseInitializer` permite validar la conexión manualmente:

```java
Connection conn = new DatabaseInitializer().getConnection();
```


## 📋 Logs y trazabilidad

La aplicación imprime la URL de la base de datos al iniciar:

```java
System.out.println("DB_URL: " + System.getenv("DB_URL"));
```

Además, se configuran logs a nivel `INFO` para Spring Framework y trazabilidad de autenticación.


## 🛡️ Defensa técnica

Este proyecto está preparado para:

- Validar visualmente flujos de conexión, configuración y autenticación
- Modularizar componentes para pruebas automatizadas
- Documentar trazabilidad de errores, dependencias y seguridad
- Integrarse fácilmente en pipelines CI/CD


## 🧠 Autor

Mauricio Vera — QA Automation Specialist  
📍 Viña del Mar, Chile  
🎯 Enfocado en trazabilidad, validación visual y defensa técnica


## 📄 Licencia

MIT
