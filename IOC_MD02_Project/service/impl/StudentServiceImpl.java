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
    public List<Student> list(String sortBy, String sortDir) throws SQLException {
        return studentDao.list(sortBy, sortDir);
    }

    @Override
    public List<Student> searchStudent(String strSt) throws SQLException {
        return studentDao.searchStudent(strSt);
    }

    @Override
    public void add(String name, Date dob, String email, boolean sex, String phone, String password) throws SQLException {
        if (studentDao.findStudentByEmail(email)) {
            System.out.println("Không thành công, email đã bị trùng!");
            return;
        }
        studentDao.add(name, dob, email, sex, phone, password);
        System.out.println("Thêm học viên thành công!");
    }

    @Override
    public boolean exists(int id) throws SQLException {
        return studentDao.exists(id);
    }

    @Override
    public boolean updateField(int id, String field, Object value) throws SQLException {
        if (field.equals("email") && studentDao.findStudentByEmail(String.valueOf(value))) {
            System.out.println("Không thành công, email đã bị trùng!");
            return false;
        }
        studentDao.updateField(id, field, value);
        return true;
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
    public void changePasswordWithVerify(int studentId, boolean byEmail, String verify, String oldPass, String newPass)
            throws SQLException {
        studentDao.changePasswordWithVerify(studentId, byEmail, verify, oldPass, newPass);
    }
}