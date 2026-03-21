package IOC_MD02_Project.utils;

import IOC_MD02_Project.model.Course;
import IOC_MD02_Project.model.Student;
import IOC_MD02_Project.model.Enrollment;

import java.util.List;

public class TablePrinter {
    private static String cut(String s, int max) {
        if (s == null) return "";
        if (s.length() <= max) return s;
        return s.substring(0, Math.max(0, max - 1)) + "…";
    }

    public static void courses(List<Course> list) {
        System.out.println("\n+------+------------------------------+----------+----------------------+------------------------------+");
        System.out.printf("| %-4s | %-28s | %-8s | %-20s | %-28s |\n", "ID", "Tên khóa học", "Giờ", "Giảng viên", "Ngày tạo");
        System.out.println("+------+------------------------------+----------+----------------------+------------------------------+");
        for (Course c : list) {
            System.out.printf("| %-4d | %-28s | %-8d | %-20s | %-28s |\n",
                    c.getId(), cut(c.getName(), 28), c.getDuration(), cut(c.getInstructor(), 20), String.valueOf(c.getCreateAt()));
        }
        System.out.println("+------+------------------------------+----------+----------------------+------------------------------+");
        System.out.println("Tổng: " + list.size());
    }

    public static void students(java.util.List<Student> list) {
        System.out.println("\n+------+------------------------------+------------+--------------------------+-----+--------------+------------------------------+");
        System.out.printf("| %-4s | %-28s | %-10s | %-24s | %-3s | %-12s | %-28s |\n",
                "ID", "Họ tên", "DOB", "Email", "GT", "SĐT", "Ngày tạo");
        System.out.println("+------+------------------------------+------------+--------------------------+-----+--------------+------------------------------+");

        for (Student s : list) {
            System.out.printf("| %-4d | %-28s | %-10s | %-24s | %-3s | %-12s | %-28s |\n",
                    s.getId(),
                    cut(s.getName(), 28),
                    String.valueOf(s.getDob()),
                    cut(s.getEmail(), 24),
                    s.sex ? "Nam" : "Nu",
                    s.getPhone() == null ? "" : cut(s.getPhone(), 12),
                    String.valueOf(s.getCreateAt()));
        }

        System.out.println("+------+------------------------------+------------+--------------------------+-----+--------------+------------------------------+");
        System.out.println("Tổng: " + list.size());
    }

    public static void studentRegistered(java.util.List<Enrollment> list) {
        System.out.println("\n+---------+---------+------------------------------+----------+----------------------+-------------------------+-----------+");
        System.out.printf("| %-7s | %-7s | %-28s | %-8s | %-20s | %-23s | %-9s |\n",
                "Enroll", "CID", "Khóa học", "Giờ", "Giảng viên", "Ngày đăng ký", "Status");
        System.out.println("+---------+---------+------------------------------+----------+----------------------+-------------------------+-----------+");
        for (Enrollment r : list) {
            System.out.printf("| %-7d | %-7d | %-28s | %-8d | %-20s | %-23s | %-9s |\n",
                    r.getEnrollId(), r.getCourseId(), cut(r.getCourseName(), 28), r.getDuration(),
                    cut(r.getInstructor(), 20), String.valueOf(r.getRegisteredAt()), r.getStatus());
        }
        System.out.println("+---------+---------+------------------------------+----------+----------------------+-------------------------+-----------+");
        System.out.println("Tổng: " + list.size());
    }
}