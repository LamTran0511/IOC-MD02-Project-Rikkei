package IOC_MD02_Project;

import java.sql.Date;
import java.sql.SQLException;

public class Main {

    static final AdminDao adminDao = new AdminDao();
    static final CourseDao courseDao = new CourseDao();
    static final StudentDao studentDao = new StudentDao();

    public static void main(String[] args) {
        while (true) {
            Input.clear();
            System.out.println("======== THỐNG QUẢN LÝ ĐÀO TẠO ========");
            System.out.println("1. Đăng nhập với tư cách Quản trị viên");
            System.out.println("2. Thoát");
            System.out.println("=======================================");
            int ch = Input.readInt("Nhập lựa chọn: ", 1, 2);
            if (ch == 1) adminLogin();
            else return;
        }
    }


    static void adminLogin() {
        while (true) {
            Input.clear();
            System.out.println("===== ĐĂNG NHẬP ADMIN =====");
            String user = Input.nonBlank("Username: ");
            String pass = Input.nonBlank("Password: ");

            try {
                Admin a = adminDao.login(user, pass);
                if (a == null) {
                    System.out.println("❌ Sai username/password! Nhập lại...");
                    Input.pause();
                    continue;
                }
                System.out.println("✅ Đăng nhập thành công!");
                Input.pause();
                adminMenu(a);
                return;
            } catch (SQLException e) {
                System.out.println("❌ Lỗi DB: " + e.getMessage());
                Input.pause();
                return;
            }
        }
    }

    static void adminMenu(Admin a) {
        while (true) {
            Input.clear();
            System.out.println("========== MENU ADMIN ==========");
            System.out.println("Xin chào, " + a.username);
            System.out.println("1. Quản lý khóa học");
            System.out.println("2. Quản lý học viên");
            System.out.println("3. Đăng xuất");
            System.out.println("================================");
            int ch = Input.readInt("Chọn: ", 1, 3);
            if (ch == 1) adminCourses();
            else if (ch == 2) adminStudents();
            else return;
        }
    }

