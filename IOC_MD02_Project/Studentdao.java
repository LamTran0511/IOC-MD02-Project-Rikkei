package IOC_MD02_Project;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Studentdao {
    private static final String[] SORT_WHITELIST = {"id", "name"};

    private String safeSortBy(String sortBy) {
        if (sortBy == null) return "id";
        for (String s : SORT_WHITELIST) if (s.equalsIgnoreCase(sortBy)) return s;
        return "id";
    }

    private String safeSortDir(String dir) {
        return "DESC".equalsIgnoreCase(dir) ? "DESC" : "ASC";
    }

    public List<Student> list(String keyword, String sortBy, String sortDir) throws SQLException {
        sortBy = safeSortBy(sortBy);
        sortDir = safeSortDir(sortDir);

        String sql =
                "SELECT id, name, dob, email, sex, phone, password, create_at " +
                        "FROM student " +
                        "WHERE (? IS NULL OR ? = '') " +
                        "   OR (name ILIKE '%'||?||'%' " +
                        "   OR  email ILIKE '%'||?||'%' " +
                        "   OR  CAST(id AS TEXT) ILIKE '%'||?||'%') " +
                        "ORDER BY " + sortBy + " " + sortDir + ", id ASC";

        try (Connection c = Db.getConn();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, keyword);
            ps.setString(2, keyword);
            ps.setString(3, keyword);
            ps.setString(4, keyword);
            ps.setString(5, keyword);

            List<Student> out = new ArrayList<>();
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Student s = new Student();
                    s.id = rs.getInt("id");
                    s.name = rs.getString("name");
                    s.dob = rs.getDate("dob");
                    s.email = rs.getString("email");
                    s.sex = rs.getBoolean("sex");
                    s.phone = rs.getString("phone");
                    s.password = rs.getString("password");
                    s.createAt = rs.getTimestamp("create_at");
                    out.add(s);
                }
            }
            return out;
        }
    }

    public void add(String name, Date dob, String email, boolean sex, String phone, String password) throws SQLException {
        if (name == null || name.isBlank()) throw new SQLException("Tên học viên không được để trống!");
        if (dob == null) throw new SQLException("DOB không hợp lệ!");
        if (email == null || email.isBlank()) throw new SQLException("Email không được để trống!");
        if (password == null || password.isBlank()) throw new SQLException("Password không được để trống!");

        String sql = "INSERT INTO student(name, dob, email, sex, phone, password) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection c = Db.getConn();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, name.trim());
            ps.setDate(2, dob);
            ps.setString(3, email.trim());
            ps.setBoolean(4, sex);
            if (phone == null || phone.isBlank()) ps.setNull(5, Types.VARCHAR);
            else ps.setString(5, phone.trim());
            ps.setString(6, password);
            ps.executeUpdate();
        }
    }

    public boolean exists(int id) throws SQLException {
        String sql = "SELECT 1 FROM student WHERE id = ?";
        try (Connection c = Db.getConn();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        }
    }

    public void updateField(int id, String field, Object value) throws SQLException {
        if (!("name".equals(field) || "dob".equals(field) || "email".equals(field) ||
                "sex".equals(field) || "phone".equals(field))) {
            throw new SQLException("Thuộc tính sửa không hợp lệ!");
        }

        String sql = "UPDATE student SET " + field + " = ? WHERE id = ?";
        try (Connection c = Db.getConn();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setObject(1, value);
            ps.setInt(2, id);
            int n = ps.executeUpdate();
            if (n == 0) throw new SQLException("Không tìm thấy học viên id=" + id);
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM student WHERE id = ?";
        try (Connection c = Db.getConn();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public Student login(String key, String password) throws SQLException {
        String sql =
                "SELECT id, name, dob, email, sex, phone, password, create_at " +
                        "FROM student WHERE (email=? OR phone=?) AND password=?";
        try (Connection c = Db.getConn();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, key);
            ps.setString(2, key);
            ps.setString(3, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                Student s = new Student();
                s.id = rs.getInt("id");
                s.name = rs.getString("name");
                s.dob = rs.getDate("dob");
                s.email = rs.getString("email");
                s.sex = rs.getBoolean("sex");
                s.phone = rs.getString("phone");
                s.password = rs.getString("password");
                s.createAt = rs.getTimestamp("create_at");
                return s;
            }
        }
    }

    public void changePasswordWithVerify(int studentId, boolean byEmail,
                                         String verifyValue, String oldPass, String newPass) throws SQLException {
        String field = byEmail ? "email" : "phone";
        String sqlCheck = "SELECT 1 FROM student WHERE id=? AND " + field + "=? AND password=?";
        String sqlUpd = "UPDATE student SET password=? WHERE id=?";
        try (Connection c = Db.getConn()) {
            c.setAutoCommit(false);
            try (PreparedStatement check = c.prepareStatement(sqlCheck);
                 PreparedStatement upd = c.prepareStatement(sqlUpd)) {
                check.setInt(1, studentId);
                check.setString(2, verifyValue);
                check.setString(3, oldPass);

                boolean ok;
                try (ResultSet rs = check.executeQuery()) { ok = rs.next(); }
                if (!ok) {
                    c.rollback();
                    throw new SQLException("Xác thực thất bại (email/sđt hoặc mật khẩu cũ sai).");
                }

                upd.setString(1, newPass);
                upd.setInt(2, studentId);
                upd.executeUpdate();
                c.commit();
            } catch (SQLException e) {
                c.rollback();
                throw e;
            } finally {
                c.setAutoCommit(true);
            }
        }
    }
}