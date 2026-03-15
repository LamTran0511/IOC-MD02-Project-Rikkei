package IOC_MD02_Project.presentation;

import IOC_MD02_Project.model.Admin;
import IOC_MD02_Project.service.IAdminService;
import IOC_MD02_Project.service.ICourseService;
import IOC_MD02_Project.service.IStudentService;
import IOC_MD02_Project.service.impl.AdminServiceImpl;
import IOC_MD02_Project.service.impl.CourseServiceImpl;
import IOC_MD02_Project.service.impl.StudentServiceImpl;
import IOC_MD02_Project.InputVallidate.Input;
import IOC_MD02_Project.utils.TablePrinter;

import java.sql.Date;
import java.sql.SQLException;

public class AdminView {

    private static final IAdminService adminService = new AdminServiceImpl();
    private static final ICourseService courseService = new CourseServiceImpl();
    private static final IStudentService studentService = new StudentServiceImpl();

    public static void login() {
        while (true) {
            Input.clear();
            System.out.println("===== ĐĂNG NHẬP ADMIN =====");
            String user = Input.nonBlank("Username: ");
            String pass = Input.nonBlank("Password: ");

            try {
                Admin a = adminService.login(user, pass);
                if (a == null) {
                    System.out.println("Sai username/password! Nhập lại...");
                    Input.pause();
                    continue;
                }
                System.out.println("Đăng nhập thành công!");
                Input.pause();
                menu(a);
                return;
            } catch (SQLException e) {
                System.out.println("Lỗi DB: " + e.getMessage());
                Input.pause();
                return;
            }
        }
    }

    private static void menu(Admin a) {
        while (true) {
            Input.clear();
            System.out.println("========== MENU ADMIN ==========");
            System.out.println("Xin chào, " + a.username);
            System.out.println("1. Quản lý khóa học");
            System.out.println("2. Quản lý học viên");
            System.out.println("3. Đăng xuất");
            System.out.println("================================");
            int ch = Input.readInt("Chọn: ", 1, 3);

            switch (ch) {
                case 1:
                    courseMenu();
                    break;
                case 2:
                    studentMenu();
                    break;
                case 3:
                    return;
                default:
                    break;
            }
        }
    }

