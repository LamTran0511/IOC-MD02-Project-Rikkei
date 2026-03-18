package IOC_MD02_Project.service;

import IOC_MD02_Project.model.Admin;

import java.sql.SQLException;

public interface IAdminService {
    Admin login(String username, String password) throws SQLException;
}