package IOC_MD02_Project.service.impl;

import IOC_MD02_Project.dao.impl.AdmindaoImpl;
import IOC_MD02_Project.model.Admin;
import IOC_MD02_Project.service.IAdminService;

import java.sql.SQLException;

public class AdminServiceImpl implements IAdminService {
    private final AdmindaoImpl adminDao = new AdmindaoImpl();

    @Override
    public Admin login(String username, String password) throws SQLException {
        return adminDao.login(username, password);
    }
}