package net.openwebinars.java.mysql.crud.model;

import java.util.Objects;

public class Categoria {
//  CREATE TABLE categoria (
//     id_categoria INT NOT NULL AUTO_INCREMENT,
//     nombre VARCHAR(100) NOT NULL,
//      descripcion TEXT,
//      PRIMARY KEY (id_categoria)
//  );
    private int id_categoria;
    private String nombre;
    private String descripcion;

    public Categoria() { }

    public Categoria(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public Categoria(int id_categoria, String nombre, String descripcion) {
        this(nombre, descripcion);
        this.id_categoria = id_categoria;
    }

    public int getId_categoria() {
        return id_categoria;
    }

    public void setId_categoria(int id_categoria) {
        this.id_categoria = id_categoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Categoria categoria = (Categoria) o;
        return id_categoria == categoria.id_categoria && 
               Objects.equals(nombre, categoria.nombre) && 
               Objects.equals(descripcion, categoria.descripcion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_categoria, nombre, descripcion);
    }

    @Override
    public String toString() {
        return "Categoria{" +
                "id_categoria=" + id_categoria +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}
