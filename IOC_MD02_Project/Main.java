package IOC_MD02_Project;


import IOC_MD02_Project.presentation.AdminView;
import IOC_MD02_Project.presentation.StudentView;
import IOC_MD02_Project.utils.Input;

public class Main {
    public static void main(String[] args) {
        while (true) {
            Input.clear();
            System.out.println("======== THỐNG QUẢN LÝ ĐÀO TẠO ========");
            System.out.println("1. Đăng nhập với tư cách Quản trị viên");
            System.out.println("2. Đăng nhập với tư cách Học viên");
            System.out.println("3. Thoát");
            System.out.println("=======================================");
            int ch = Input.readInt("Nhập lựa chọn: ", 1, 3);

            switch (ch) {
                case 1:
                    AdminView.login();
                    break;
                case 2:
                    StudentView.login();
                    break;
                case 3:
                    return;
                default:
                    break;
            }
        }
    }
}