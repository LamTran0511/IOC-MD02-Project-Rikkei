package IOC_MD02_Project.dao;

import IOC_MD02_Project.model.Course;
import IOC_MD02_Project.model.Student;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public interface IStudentDao {
    List<Student> list(String sortBy, String sortDir) throws SQLException;
    void add(String name, Date dob, String email, boolean sex, String phone, String password) throws SQLException;
    boolean exists(int id) throws SQLException;
    void updateField(int id, String field, Object value) throws SQLException;
    void delete(int id) throws SQLException;
    boolean findStudentByEmail(String email) throws SQLException;
    Student login(String key, String password) throws SQLException;
    void changePasswordWithVerify(int studentId, boolean byEmail, String verify, String oldPass, String newPass) throws SQLException;
    List<Student> searchStudent(String strSt) throws SQLException;

}