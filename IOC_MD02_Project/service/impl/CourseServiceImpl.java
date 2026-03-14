package IOC_MD02_Project.service.impl;

import IOC_MD02_Project.dao.impl.CoursedaoImpl;
import IOC_MD02_Project.model.Course;
import IOC_MD02_Project.service.ICourseService;

import java.sql.SQLException;
import java.util.List;

public class CourseServiceImpl implements ICourseService {
    private final CoursedaoImpl courseDao = new CoursedaoImpl();

    @Override
    public List<Course> list(String keyword, String sortBy, String sortDir) throws SQLException {
        return courseDao.list(keyword, sortBy, sortDir);
    }

    @Override
    public void add(String name, int duration, String instructor) throws SQLException {
        courseDao.add(name, duration, instructor);
    }

    @Override
    public boolean exists(int id) throws SQLException {
        return courseDao.exists(id);
    }

    @Override
    public void updateField(int id, String field, Object value) throws SQLException {
        courseDao.updateField(id, field, value);
    }

    @Override
    public void delete(int id) throws SQLException {
        courseDao.delete(id);
    }
}