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
}