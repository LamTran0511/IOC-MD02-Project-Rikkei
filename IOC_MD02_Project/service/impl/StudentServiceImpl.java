package IOC_MD02_Project.service.impl;

import IOC_MD02_Project.dao.impl.StudentdaoImpl;
import IOC_MD02_Project.model.Student;
import IOC_MD02_Project.service.IStudentService;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class StudentServiceImpl implements IStudentService {
    private final StudentdaoImpl studentDao = new StudentdaoImpl();

    @Override
    public List<Student> list(String keyword, String sortBy, String sortDir) throws SQLException {
        return studentDao.list(keyword, sortBy, sortDir);
    }

    @Override
    public void add(String name, Date dob, String email, boolean sex, String phone, String password) throws SQLException {
        studentDao.add(name, dob, email, sex, phone, password);
    }

    @Override
    public boolean exists(int id) throws SQLException {
        return studentDao.exists(id);
    }

    @Override
    public void updateField(int id, String field, Object value) throws SQLException {
        studentDao.updateField(id, field, value);
    }

    @Override
    public void delete(int id) throws SQLException {
        studentDao.delete(id);
    }

    @Override
    public Student login(String key, String password) throws SQLException {
        return studentDao.login(key, password);
    }

    @Override
    public void changePasswordWithVerify(int studentId, boolean byEmail, String verify, String oldPass, String newPass) throws SQLException {
        studentDao.changePasswordWithVerify(studentId, byEmail, verify, oldPass, newPass);
    }
}