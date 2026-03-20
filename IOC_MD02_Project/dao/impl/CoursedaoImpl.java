package IOC_MD02_Project.dao.impl;

import IOC_MD02_Project.dao.ICourseDao;
import IOC_MD02_Project.model.Course;
import IOC_MD02_Project.utils.ConnectionDB;

import org.postgresql.util.PSQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CoursedaoImpl implements ICourseDao {

//    private String safeSortBy(String sortBy) {
//        if ("name".equalsIgnoreCase(sortBy)) {
//            return "name";
//        }
//        return "id";
//    }
//
//    private String safeSortDir(String sortDir) {
//        if ("DESC".equalsIgnoreCase(sortDir)) {
//            return "DESC";
//        }
//        return "ASC";
//    }

    @Override
    public List<Course> list(String sortBy, String sortDir) throws SQLException {
//        sortBy = safeSortBy(sortBy);
//        sortDir = safeSortDir(sortDir);

        String sql = "SELECT * " +
                "FROM course " +
                "ORDER BY " + sortBy + " " + sortDir + ", id ASC";

        List<Course> courses = new ArrayList<>();

        try (Connection conn = ConnectionDB.getConn();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Course c = new Course();
                c.id = rs.getInt("id");
                c.name = rs.getString("name");
                c.duration = rs.getInt("duration");
                c.instructor = rs.getString("instructor");
                c.createAt = rs.getTimestamp("created_at");
                courses.add(c);
            }
        }

        return courses;
    }

    @Override
    public List<Course> searchCourse(String str) throws SQLException {
        String sql = "SELECT * " +
                "FROM course " +
                "WHERE name ILIKE ? " +
//                "OR instructor ILIKE ? " +
                "OR CAST(id AS TEXT) ILIKE ? " + //sửa tìm theo tên
                "ORDER BY id ASC";

        List<Course> courses = new ArrayList<>();

        try (Connection conn = ConnectionDB.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String value = "%" + str.trim() + "%";
            ps.setString(1, value);
            ps.setString(2, value);
//            ps.setString(3, value);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Course c = new Course();
                    c.id = rs.getInt("id");
                    c.name = rs.getString("name");
                    c.duration = rs.getInt("duration");
                    c.instructor = rs.getString("instructor");
                    c.createAt = rs.getTimestamp("created_at");
                    courses.add(c);
                }
            }
        }

        return courses;
    }

    @Override
    public void add(String name, int duration, String instructor) throws SQLException {
        if (name == null || name.isBlank()) {
            throw new SQLException("Tên khóa học không được để trống!");
        }
        if (duration <= 0) {
            throw new SQLException("Thời lượng phải lớn hơn 0!");
        }
        if (instructor == null || instructor.isBlank()) {
            throw new SQLException("Giảng viên không được để trống!");
        }

        String sql = "INSERT INTO course(name, duration, instructor) VALUES (?, ?, ?)";

        try (Connection conn = ConnectionDB.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name.trim());
            ps.setInt(2, duration);
            ps.setString(3, instructor.trim());
            ps.executeUpdate();
        }
    }

    @Override
    public boolean exists(int id) throws SQLException {
        String sql = "SELECT 1 FROM course WHERE id = ?";

        try (Connection conn = ConnectionDB.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public void updateField(int id, String field, Object value) throws SQLException {
        if (!"name".equals(field) && !"duration".equals(field) && !"instructor".equals(field)) {
            throw new SQLException("Thuộc tính sửa không hợp lệ!");
        }

        String sql = "UPDATE course SET " + field + " = ? WHERE id = ?";

        try (Connection conn = ConnectionDB.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setObject(1, value);
            ps.setInt(2, id);

            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new SQLException("Không tìm thấy khóa học id = " + id);
            }
        }
    }

    @Override
    public boolean deleteCourse(int id) {
        String sql = "DELETE FROM course WHERE id=?";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (PSQLException e) {
            if ("23503".equals(e.getSQLState()) || "23001".equals(e.getSQLState())) {
                throw new RuntimeException("COURSE_HAS_ENROLLMENT");
            }
            throw new RuntimeException("DB_ERROR");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("SYSTEM_ERROR");
        }
    }
}