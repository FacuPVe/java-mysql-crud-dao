package net.openwebinars.java.mysql.crud.dao;

import net.openwebinars.java.mysql.crud.model.Producto;

import java.sql.SQLException;
import java.util.List;

public interface ProductoDao {
    int add(Producto prod) throws SQLException;

    Producto getById(int id) throws SQLException;

    List<Producto> getAll() throws SQLException;

    List<Producto> getByCategoria(int categoriaId) throws SQLException;

    int update(Producto prod) throws SQLException;

    void delete(int id) throws SQLException;
}