    private static void courseMenu() {
        while (true) {
            Input.clear();
            System.out.println("===== QUẢN LÝ KHÓA HỌC (ADMIN) =====");
            System.out.println("1. Hiển thị danh sách khóa học");
            System.out.println("2. Thêm mới khóa học");
            System.out.println("3. Chỉnh sửa khóa học (chọn thuộc tính)");
            System.out.println("4. Xóa khóa học theo id (xác nhận)");
            System.out.println("5. Tìm kiếm khóa học theo tên/id (tương đối)");
            System.out.println("6. Sắp xếp khóa học (theo tên/id - tăng/giảm)");
            System.out.println("7. Quay về menu admin");
            System.out.println("===================================");
            int ch = Input.readInt("Chọn: ", 1, 7);

            try {
                switch (ch) {
                    case 1:
                        TablePrinter.courses(courseService.list(null, "id", "ASC"));
                        Input.pause();
                        break;

                    case 2: {
                        String name = Input.nonBlank("Tên khóa học: ");
                        int duration = Input.readInt("Thời lượng (giờ): ", 1, Integer.MAX_VALUE);
                        String instructor = Input.nonBlank("Giảng viên: ");
                        courseService.add(name, duration, instructor);
                        System.out.println("Thêm khóa học thành công!");
                        Input.pause();
                        break;
                    }

                    case 3: {
                        int id = Input.readInt("Nhập id khóa học cần sửa: ", 1, Integer.MAX_VALUE);
                        if (!courseService.exists(id)) {
                            System.out.println("Không tìm thấy khóa học!");
                            Input.pause();
                            break;
                        }
                        System.out.println("Chọn thuộc tính: 1.name  2.duration  3.instructor");
                        int f = Input.readInt("Chọn: ", 1, 3);

                        if (f == 1) courseService.updateField(id, "name", Input.nonBlank("Name mới: "));
                        else if (f == 2) courseService.updateField(id, "duration", Input.readInt("Duration mới: ", 1, Integer.MAX_VALUE));
                        else courseService.updateField(id, "instructor", Input.nonBlank("Instructor mới: "));

                        System.out.println("Cập nhật thành công!");
                        Input.pause();
                        break;
                    }

                    case 4: {
                        int id = Input.readInt("Nhập id cần xóa: ", 1, Integer.MAX_VALUE);
                        boolean ok = Input.yesNo("Xác nhận xóa? (y/n): ");
                        if (ok) {
                            courseService.delete(id);
                            System.out.println("Đã xóa!");
                        } else System.out.println("Đã hủy.");
                        Input.pause();
                        break;
                    }

                    case 5: {
                        String kw = Input.nonBlank("Nhập từ khóa: ");
                        TablePrinter.courses(courseService.list(kw, "id", "ASC"));
                        Input.pause();
                        break;
                    }

                    case 6: {
                        String by = Input.choice("Sắp xếp theo (id/name): ", new String[]{"id", "name"});
                        String dir = Input.choice("Chiều (ASC/DESC): ", new String[]{"ASC", "DESC"});
                        TablePrinter.courses(courseService.list(null, by, dir));
                        Input.pause();
                        break;
                    }

                    case 7:
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

    private static void studentMenu() {
        while (true) {
            Input.clear();
            System.out.println("===== QUẢN LÝ HỌC VIÊN (ADMIN) =====");
            System.out.println("1. Hiển thị danh sách học viên");
            System.out.println("2. Thêm mới học viên");
            System.out.println("3. Chỉnh sửa học viên (chọn thuộc tính)");
            System.out.println("4. Xóa học viên theo id (xác nhận)");
            System.out.println("5. Tìm kiếm theo tên/email/id (tương đối)");
            System.out.println("6. Sắp xếp theo tên/id (tăng/giảm)");
            System.out.println("7. Quay về menu admin");
            System.out.println("===================================");
            int ch = Input.readInt("Chọn: ", 1, 7);

            try {
                switch (ch) {
                    case 1:
                        TablePrinter.students(studentService.list(null, "id", "ASC"));
                        Input.pause();
                        break;

                    case 2: {
                        String name = Input.nonBlank("Họ tên: ");
                        Date dob = Date.valueOf(Input.nonBlank("DOB (yyyy-mm-dd): "));
                        String email = Input.nonBlank("Email: ");
                        int sex = Input.readInt("Giới tính (Nam=1, Nữ=0): ", 0, 1);
                        String phone = Input.optional("SĐT (có thể bỏ trống): ");
                        String pass = Input.nonBlank("Password: ");

                        studentService.add(name, dob, email, sex == 1, phone, pass);
                        System.out.println("Thêm học viên thành công!");
                        Input.pause();
                        break;
                    }

                    case 3: {
                        int id = Input.readInt("Nhập id học viên cần sửa: ", 1, Integer.MAX_VALUE);
                        if (!studentService.exists(id)) {
                            System.out.println("Không tìm thấy học viên!");
                            Input.pause();
                            break;
                        }

                        System.out.println("Chọn thuộc tính: 1.name  2.dob  3.email  4.sex  5.phone");
                        int f = Input.readInt("Chọn: ", 1, 5);

                        switch (f) {
                            case 1:
                                studentService.updateField(id, "name", Input.nonBlank("Name mới: "));
                                break;
                            case 2:
                                studentService.updateField(id, "dob", Date.valueOf(Input.nonBlank("DOB mới (yyyy-mm-dd): ")));
                                break;
                            case 3:
                                studentService.updateField(id, "email", Input.nonBlank("Email mới: "));
                                break;
                            case 4:
                                studentService.updateField(id, "sex", Input.readInt("Sex (Nam=1, Nữ=0): ", 0, 1) == 1);
                                break;
                            case 5:
                                studentService.updateField(id, "phone", Input.optional("Phone mới (có thể bỏ trống): "));
                                break;
                            default:
                                break;
                        }

                        System.out.println("Cập nhật thành công!");
                        Input.pause();
                        break;
                    }

                    case 4: {
                        int id = Input.readInt("Nhập id cần xóa: ", 1, Integer.MAX_VALUE);
                        boolean ok = Input.yesNo("Xác nhận xóa? (y/n): ");
                        if (ok) {
                            studentService.delete(id);
                            System.out.println("Đã xóa!");
                        } else System.out.println("Đã hủy.");
                        Input.pause();
                        break;
                    }

                    case 5: {
                        String kw = Input.nonBlank("Nhập từ khóa: ");
                        TablePrinter.students(studentService.list(kw, "id", "ASC"));
                        Input.pause();
                        break;
                    }

                    case 6: {
                        String by = Input.choice("Sắp xếp theo (id/name): ", new String[]{"id", "name"});
                        String dir = Input.choice("Chiều (ASC/DESC): ", new String[]{"ASC", "DESC"});
                        TablePrinter.students(studentService.list(null, by, dir));
                        Input.pause();
                        break;
                    }

                    case 7:
                        return;
                    default:
                        break;
                }
            } catch (IllegalArgumentException e) {
                System.out.println("DOB sai định dạng! Nhập đúng yyyy-mm-dd.");
                Input.pause();
            } catch (SQLException e) {
                System.out.println("Lỗi DB: " + e.getMessage());
                Input.pause();
            }
        }
    }
}