package IOC_MD02_Project;

import java.util.List;

public class TablePrinter {
    private static String cut(String s, int max) {
        if (s == null) return "";
        if (s.length() <= max) return s;
        return s.substring(0, Math.max(0, max - 1)) + "…";
    }

    public static void courses(List<Course> list) {
        System.out.println("\n+------+------------------------------+----------+----------------------+-------------------------+");
        System.out.printf("| %-4s | %-28s | %-8s | %-20s | %-23s |\n", "ID", "Tên khóa học", "Giờ", "Giảng viên", "Ngày tạo");
        System.out.println("+------+------------------------------+----------+----------------------+-------------------------+");
        for (Course c : list) {
            System.out.printf("| %-4d | %-28s | %-8d | %-20s | %-23s |\n",
                    c.id, cut(c.name, 28), c.duration, cut(c.instructor, 20), String.valueOf(c.createAt));
        }
        System.out.println("+------+------------------------------+----------+----------------------+-------------------------+");
        System.out.println("Tổng: " + list.size());
    }

    public static void students(java.util.List<IOC_MD02_Project.Student> list) {
        System.out.println("\n+------+------------------------------+------------+--------------------------+-----+--------------+-------------------------+");
        System.out.printf("| %-4s | %-28s | %-10s | %-24s | %-3s | %-12s | %-23s |\n",
                "ID", "Họ tên", "DOB", "Email", "GT", "SĐT", "Ngày tạo");
        System.out.println("+------+------------------------------+------------+--------------------------+-----+--------------+-------------------------+");

        for (IOC_MD02_Project.Student s : list) {
            System.out.printf("| %-4d | %-28s | %-10s | %-24s | %-3s | %-12s | %-23s |\n",
                    s.id,
                    cut(s.name, 28),
                    String.valueOf(s.dob),
                    cut(s.email, 24),
                    s.sex ? "Nam" : "Nu",
                    s.phone == null ? "" : cut(s.phone, 12),
                    String.valueOf(s.createAt));
        }

        System.out.println("+------+------------------------------+------------+--------------------------+-----+--------------+-------------------------+");
        System.out.println("Tổng: " + list.size());
    }

    public static void studentRegistered(java.util.List<IOC_MD02_Project.StudentCourseRow> list) {
        System.out.println("\n+---------+---------+------------------------------+----------+----------------------+-------------------------+-----------+");
        System.out.printf("| %-7s | %-7s | %-28s | %-8s | %-20s | %-23s | %-9s |\n",
                "Enroll", "CID", "Khóa học", "Giờ", "Giảng viên", "Ngày đăng ký", "Status");
        System.out.println("+---------+---------+------------------------------+----------+----------------------+-------------------------+-----------+");
        for (IOC_MD02_Project.StudentCourseRow r : list) {
            System.out.printf("| %-7d | %-7d | %-28s | %-8d | %-20s | %-23s | %-9s |\n",
                    r.enrollId, r.courseId, cut(r.courseName, 28), r.duration,
                    cut(r.instructor, 20), String.valueOf(r.registeredAt), r.status);
        }
        System.out.println("+---------+---------+------------------------------+----------+----------------------+-------------------------+-----------+");
        System.out.println("Tổng: " + list.size());
    }
}