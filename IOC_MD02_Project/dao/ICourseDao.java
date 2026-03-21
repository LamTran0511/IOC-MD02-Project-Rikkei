package IOC_MD02_Project.dao;

import IOC_MD02_Project.model.Course;
import java.sql.SQLException;
import java.util.List;

public interface ICourseDao {
    List<Course> list(String sortBy, String sortDir) throws SQLException;
    void add(String name, int duration, String instructor) throws SQLException;
    boolean exists(int id) throws SQLException;
    void updateField(int id, String field, Object value) throws SQLException;
    boolean deleteCourse(int id);
    List<Course> searchCourseByName(String name) throws SQLException;
    Course findCourseById(int id) throws SQLException;
}
