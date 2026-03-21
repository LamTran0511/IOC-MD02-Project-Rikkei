package IOC_MD02_Project.dao.impl;

import IOC_MD02_Project.dao.IStudentDao;
import IOC_MD02_Project.model.Student;
import IOC_MD02_Project.utils.ConnectionDB;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class StudentdaoImpl implements IStudentDao {

    private String safeSortBy(String sortBy) {
        if ("name".equalsIgnoreCase(sortBy)) {
            return "name";
        }
        return "id";
    }

    private String safeSortDir(String sortDir) {
        if ("DESC".equalsIgnoreCase(sortDir)) {
            return "DESC";
        }
        return "ASC";
    }

    @Override
    public List<Student> list(String sortBy, String sortDir) throws SQLException {
        sortBy = safeSortBy(sortBy);
        sortDir = safeSortDir(sortDir);

        String sql = "SELECT * " +
                "FROM student " +
                "ORDER BY " + sortBy + " " + sortDir + ", id ASC";

        List<Student> students = new ArrayList<>();

        try (Connection conn = ConnectionDB.getConn();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Student s = new Student();
                s.setId(rs.getInt("id"));
                s.setName(rs.getString("name"));
                s.setDob(rs.getDate("dob"));
                s.setEmail(rs.getString("email"));
                s.setSex(rs.getBoolean("sex"));
                s.setPhone(rs.getString("phone"));
                s.setPassword(rs.getString("password"));
                s.setCreateAt(rs.getTimestamp("created_at"));
                students.add(s);
            }
        }

        return students;
    }

//    public Student findById(int id) {
//        String sql = " SELECT * FROM student WHERE id = ? ";
//
//        try (Connection conn = ConnectionDB.getConnection();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//
//            ps.setInt(1, id);
//            ResultSet rs = ps.executeQuery();
//
//            if (rs.next()) {
//                return new Student(
//                        rs.getInt("id"),
//                        rs.getString("name"),
//                        rs.getDate("dob").toLocalDate(),
//                        rs.getString("email"),
//                        rs.getBoolean("sex"),
//                        rs.getString("phone"),
//                        rs.getString("password"),
//                        rs.getDate("created_at").toLocalDate()
//                );
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    @Override
    public List<Student> searchStudent(String strSt) throws SQLException {
        String sql = "SELECT * " +
                "FROM student " +
                "WHERE name ILIKE ? " +
                "OR email ILIKE ? " +
                "OR phone ILIKE ? " +
                "OR CAST(id AS TEXT) ILIKE ? " +
                "ORDER BY id ASC";

        List<Student> students = new ArrayList<>();

        try (Connection conn = ConnectionDB.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String value = "%" + strSt.trim() + "%";
            ps.setString(1, value);
            ps.setString(2, value);
            ps.setString(3, value);
            ps.setString(4, value);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Student s = new Student();
                    s.setId(rs.getInt("id"));
                    s.setName(rs.getString("name"));
                    s.setDob(rs.getDate("dob"));
                    s.setEmail(rs.getString("email"));
                    s.setSex(rs.getBoolean("sex"));
                    s.setPhone(rs.getString("phone"));
                    s.setPassword(rs.getString("password"));
                    s.setCreateAt(rs.getTimestamp("created_at"));
                    students.add(s);
                }
            }
        }

        return students;
    }

    @Override
    public void add(String name, Date dob, String email, boolean sex, String phone, String password) throws SQLException {
        if (name == null || name.isBlank()) {
            throw new SQLException("Tên học viên không được để trống!");
        }
        if (dob == null) {
            throw new SQLException("Ngày sinh không hợp lệ!");
        }
        if (email == null || email.isBlank()) {
            throw new SQLException("Email không được để trống!");
        }
        if (password == null || password.isBlank()) {
            throw new SQLException("Password không được để trống!");
        }

        String sql = "INSERT INTO student(name, dob, email, sex, phone, password) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionDB.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name.trim());
            ps.setDate(2, dob);
            ps.setString(3, email.trim());
            ps.setBoolean(4, sex);

            if (phone == null || phone.isBlank()) {
                ps.setNull(5, Types.VARCHAR);
            } else {
                ps.setString(5, phone.trim());
            }

            ps.setString(6, password);
            ps.executeUpdate();
        }
    }

    @Override
    public boolean exists(int id) throws SQLException {
        String sql = "SELECT 1 FROM student WHERE id = ?";

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
        if (!"name".equals(field)
                && !"dob".equals(field)
                && !"email".equals(field)
                && !"sex".equals(field)
                && !"phone".equals(field)) {
            throw new SQLException("Thuộc tính sửa không hợp lệ!");
        }

        String sql = "UPDATE student SET " + field + " = ? WHERE id = ?";

        try (Connection conn = ConnectionDB.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if ("phone".equals(field) && (value == null || value.toString().isBlank())) {
                ps.setNull(1, Types.VARCHAR);
            } else {
                ps.setObject(1, value);
            }

            ps.setInt(2, id);

            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new SQLException("Không tìm thấy học viên id = " + id);
            }
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM student WHERE id = ?";

        try (Connection conn = ConnectionDB.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new SQLException("Không tìm thấy học viên để xóa!");
            }
        }
    }

    @Override
    public boolean findStudentByEmail(String email) throws SQLException {
        String sql = "SELECT 1 FROM student WHERE email = ?";

        try (Connection conn = ConnectionDB.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public Student login(String key, String password) throws SQLException {
        String sql = "SELECT * " +
                "FROM student WHERE (email = ? OR phone = ?) AND password = ?";

        try (Connection conn = ConnectionDB.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, key);
            ps.setString(2, key);
            ps.setString(3, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                Student s = new Student();
                s.setId(rs.getInt("id"));
                s.setName(rs.getString("name"));
                s.setDob(rs.getDate("dob"));
                s.setEmail(rs.getString("email"));
                s.setSex(rs.getBoolean("sex"));
                s.setPhone(rs.getString("phone"));
                s.setPassword(rs.getString("password"));
                s.setCreateAt(rs.getTimestamp("created_at"));
                return s;
            }
        }
    }

    @Override
    public void changePasswordWithVerify(int studentId, boolean byEmail, String verify, String oldPass, String newPass)
            throws SQLException {

        String field = byEmail ? "email" : "phone";

        String sqlCheck = "SELECT 1 FROM student WHERE id = ? AND " + field + " = ? AND password = ?";
        String sqlUpdate = "UPDATE student SET password = ? WHERE id = ?";

        try (Connection conn = ConnectionDB.getConn()) {
            conn.setAutoCommit(false);

            try (PreparedStatement checkPs = conn.prepareStatement(sqlCheck);
                 PreparedStatement updatePs = conn.prepareStatement(sqlUpdate)) {

                checkPs.setInt(1, studentId);
                checkPs.setString(2, verify);
                checkPs.setString(3, oldPass);

                boolean ok;
                try (ResultSet rs = checkPs.executeQuery()) {
                    ok = rs.next();
                }

                if (!ok) {
                    conn.rollback();
                    throw new SQLException("Xác thực thất bại (email/sđt hoặc mật khẩu cũ sai).");
                }

                updatePs.setString(1, newPass);
                updatePs.setInt(2, studentId);
                updatePs.executeUpdate();

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }
}