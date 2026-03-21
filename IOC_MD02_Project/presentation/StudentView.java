package IOC_MD02_Project.presentation;

import IOC_MD02_Project.model.Course;
import IOC_MD02_Project.model.Student;
import IOC_MD02_Project.service.ICourseService;
import IOC_MD02_Project.service.IEnrollmentService;
import IOC_MD02_Project.service.IStudentService;
import IOC_MD02_Project.service.impl.CourseServiceImpl;
import IOC_MD02_Project.service.impl.EnrollmentServiceImpl;
import IOC_MD02_Project.service.impl.StudentServiceImpl;
import IOC_MD02_Project.utils.Input;
import IOC_MD02_Project.utils.TablePrinter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentView {
    private static final IStudentService studentService = new StudentServiceImpl();
    private static final ICourseService courseService = new CourseServiceImpl();
    private static final IEnrollmentService enrollmentService = new EnrollmentServiceImpl();

    public static void login() {
        while (true) {
            Input.clear();
            System.out.println("===== ĐĂNG NHẬP HỌC VIÊN =====");
            System.out.println("(Nhập email hoặc số điện thoại)");

            String key = Input.nonBlank("Email/SĐT: ");
            String pass = Input.nonBlank("Password: ");

            try {
                Student s = studentService.login(key, pass);
                if (s == null) {
                    System.out.println("Sai thông tin đăng nhập! Nhập lại...");
                    Input.pause();
                    continue;
                }

                System.out.println("Đăng nhập thành công!");
                Input.pause();
                menu(s);
                return;
            } catch (SQLException e) {
                System.out.println("Lỗi DB: " + e.getMessage());
                Input.pause();
                return;
            }
        }
    }

    private static void menu(Student s) {
        while (true) {
            Input.clear();
            System.out.println("========== MENU HỌC VIÊN ==========");
            System.out.println("Xin chào, " + s.getName() + " (" + s.getEmail() + ")");
            System.out.println("1. Xem danh sách khóa học");
            System.out.println("2. Tìm kiếm khóa học");
            System.out.println("3. Sắp xếp danh sách khóa học");
            System.out.println("4. Đăng ký khóa học");
            System.out.println("5. Xem khóa học đã đăng ký + sắp xếp");
            System.out.println("6. Hủy đăng ký (chỉ khi WAITING)");
            System.out.println("7. Đổi mật khẩu");
            System.out.println("8. Đăng xuất");
            System.out.println("===================================");

            int ch = Input.readInt("Chọn: ", 1, 8);

            try {
                switch (ch) {
                    case 1:
                        TablePrinter.courses(courseService.list("id", "ASC"));
                        Input.pause();
                        break;

                    case 2: {
                        searchCourseMenu();
                        break;
                    }

                    case 3: {
                        String by = Input.choice("Sắp xếp theo (id/name): ", new String[]{"id", "name"});
                        String dir = Input.choice("Chiều (ASC/DESC): ", new String[]{"ASC", "DESC"});

                        TablePrinter.courses(courseService.list(by, dir));
                        Input.pause();
                        break;
                    }

                    case 4: {
                        int courseId = Input.readInt("Nhập course_id muốn đăng ký: ", 1, Integer.MAX_VALUE);
                        enrollmentService.register(s.getId(), courseId);
                        System.out.println("Đăng ký thành công (WAITING)!");
                        Input.pause();
                        break;
                    }

                    case 5: {
                        String by = Input.choice("Sắp xếp theo (NAME/DATE): ", new String[]{"NAME", "DATE"});
                        String dir = Input.choice("Chiều (ASC/DESC): ", new String[]{"ASC", "DESC"});

                        TablePrinter.studentRegistered(enrollmentService.listRegisteredCourses(s.getId(), by, dir));
                        Input.pause();
                        break;
                    }

                    case 6: {
                        int courseId = Input.readInt("Nhập course_id muốn hủy: ", 1, Integer.MAX_VALUE);
                        enrollmentService.cancelIfWaiting(s.getId(), courseId);
                        System.out.println("Đã hủy (CANCEL)!");
                        Input.pause();
                        break;
                    }

                    case 7: {
                        System.out.println("Xác thực đổi mật khẩu:");
                        System.out.println("1. Email");
                        System.out.println("2. SĐT");

                        boolean byEmail = Input.readInt("Chọn: ", 1, 2) == 1;
                        String verify = Input.nonBlank(byEmail ? "Nhập Email: " : "Nhập SĐT: ");
                        String oldPass = Input.nonBlank("Mật khẩu cũ: ");
                        String newPass = Input.nonBlank("Mật khẩu mới: ");

                        studentService.changePasswordWithVerify(s.getId(), byEmail, verify, oldPass, newPass);
                        System.out.println("Đổi mật khẩu thành công!");
                        Input.pause();
                        break;
                    }

                    case 8:
                        return;

                    default:
                        break;
                }
            } catch (SQLException e) {
                System.out.println("Lỗi: " + e.getMessage());
                Input.pause();
            }
        }
    }
    private static void searchCourseMenu() {
        while (true) {
            System.out.println("\n===== TÌM KIẾM KHÓA HỌC =====");
            System.out.println("1. Tìm theo ID");
            System.out.println("2. Tìm theo tên");
            System.out.println("3. Quay lại");
            int choice = Input.readInt("Chọn: ", 1, 3);

            switch (choice) {
                case 1:
                    int id = Input.readInt("Nhập id: ", 1, Integer.MAX_VALUE);
                    Course course = courseService.findCourseById(id);

                    List<Course> courseList = new ArrayList<>();
                    if (course != null) {
                        courseList.add(course);
                    }

                    TablePrinter.courses(courseList);
                    break;
                case 2:
                    String name = Input.nonBlank("Nhập tên: ");
                    TablePrinter.courses(courseService.searchCourseByName(name));
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }
}