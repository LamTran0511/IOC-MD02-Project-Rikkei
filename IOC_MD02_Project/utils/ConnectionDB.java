package IOC_MD02_Project.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDB {

    private static final String URL =
            "jdbc:postgresql://localhost:5432/ioc_md02_project?currentSchema=projectioc";
    private static final String USER = "postgres";
    private static final String PASS = "526412";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public static Connection getConn() throws SQLException {
        return getConnection();
    }
}