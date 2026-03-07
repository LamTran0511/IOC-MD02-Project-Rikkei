package IOC_MD02_Project;

import java.sql.Date;
import java.sql.Timestamp;

public class Student {
    public int id;
    public String name;
    public Date dob;
    public String email;
    public boolean sex;        // true=Nam, false=Nữ
    public String phone;       // nullable
    public String password;    // plain text (theo bài JDBC cơ bản)
    public Timestamp createAt;
}