package IOC_MD02_Project.model;

import java.sql.Timestamp;

public class Enrollment {
    private int enrollId;
    private int courseId;
    private String courseName;
    private int duration;
    private String instructor;
    private Timestamp registeredAt;
    private String status;

    public Enrollment() {
    }

    public Enrollment(int enrollId, int courseId, String courseName, int duration, String instructor, Timestamp registeredAt, String status) {
        this.enrollId = enrollId;
        this.courseId = courseId;
        this.courseName = courseName;
        this.duration = duration;
        this.instructor = instructor;
        this.registeredAt = registeredAt;
        this.status = status;
    }

    public int getEnrollId() {
        return enrollId;
    }

    public void setEnrollId(int enrollId) {
        this.enrollId = enrollId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public Timestamp getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(Timestamp registeredAt) {
        this.registeredAt = registeredAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}