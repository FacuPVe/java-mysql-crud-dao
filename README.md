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

**3.2. Método de consulta: Introducción**

Las consultas sin parámetros del proyecto:
- Método de consulta para atraer todos los empleados
- No necesitan argumentos de entrada.
- Implementa try-with-resources.

**3.3. Método de consulta: Conclusión**

Las consultas con parámetros del proyecto:
- Necesitan algún argumento de entrada.
- Implementan try-with-resources.
- Necesitan la clave primaria (ID) del empleado.

**3.4. Método de actualización**

Método de actualización:
- Similar al método de inserción.
- Utiliza try-with-resources.

**3.5. Método de borrado**

Método de borrado:
- Se recibe la clave primaria del empleado(ID) como argumento.
- Utiliza try-with-resources.
- Habitualmente no suelen devolver nada.

### 4. Desarrollo del resto de la aplicación

**4.1. Desarrollo del menú**

Para el desarrollo del menú se han hecho las siguientes implementaciones:
- Una clase con el menú.
- Diferentes métodos para cada opción del CRUD.
- Una clase para la lectura por teclado sin el uso de Scanner debido a que es más lento.
- La lectujra por teclado se hace con BufferedReader y StringTokenizer.

**4.2. Método de inserción**

En el método de inserción se solicitan los datos del nuevo empleado por consola mediante el reader y se utiliza el DAO para poder hacer la inserción.

**4.3. Método de consulta de todos los registros**

Sobre este método de consulta podemos saber lo siguiente:
- Solo se recoge la opción del menú.
- Se realiza la consulta a través del Dao.
- Se formatea la salida para imprimir los datos más estructurados con printf.

**4.4. Método de consulta por la clave primaria**

Características sobre el método de consulta por clave primaria:
- Se solicita por consola el ID del empleado que se quiere buscar.
- Se realiza la consulta a través del DAO.
- Se reutilizan los métodos que formateaban la salida para tener los datos mostrados mejor estructurados.

**4.5. Método de actualización**

Características sobre el método de actualización:
- Se solicita el ID y se busca al empleado.
- Si existge el empleado se muestran los datos actuales y después se recogen los nuevos datos.
- En caso de que no se quiera modificar un dato, se reutiliza el dato antiguo con la captura de ningún caracter introducido.

**4.6. Método de borrado**

Características sobre el método de borrado:

- Se solicita por consola el ID del empleado que se quiere borrar.
- Se solicita una confirmación para asegurar de que no se elimine a un empleado por error.
- Se elimina al empleado a través del DAO.


# Conclusiones

**¿Qué se ha visto?**

- Introducción: Patrón DAO, sentencias precompiladas, 
- Pool de conexiones gestionado con HikariCP.
- Objeto DAO: métodos de creación, consulta, actualización y borrado.
- Creación de un menú interactivo con opciones para cada uno de los métodos del objeto DAO.

**¿Qué se ha aprendido?**

- La creación de una aplicación de consola que utiliza una base de datos relacional.
- Buenas prácticas sobre cómo evitar el uso de Inyección SQL como puede ser el uso de sentencias precompiladas.
- El uso de pool de conexiones para hacer eficiente el uso de recursos.
- Las operaciones básicas que se pueden hacer en una base de datos MySQL utilizando JDBC.