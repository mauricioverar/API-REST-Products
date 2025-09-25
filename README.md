# 📦 Kibernunm Academy REST API

Este proyecto es una API REST construida con Spring Boot, diseñada para validar flujos integrados, arquitectura de componentes y automatización de pruebas en entornos CI/CD. Utiliza PostgreSQL como base de datos y permite configuración dinámica vía variables de entorno.


## 🚀 Inicio rápido

### Requisitos

- Java 17+
- Maven 3.8+
- PostgreSQL
- Variables de entorno definidas:
  - `DB_URL`
  - `DB_USER`
  - `DB_PASS`

### Ejecución local

```bash
export DB_URL=jdbc:postgresql://localhost:5432/mi_db
export DB_USER=usuario
export DB_PASS=clave

mvn spring-boot:run
```


## 🧩 Estructura del proyecto

```
src/main/java/cl/kibernunmacademy/rest/
├── config/           # Inicialización de base de datos y configuración
├── controller/       # Controladores REST
├── dto/              # Objetos de transferencia de datos
├── exception/        # Manejo de errores personalizados
├── model/            # Entidades JPA
├── repository/       # Interfaces de acceso a datos
├── service/          # Lógica de negocio
└── RestApplication.java # Punto de entrada
```


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

Además, se configuran logs a nivel `INFO` para Spring Framework.


## 🛡️ Defensa técnica

Este proyecto está preparado para:

- Validar visualmente flujos de conexión y configuración
- Modularizar componentes para pruebas automatizadas
- Documentar trazabilidad de errores y dependencias
- Integrarse fácilmente en pipelines CI/CD


## 📚 Futuras mejoras

- Integración con Swagger para documentación de endpoints
- Pruebas automatizadas con JUnit5 y Mockito
- Validación visual con Selenium o Puppeteer


## 🧠 Autor

Mauricio Vera — QA Automation Specialist  
📍 Viña del Mar, Chile  
🎯 Enfocado en trazabilidad, validación visual y defensa técnica


## 📄 Licencia

MIT
```
