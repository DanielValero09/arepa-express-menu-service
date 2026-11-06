# menu-service · Arepa Express

Paso 1 del laboratorio DOSW, semana 14. API de menú con Java 21, Spring Boot 3.5.16 y Maven (3.6.3 o superior). Proyecto independiente: no requiere el directorio padre ni pedidos-service para compilar o ejecutarse.

## Compilar y probar

Desde este directorio, con JDK 21 y Maven en el PATH:

```powershell
java -version
mvn -version
mvn test
mvn package
```

`mvn package` también ejecuta las pruebas y produce `target/menu-service-1.0.0.jar`. Las pruebas HTTP arrancan Tomcat en un puerto aleatorio y verifican el contenido exacto del menú, HTTP 200, JSON y el rechazo de POST con 405.

## Ejecutar localmente

```powershell
java -jar target/menu-service-1.0.0.jar
```

También se puede ejecutar con `mvn spring-boot:run`. El puerto predeterminado de Spring Boot es **8080**; no se redefine en el proyecto. Detener con `Ctrl+C`.

## Probar GET /menu

En otra terminal PowerShell:

```powershell
$response = Invoke-WebRequest -Uri 'http://localhost:8080/menu' -Method Get -UseBasicParsing
$response.StatusCode
$response.Headers['Content-Type']
$response.Content
```

Respuesta HTTP **200**, `Content-Type: application/json`:

```json
[
  {"id":1,"nombre":"Arepa de choclo con queso","precio":9000},
  {"id":2,"nombre":"Arepa rellena de pollo","precio":11000},
  {"id":3,"nombre":"Arepa de huevo","precio":7000}
]
```

## Estructura

`MenuServiceApplication` inicia Spring Boot; `domain/model/Arepa` define el record de respuesta; `infrastructure/adapter/in/rest/MenuController` devuelve el menú fijo. Solo se utilizan Spring Web y Spring Boot Test (scope test).

## Contenedor y preparación para Azure

Con Docker instalado y en ejecución, después de `mvn package`:

```powershell
docker build -t menu-service:1.0.0 .
docker run --rm -p 8080:8080 menu-service:1.0.0
```

El Dockerfile usa `eclipse-temurin:21-jre` y el JAR ejecutable. En una fase posterior, Azure Container Apps deberá dirigir su ingress al puerto 8080. Esta fase no crea recursos de Azure.

## Ejecutar ambos servicios

Ejecutar menu-service en 8080 y, en otra terminal, pedidos-service con `SERVER_PORT=8081`, como indica su README. Si se necesita cambiar temporalmente el puerto de este servicio:

```powershell
$env:SERVER_PORT = '8082'
try { java -jar target/menu-service-1.0.0.jar } finally { Remove-Item Env:SERVER_PORT }
```

El cambio solo afecta a esa terminal. En Bash: `SERVER_PORT=8082 java -jar target/menu-service-1.0.0.jar`.

## Repositorio independiente y entorno restringido

Este directorio incluye su propio `pom.xml`, Dockerfile, pruebas y `.gitignore`; se puede copiar completo a un repositorio GitHub propio. No se ha inicializado ni publicado ningún repositorio.

En este entorno se necesita una caché Maven escribible y precargada, porque Maven Central está bloqueado. Desde este directorio, después de preparar la caché del workspace:

```powershell
$env:MAVEN_BASEDIR = (Get-Location).Path
$repo = (Resolve-Path '..\.maven-repository').Path
mvn -o "-Dmaven.repo.local=$repo" -B test
mvn -o "-Dmaven.repo.local=$repo" -B package
Remove-Item Env:MAVEN_BASEDIR
```

Esta alternativa es específica del entorno del laboratorio. Con acceso normal a Maven Central bastan `mvn test` y `mvn package`; no se debe publicar la caché Maven.

**Límite de la verificación:** el sandbox también produjo `AccessDeniedException` al cerrar recursos de `javac`. Las ejecuciones finales de `test` y `package` fueron incrementales y usaron las clases ya generadas. No se ha certificado una compilación limpia en este sandbox; repetir `mvn clean test package` en una terminal normal con Java 21. El reporte de fase en la raíz detalla los intentos y resultados reales.
