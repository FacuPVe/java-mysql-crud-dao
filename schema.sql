CREATE TABLE empleado (
    id_empleado         MEDIUMINT NOT NULL AUTO_INCREMENT,
    nombre              VARCHAR(100) NOT NULL,
    apellidos           VARCHAR(100) NOT NULL,
    fecha_nacimiento    DATE NOT NULL,
    puesto              VARCHAR(100) NOT NULL,
    email               VARCHAR(320),
    PRIMARY KEY (id_empleado)
);

-- Tablas sobre productos y categorías siguiendo relación de uno a muchos(1:N)
CREATE TABLE categoria (
    id_categoria INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    PRIMARY KEY (id_categoria)
);

CREATE TABLE producto (
    id_producto INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    precio DECIMAL(10,2) NOT NULL,
    stock INT NOT NULL,
    id_categoria INT,
    PRIMARY KEY (id_producto),
    FOREIGN KEY (id_categoria) REFERENCES categoria(id_categoria)
);