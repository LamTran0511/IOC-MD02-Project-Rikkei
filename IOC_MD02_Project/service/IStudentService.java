package IOC_MD02_Project.service;

import IOC_MD02_Project.model.Student;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public interface IStudentService {
    List<Student> list(String keyword, String sortBy, String sortDir) throws SQLException;
    void add(String name, Date dob, String email, boolean sex, String phone, String password) throws SQLException;
    boolean exists(int id) throws SQLException;
    void updateField(int id, String field, Object value) throws SQLException;
    void delete(int id) throws SQLException;

    Student login(String key, String password) throws SQLException;
    void changePasswordWithVerify(int studentId, boolean byEmail, String verify, String oldPass, String newPass) throws SQLException;
}