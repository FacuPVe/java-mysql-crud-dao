package net.openwebinars.java.mysql.crud;
import java.sql.*;

import net.openwebinars.java.mysql.crud.pool.MyDataSource;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

public class App {

    public static void main(String[] args) {
//        System.out.println("Hello World!");
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
