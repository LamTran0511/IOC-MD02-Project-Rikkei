package IOC_MD02_Project.service.impl;

import IOC_MD02_Project.dao.impl.EnrollmentdaoImpl;
import IOC_MD02_Project.model.StudentCourseRow;
import IOC_MD02_Project.service.IEnrollmentService;

import java.sql.SQLException;
import java.util.List;

public class EnrollmentServiceImpl implements IEnrollmentService {
    private final EnrollmentdaoImpl enrollmentDao = new EnrollmentdaoImpl();

    @Override
    public void register(int studentId, int courseId) throws SQLException {
        enrollmentDao.register(studentId, courseId);
    }

    @Override
    public List<StudentCourseRow> listRegisteredCourses(int studentId, String sortBy, String sortDir) throws SQLException {
        return enrollmentDao.listRegisteredCourses(studentId, sortBy, sortDir);
    }

    @Override
    public void cancelIfWaiting(int studentId, int courseId) throws SQLException {
        enrollmentDao.cancelIfWaiting(studentId, courseId);
    }
}