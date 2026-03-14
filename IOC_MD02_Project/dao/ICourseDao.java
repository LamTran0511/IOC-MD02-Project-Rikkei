package IOC_MD02_Project.dao;

import IOC_MD02_Project.model.Course;

import java.sql.SQLException;
import java.util.List;

public interface ICourseDao {
    List<Course> list(String keyword, String sortBy, String sortDir) throws SQLException;
    void add(String name, int duration, String instructor) throws SQLException;
    boolean exists(int id) throws SQLException;
    void updateField(int id, String field, Object value) throws SQLException;
    void delete(int id) throws SQLException;
}