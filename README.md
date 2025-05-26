# Elaboración de un CRUD con JAVA y MySQL JDBC

Este proyecto sigue los pasos del curso de OpenWebinars llamado "Curso de Java y MySQL JDBC: Elaboración de un CRUD" para poder crear un CRUD en Java.

## Contenido

### 1. Introducción

**Archivos creados**:

- docker-compose.yml
- schema.sql

**Comandos**:
- docker-compose -p java-mysql-crud up -d (este comando ha dado error para poder autenticarse)
- docker-compose up -d (con este comando no da error de autenticación porque no se cambia el nombre)

**Accesos**:

Por como está configurado el docker-compose.yml, podremos acceder al phpMyAdmin desde la url `http://localhost:8088/`

**Patrón DAO**:

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

