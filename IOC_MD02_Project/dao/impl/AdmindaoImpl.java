package IOC_MD02_Project.dao.impl;

import IOC_MD02_Project.dao.IAdminDao;
import IOC_MD02_Project.model.Admin;
import IOC_MD02_Project.utils.ConnectionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdmindaoImpl implements IAdminDao {

    @Override
    public Admin login(String username, String password) throws SQLException {
        String sql = "SELECT id, username, password FROM admin WHERE username = ? AND password = ?";

        try (Connection conn = ConnectionDB.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                Admin admin = new Admin();
                admin.setId(rs.getInt("id"));
                admin.setUsername(rs.getString("username"));
                admin.setPassword(rs.getString("password"));
                return admin;
            }
        }
    }
}