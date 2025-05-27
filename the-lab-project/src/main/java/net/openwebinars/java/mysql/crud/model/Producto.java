package net.openwebinars.java.mysql.crud.model;

import java.util.Objects;

public class Producto {
    //
//    CREATE TABLE producto (
//            id_producto INT NOT NULL AUTO_INCREMENT,
//            nombre VARCHAR(100) NOT NULL,
//    precio DECIMAL(10,2) NOT NULL,
//    stock INT NOT NULL,
//    id_categoria INT,
//    PRIMARY KEY (id_producto),
//    FOREIGN KEY (id_categoria) REFERENCES categoria(id_categoria)
//            );
    private int id_producto;
    private String nombre;
    private double precio;
    private int stock;
    private int id_categoria;

    public Producto() { }

    public Producto(String nombre, double precio, int stock, int id_categoria) {
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
        this.id_categoria = id_categoria;
    }

    public Producto(int id_producto, String nombre, double precio, int stock, int id_categoria) {
        this(nombre, precio, stock, id_categoria);
        this.id_producto = id_producto;
    }

    public int getId_producto() {
        return id_producto;
    }

    public void setId_producto(int id_producto) {
        this.id_producto = id_producto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getId_categoria() {
        return id_categoria;
    }

    public void setId_categoria(int id_categoria) {
        this.id_categoria = id_categoria;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Producto producto = (Producto) o;
        return id_producto == producto.id_producto && 
               Double.compare(producto.precio, precio) == 0 && 
               stock == producto.stock && 
               id_categoria == producto.id_categoria && 
               Objects.equals(nombre, producto.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_producto, nombre, precio, stock, id_categoria);
    }

    @Override
    public String toString() {
        return "Producto{" +
                "id_producto=" + id_producto +
                ", nombre='" + nombre + '\'' +
                ", precio=" + precio +
                ", stock=" + stock +
                ", id_categoria=" + id_categoria +
                '}';
    }
}
