package net.openwebinars.java.mysql.crud.dao;

import net.openwebinars.java.mysql.crud.model.Categoria;
import net.openwebinars.java.mysql.crud.pool.MyDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDaoImpl implements CategoriaDao {
    private static CategoriaDaoImpl instance;

    static {
        instance = new CategoriaDaoImpl();
    }

    private CategoriaDaoImpl() {}

    public static CategoriaDaoImpl getInstance() {
        return instance;
    }

    @Override
    public int add(Categoria cat) throws SQLException {
        String sql = """
                    INSERT INTO categoria (nombre, descripcion)
                    VALUES (?, ?);
                """;

        int result;

        try(Connection conn = MyDataSource.getConnection();
            PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setString(1, cat.getNombre());
            pstm.setString(2, cat.getDescripcion());

            result = pstm.executeUpdate();
        }
        return result;
    }

    @Override
    public Categoria getById(int id) throws SQLException {
        Categoria result = null;

        String sql = "SELECT * FROM categoria WHERE id_categoria = ?";

        try(Connection conn = MyDataSource.getConnection();
            PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setInt(1, id);

            try(ResultSet rs = pstm.executeQuery()) {
                while(rs.next()) {
                    result = new Categoria();
                    result.setId_categoria(rs.getInt("id_categoria"));
                    result.setNombre(rs.getString("nombre"));
                    result.setDescripcion(rs.getString("descripcion"));
                }
            }
        }
        return result;
    }

    @Override
    public List<Categoria> getAll() throws SQLException {
        String sql = "SELECT * FROM categoria";

        List<Categoria> result = new ArrayList<>();

        try(Connection conn = MyDataSource.getConnection();
            PreparedStatement pstm = conn.prepareStatement(sql);
            ResultSet rs = pstm.executeQuery()) {

            Categoria cat;

            while(rs.next()) {
                cat = new Categoria();
                cat.setId_categoria(rs.getInt("id_categoria"));
                cat.setNombre(rs.getString("nombre"));
                cat.setDescripcion(rs.getString("descripcion"));

                result.add(cat);
            }
        }
        return result;
    }

    @Override
    public int update(Categoria cat) throws SQLException {
        String sql = """
                    UPDATE categoria SET
                        nombre = ?, descripcion = ?
                    WHERE id_categoria = ?
                """;

        int result;

        try(Connection conn = MyDataSource.getConnection();
            PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setString(1, cat.getNombre());
            pstm.setString(2, cat.getDescripcion());
            pstm.setInt(3, cat.getId_categoria());

            result = pstm.executeUpdate();
        }
        return result;
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM categoria WHERE id_categoria = ?";

        try (Connection conn = MyDataSource.getConnection();
             PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setInt(1, id);
            pstm.executeUpdate();
        }
    }
}
