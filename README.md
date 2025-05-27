# Elaboración de un CRUD con JAVA y MySQL JDBC

Este proyecto sigue los pasos del curso de OpenWebinars llamado "Curso de Java y MySQL JDBC: Elaboración de un CRUD" para poder crear un CRUD en Java.

## Contenido

### 1. Introducción

**1.1. Archivos creados**:

- docker-compose.yml
- schema.sql

**Comandos**:
- docker-compose -p java-mysql-crud up -d (este comando ha dado error para poder autenticarse)
- docker-compose up -d (con este comando no da error de autenticación porque no se cambia el nombre)

**Accesos**:

Por como está configurado el docker-compose.yml, podremos acceder al phpMyAdmin desde la url `http://localhost:8088/`

**1.2. Patrón DAO**:

En este proyecto se utliza el patrón DAO (Data Access Object), el cual pretende desacoplar la aplicación de la forma de acceder a los datos. O sea, si cambiamos el tipo de la fuente de datos, no tenemos que cambiar el resto de la aplicación. 

Ventajas:
- Posibilidad de modificar la API sin cambiar la lógica de negocio
- Debido a que cada capa/clase tiene su responsabilidad, hay una mayor facilidad para testear.
- Posibilidad de complementar con otros patrones de diseño (mayor robustez).

Desventajas:
- Componentes DAO pueden llegar a tener código muy parecido.
- Puede ser tedioso, tender a producir copy&paste y fallos.

Métodos habituales:
- Create
- Update
- Delete
- Query: All y ByPk / ById

**1.3. Sentencias precompiladas con PreparedStatement**:

De la interfaz Statement podemos saber lo siguiente:
- Tiene métodos como execute, executeUpdate o executeQuery.
- Tiene sentencias SQL con parámetros que se proporcionan en una cadena de caracteres.
    - Esto puede traer problemas porque se pueden utilizar técnicas de SQL INJECTION.
- Para prevenir problemas de SQL INJECTION se pueden utilizar sentencias precompiladas con PreparedStatement.

PreparedStatement:
- Es una interfaz que hereda de Statement.
- Se construye usando el método Connection.prepareStatement(String sql).
- Tiene una serie de métodos setXXX para establecer parámetros de entrada.
- En vez de concatener parámetros dentro de una consulta SQL se indican los "huecos" con ? y después se les asignan valores. Ejemplo:
  ```sql
  SELECT * FROM users WHERE userId = ?
  ```
- JDBC se encarga de "precompilar" la consulta antes de enviarla para poder evitar código malicioso.
- Los objetivos de tipo PreparedStatement se construyen a través de un objeto Connection.

### 2. Establecimiento de la conexión

**1.1. Creación del proyecto**:

Propiedades del proyecto:
- IntelliJ IDEA 
- Maven: Empty project
    - Java XX
    - Group Id
    - Artifact Id
- Elementos añadidos en POM.XML:
    - UTF8, Java 22
    - plugins (test, compile, exec, ...)
    - Dependencias: JUnit en el caso de que se hagan pruebas

Driver para poder trabajar con MySQL (versión actual 9.3.0): https://dev.mysql.com/downloads/connector/j/

Añadir la versión en el siguiente código:
```java
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <version>9.3.0</version>
 </dependency>
```

**1.2. Establecimiento de la conexión con la base de datos**:

Algunas interfaces de JDBC:
- `DriverManager`: responsable de crear una conexión con la base de datos utilizando el driver registrado en el classpath y la URL JDBC.

- La conexión creada por DriverManager se conoce como `Connection`: conexión abierta responsable de crear sentencias SQL. Además debe estar cerrada si no se utiliza.

En caso de que se necesite una conexión abierta por si el usuario lo necesita, se pueden hacer las siguientes soluciones:
- Abrir y cerrar una conexión cada que sea necesario.
- Mantener una única conexión abierta.
- Utilizar un pool de conexiones.

**Pool de conexiones**

Un Pool de conexiones permite crear varias conexiones reutilizables. Ejemplo:
- Sin un pool de conexiones se crea una propia conexión para cada proceso y debido a eso se pueden gastar muchos recursos ineficientemente si no se gestiona debidamente.
- Con un pool de conexiones se pueden tener un número máximo de conexiones para gestionar y también se puede ir devolviendo y recibiendo diferentes conexiones para reutilizarlas.

Dependen de:
- Número de usuarios concurrentes.
- Tipos de consultas a realizar.
- Procesadores (núcleos), memoria, tipo de disco, etc.

**Gestionar Pool de conexiones**

Existen diferentes librerías/frameworks para poder trabajar con los Pool de conexiones.

En este proyecto se utiliza HIKARICP debido a las siguientes razones:
- Peso liviano (130kb).
- Más ligera y rápida, por lo tanto, menos consumo de memoria.
- Prevención contra problemas de deadlcok (interbloqueos).
- Control de detección para ante leaks (conexiones olvidadas).
- Uso de colecciones más rápidas (FastList en vez de ArrayList).


**HIKARICP**

La documentación de HIKARICP se encuentra en https://github.com/brettwooldridge/HikariCP

Las versiones disponibles junto con información de implementación está disponible en: https://central.sonatype.com/search?q=com.zaxxer.hikaricp&smo=true

Código para pom.xml:
```java
<dependency>
    <groupId>com.zaxxer</groupId>
    <artifactId>HikariCP</artifactId>
    <version>6.3.0</version>
</dependency>
```

### 3. Desarrollo del DAO

**3.1. Creación del objeto y método de inserción**

El objeto DAO implementado para la aplicación abstrae cómo se almacena o se extrae la información ofreciendo clases POJO (Plain Old Java Object) que representarán los elementos de información del sistema (entidad de información).

- Permite manejar toda la información de la tabla.
- El objeto DAO se define mediante una interfaz y clase.
- En la clase se ha aplicado el patrón singleton para poder tener una única instancia para toda la aplicación.
- Para el método de inserción se utiliza una estructura try-with-resources para abrir los objetos usados en el proyecto.

**3.2. Método de consulta: Introducción*

**3.3. Método de consulta: Conclusión*

**3.4. Método de actualización*

**3.5. Método de borrado*



