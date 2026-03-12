package IOC_MD02_Project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Db {
    private static final String URL =
            "jdbc:postgresql://localhost:5432/ioc_md02_project?currentSchema=projectioc";
    private static final String USER = "postgres";
    private static final String PASS = "526412";
    public static Connection getConn() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}