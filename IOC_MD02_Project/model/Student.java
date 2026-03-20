package IOC_MD02_Project.model;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;

public class Student {
    public int id;
    public String name;
    public Date dob;
    public String email;
    public boolean sex;
    public String phone;
    public String password;
    public Timestamp createAt;


    public Student(int id, String name, LocalDate dob, String email, boolean sex, String phone, String password, LocalDate createdAt) {
    }

    public Student() {

    }
}