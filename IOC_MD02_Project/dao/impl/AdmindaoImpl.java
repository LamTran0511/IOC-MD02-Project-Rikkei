package IOC_MD02_Project.dao.impl;

import IOC_MD02_Project.model.Admin;
import IOC_MD02_Project.utils.ConnectionDB;

import java.sql.*;

public class AdmindaoImpl {

    public Admin login(String username, String password) throws SQLException {
        String sql = "SELECT id, username FROM admin WHERE username = ? AND password = ?";
        try (Connection c = ConnectionDB.getConn();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                Admin a = new Admin();
                a.id = rs.getInt("id");
                a.username = rs.getString("username");
                return a;
            }
        }
    }
}