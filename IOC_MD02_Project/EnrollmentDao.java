package IOC_MD02_Project;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentDao {

    private String safeSortDir(String dir) {
        return "DESC".equalsIgnoreCase(dir) ? "DESC" : "ASC";
    }

    public void register(int studentId, int courseId) throws SQLException {
        // Nếu bạn đã tạo UNIQUE INDEX (student_id, course_id) thì đã chặn trùng.
        // Nhưng vẫn check để báo lỗi thân thiện:
        String sqlCheck = "SELECT 1 FROM enrollment WHERE student_id=? AND course_id=? AND status <> 'CANCEL'";
        String sqlIns = "INSERT INTO enrollment(student_id, course_id) VALUES (?, ?)";

        try (Connection c = Db.getConn()) {
            try (PreparedStatement check = c.prepareStatement(sqlCheck)) {
                check.setInt(1, studentId);
                check.setInt(2, courseId);
                try (ResultSet rs = check.executeQuery()) {
                    if (rs.next()) throw new SQLException("Bạn đã đăng ký khóa học này rồi!");
                }
            }

            try (PreparedStatement ins = c.prepareStatement(sqlIns)) {
                ins.setInt(1, studentId);
                ins.setInt(2, courseId);
                ins.executeUpdate();
            }
        }
    }

    public List<StudentCourseRow> listRegisteredCourses(int studentId, String sortBy, String sortDir) throws SQLException {
        sortDir = safeSortDir(sortDir);
        String orderBy = "c.name " + sortDir;
        if ("DATE".equalsIgnoreCase(sortBy)) orderBy = "e.registered_at " + sortDir;

        String sql =
                "SELECT e.id AS enroll_id, c.id AS course_id, c.name AS course_name, c.duration, c.instructor, " +
                        "       e.registered_at, e.status " +
                        "FROM enrollment e JOIN course c ON c.id = e.course_id " +
                        "WHERE e.student_id=? " +
                        "ORDER BY " + orderBy + ", e.id DESC";

        try (Connection c = Db.getConn();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            List<StudentCourseRow> out = new ArrayList<>();
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    StudentCourseRow r = new StudentCourseRow();
                    r.enrollId = rs.getInt("enroll_id");
                    r.courseId = rs.getInt("course_id");
                    r.courseName = rs.getString("course_name");
                    r.duration = rs.getInt("duration");
                    r.instructor = rs.getString("instructor");
                    r.registeredAt = rs.getTimestamp("registered_at");
                    r.status = rs.getString("status");
                    out.add(r);
                }
            }
            return out;
        }
    }

    public void cancelIfWaiting(int studentId, int courseId) throws SQLException {
        String sql =
                "UPDATE enrollment SET status='CANCEL' " +
                        "WHERE student_id=? AND course_id=? AND status='WAITING'";
        try (Connection c = Db.getConn();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setInt(2, courseId);
            int n = ps.executeUpdate();
            if (n == 0) throw new SQLException("Chỉ hủy được khi trạng thái WAITING (chưa xác nhận).");
        }
    }
}