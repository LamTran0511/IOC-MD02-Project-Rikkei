package IOC_MD02_Project.service;

import IOC_MD02_Project.model.StudentCourseRow;

import java.sql.SQLException;
import java.util.List;

public interface IEnrollmentService {
    void register(int studentId, int courseId) throws SQLException;
    List<StudentCourseRow> listRegisteredCourses(int studentId, String sortBy, String sortDir) throws SQLException;
    void cancelIfWaiting(int studentId, int courseId) throws SQLException;
}