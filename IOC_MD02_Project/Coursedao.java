package IOC_MD02_Project;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Coursedao {

    private static final String[] SORT_WHITELIST = {"id", "name"};

    private String safeSortBy(String sortBy) {
        if (sortBy == null) return "id";
        for (String s : SORT_WHITELIST) {
            if (s.equalsIgnoreCase(sortBy)) return s;
        }
        return "id";
    }

    private String safeSortDir(String dir) {
        return "DESC".equalsIgnoreCase(dir) ? "DESC" : "ASC";
    }

    public List<Course> list(String keyword, String sortBy, String sortDir) throws SQLException {
        sortBy = safeSortBy(sortBy);
        sortDir = safeSortDir(sortDir);

        String sql =
                "SELECT id, name, duration, instructor, create_at " +
                        "FROM course " +
                        "WHERE (? IS NULL OR ? = '') " +
                        "   OR (name ILIKE '%'||?||'%' OR CAST(id AS TEXT) ILIKE '%'||?||'%') " +
                        "ORDER BY " + sortBy + " " + sortDir + ", id ASC";

        try (Connection c = Db.getConn();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, keyword);
            ps.setString(2, keyword);
            ps.setString(3, keyword);
            ps.setString(4, keyword);

            List<Course> out = new ArrayList<>();
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Course x = new Course();
                    x.id = rs.getInt("id");
                    x.name = rs.getString("name");
                    x.duration = rs.getInt("duration");
                    x.instructor = rs.getString("instructor");
                    x.createAt = rs.getTimestamp("create_at");
                    out.add(x);
                }
            }
            return out;
        }
    }

    public void add(String name, int duration, String instructor) throws SQLException {
        if (name == null || name.isBlank()) throw new SQLException("Tên khóa học không được để trống!");
        if (duration <= 0) throw new SQLException("Thời lượng phải > 0!");
        if (instructor == null || instructor.isBlank()) throw new SQLException("Giảng viên không được để trống!");

        String sql = "INSERT INTO course(name, duration, instructor) VALUES (?, ?, ?)";
        try (Connection c = Db.getConn();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, name.trim());
            ps.setInt(2, duration);
            ps.setString(3, instructor.trim());
            ps.executeUpdate();
        }
    }

    public boolean exists(int id) throws SQLException {
        String sql = "SELECT 1 FROM course WHERE id = ?";
        try (Connection c = Db.getConn();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public void updateField(int id, String field, Object value) throws SQLException {
        // whitelist field
        if (!("name".equals(field) || "duration".equals(field) || "instructor".equals(field))) {
            throw new SQLException("Thuộc tính sửa không hợp lệ!");
        }

        String sql = "UPDATE course SET " + field + " = ? WHERE id = ?";
        try (Connection c = Db.getConn();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setObject(1, value);
            ps.setInt(2, id);
            int n = ps.executeUpdate();
            if (n == 0) throw new SQLException("Không tìm thấy khóa học id=" + id);
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM course WHERE id = ?";
        try (Connection c = Db.getConn();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}