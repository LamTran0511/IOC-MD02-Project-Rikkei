package IOC_MD02_Project.dao.impl;

import IOC_MD02_Project.dao.IEnrollmentDao;
import IOC_MD02_Project.model.Enrollment;
import IOC_MD02_Project.utils.ConnectionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentdaoImpl implements IEnrollmentDao {

    private String safeSortDir(String sortDir) {
        if ("DESC".equalsIgnoreCase(sortDir)) {
            return "DESC";
        }
        return "ASC";
    }

    @Override
    public void register(int studentId, int courseId) throws SQLException {
//        String sql = "INSERT INTO enrollment(student_id, course_id, status) " +
//                "VALUES (?, ?, 'WAITING') " +
//                "ON CONFLICT (student_id, course_id) DO UPDATE " +
//                "SET status = 'WAITING', registered_at = CURRENT_TIMESTAMP " +
//                "WHERE enrollment.status IN ('CANCEL', 'DENIED') " +
//                "RETURNING status";
        String sql = """
INSERT INTO enrollment(student_id, course_id, registered_at) VALUES (?, ?, CURRENT_TIMESTAMP)
""";

        try (Connection conn = ConnectionDB.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            ps.setInt(2, courseId);
            ps.executeUpdate();

        }

    }

    @Override
    public List<Enrollment> listRegisteredCourses(int studentId, String sortBy, String sortDir) throws SQLException {
        sortDir = safeSortDir(sortDir);

        String orderBy;
        if ("DATE".equalsIgnoreCase(sortBy)) {
            orderBy = "e.registered_at " + sortDir;
        } else {
            orderBy = "c.name " + sortDir;
        }

        String sql = "SELECT e.id AS enroll_id, c.id AS course_id, c.name AS course_name, " +
                "c.duration, c.instructor, e.registered_at, e.status " +
                "FROM enrollment e " +
                "JOIN course c ON c.id = e.course_id " +
                "WHERE e.student_id = ? " +
                "ORDER BY " + orderBy + ", e.id DESC";

        List<Enrollment> enrollments = new ArrayList<>();

        try (Connection conn = ConnectionDB.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, studentId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Enrollment e = new Enrollment();
                    e.setEnrollId(rs.getInt("enroll_id"));
                    e.setCourseId(rs.getInt("course_id"));
                    e.setCourseName(rs.getString("course_name"));
                    e.setDuration(rs.getInt("duration"));
                    e.setInstructor(rs.getString("instructor"));
                    e.setRegisteredAt(rs.getTimestamp("registered_at"));
                    e.setStatus(rs.getString("status"));
                    enrollments.add(e);
                }
            }
        }

        return enrollments;
    }

    @Override
    public void cancelIfWaiting(int studentId, int courseId) throws SQLException {
        String sql = "UPDATE enrollment SET status = 'CANCEL' " +
                "WHERE student_id = ? AND course_id = ? AND status = 'WAITING'";

        try (Connection conn = ConnectionDB.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            ps.setInt(2, courseId);

            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new SQLException("Chỉ hủy được khi trạng thái đang là WAITING!");
            }
        }
    }
}