    static void adminCourses() {
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
                    case 1 -> {
                        TablePrinter.courses(courseDao.list(null, "id", "ASC"));
                        Input.pause();
                    }
                    case 2 -> {
                        String name = Input.nonBlank("Tên khóa học: ");
                        int duration = Input.readInt("Thời lượng (giờ): ", 1, Integer.MAX_VALUE);
                        String instructor = Input.nonBlank("Giảng viên: ");
                        courseDao.add(name, duration, instructor);
                        System.out.println("✅ Thêm khóa học thành công!");
                        Input.pause();
                    }
                    case 3 -> {
                        int id = Input.readInt("Nhập id khóa học cần sửa: ", 1, Integer.MAX_VALUE);
                        if (!courseDao.exists(id)) {
                            System.out.println("❌ Không tìm thấy khóa học!");
                            Input.pause();
                            break;
                        }
                        System.out.println("Chọn thuộc tính: 1.name  2.duration  3.instructor");
                        int f = Input.readInt("Chọn: ", 1, 3);
                        if (f == 1) courseDao.updateField(id, "name", Input.nonBlank("Name mới: "));
                        else if (f == 2) courseDao.updateField(id, "duration", Input.readInt("Duration mới: ", 1, Integer.MAX_VALUE));
                        else courseDao.updateField(id, "instructor", Input.nonBlank("Instructor mới: "));
                        System.out.println("✅ Cập nhật thành công!");
                        Input.pause();
                    }
                    case 4 -> {
                        int id = Input.readInt("Nhập id cần xóa: ", 1, Integer.MAX_VALUE);
                        boolean ok = Input.yesNo("Xác nhận xóa? (y/n): ");
                        if (ok) {
                            courseDao.delete(id);
                            System.out.println("✅ Đã xóa!");
                        } else {
                            System.out.println("Đã hủy.");
                        }
                        Input.pause();
                    }
                    case 5 -> {
                        String kw = Input.nonBlank("Nhập từ khóa: ");
                        TablePrinter.courses(courseDao.list(kw, "id", "ASC"));
                        Input.pause();
                    }
                    case 6 -> {
                        String by = Input.choice("Sắp xếp theo (id/name): ", new String[]{"id","name"});
                        String dir = Input.choice("Chiều (ASC/DESC): ", new String[]{"ASC","DESC"});
                        TablePrinter.courses(courseDao.list(null, by, dir));
                        Input.pause();
                    }
                    case 7 -> { return; }
                }
            } catch (SQLException e) {
                System.out.println("❌ Lỗi: " + e.getMessage());
                Input.pause();
            }
        }
    }
    static void adminStudents() {
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
                    case 1 -> {
                        TablePrinter.students(studentDao.list(null, "id", "ASC"));
                        Input.pause();
                    }
                    case 2 -> {
                        String name = Input.nonBlank("Họ tên: ");
                        String dobStr = Input.nonBlank("DOB (yyyy-mm-dd): ");
                        Date dob = Date.valueOf(dobStr); // sai format sẽ throw -> catch bên dưới
                        String email = Input.nonBlank("Email: ");
                        int sex = Input.readInt("Giới tính (Nam=1, Nữ=0): ", 0, 1);
                        String phone = Input.optional("SĐT (có thể bỏ trống): ");
                        String pass = Input.nonBlank("Password: ");

                        studentDao.add(name, dob, email, sex == 1, phone, pass);
                        System.out.println("✅ Thêm học viên thành công!");
                        Input.pause();
                    }
                    case 3 -> {
                        int id = Input.readInt("Nhập id học viên cần sửa: ", 1, Integer.MAX_VALUE);
                        if (!studentDao.exists(id)) {
                            System.out.println("❌ Không tìm thấy học viên!");
                            Input.pause();
                            break;
                        }

                        System.out.println("Chọn thuộc tính: 1.name  2.dob  3.email  4.sex  5.phone");
                        int f = Input.readInt("Chọn: ", 1, 5);

                        switch (f) {
                            case 1 -> studentDao.updateField(id, "name", Input.nonBlank("Name mới: "));
                            case 2 -> studentDao.updateField(id, "dob", Date.valueOf(Input.nonBlank("DOB mới (yyyy-mm-dd): ")));
                            case 3 -> studentDao.updateField(id, "email", Input.nonBlank("Email mới: "));
                            case 4 -> studentDao.updateField(id, "sex", Input.readInt("Sex (Nam=1, Nữ=0): ", 0, 1) == 1);
                            case 5 -> studentDao.updateField(id, "phone", Input.optional("Phone mới (có thể bỏ trống): "));
                        }

                        System.out.println("✅ Cập nhật thành công!");
                        Input.pause();
                    }
                    case 4 -> {
                        int id = Input.readInt("Nhập id cần xóa: ", 1, Integer.MAX_VALUE);
                        boolean ok = Input.yesNo("Xác nhận xóa? (y/n): ");
                        if (ok) {
                            studentDao.delete(id);
                            System.out.println("✅ Đã xóa!");
                        } else {
                            System.out.println("Đã hủy.");
                        }
                        Input.pause();
                    }
                    case 5 -> {
                        String kw = Input.nonBlank("Nhập từ khóa: ");
                        TablePrinter.students(studentDao.list(kw, "id", "ASC"));
                        Input.pause();
                    }
                    case 6 -> {
                        String by = Input.choice("Sắp xếp theo (id/name): ", new String[]{"id", "name"});
                        String dir = Input.choice("Chiều (ASC/DESC): ", new String[]{"ASC", "DESC"});
                        TablePrinter.students(studentDao.list(null, by, dir));
                        Input.pause();
                    }
                    case 7 -> { return; }
                }
            } catch (IllegalArgumentException e) {
                System.out.println("❌ DOB sai định dạng! Nhập đúng yyyy-mm-dd.");
                Input.pause();
            } catch (SQLException e) {
                System.out.println("❌ Lỗi DB: " + e.getMessage());
                Input.pause();
            }
        }
    }
}