package IOC_MD02_Project.dao;

import IOC_MD02_Project.model.Admin;

import java.sql.SQLException;

public interface IAdminDao {
    Admin login(String username, String password) throws SQLException;
}