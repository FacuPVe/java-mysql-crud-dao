package net.openwebinars.java.mysql.crud;

import net.openwebinars.java.mysql.crud.dao.EmpleadoDao;
import net.openwebinars.java.mysql.crud.dao.EmpleadoDaoImpl;
import net.openwebinars.java.mysql.crud.model.Empleado;
import net.openwebinars.java.mysql.crud.pool.MyDataSource;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;


public class App {

    public static void main(String[] args) {
//        System.out.println("Hello World!");
        testDao();
    }

    public static void testDao() {

        EmpleadoDao dao = EmpleadoDaoImpl.getInstance();

        Empleado emp = new Empleado("Luis Miguel", "López Magaña", LocalDate.of(1982, 9, 18),
                "Profesor", "luismi@openwebinars.net");

        try {
            int n = dao.add(emp);
            System.out.println("El número de registros insertados es: " + n);

            List<Empleado> empleados = dao.getAll();

            if (empleados.isEmpty())
                System.out.println("No hay empleados registrados");
            else
                empleados.forEach(System.out::println);
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void testPool() {
        try (Connection conn = MyDataSource.getConnection()){
            DatabaseMetaData metaData = conn.getMetaData();
            String[] types = {"TABLE"};
            ResultSet tables = metaData.getTables(null, null, "%", types);
            while (tables.next()) {
                System.out.println(tables.getString("TABLE_NAME"));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}
