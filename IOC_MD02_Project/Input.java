package IOC_MD02_Project;

import java.util.Scanner;

public class Input {
    private static final Scanner sc = new Scanner(System.in);

    public static void clear() {
        System.out.print("\n".repeat(30));
    }

    public static void pause() {
        System.out.println("\nNhấn Enter để tiếp tục...");
        sc.nextLine();
    }

    public static String nonBlank(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine();
            if (s != null && !s.trim().isEmpty()) return s.trim();
            System.out.println("❌ Không được để trống!");
        }
    }

    public static String optional(String prompt) {
        System.out.print(prompt);
        String s = sc.nextLine();
        if (s == null) return null;
        s = s.trim();
        return s.isEmpty() ? null : s;
    }

    public static int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine().trim();
            try {
                int v = Integer.parseInt(s);
                if (v < min || v > max) throw new NumberFormatException();
                return v;
            } catch (Exception e) {
                System.out.println("❌ Nhập số trong khoảng [" + min + ", " + max + "]");
            }
        }
    }

    public static boolean yesNo(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine().trim();
            if (s.equalsIgnoreCase("y") || s.equalsIgnoreCase("yes")) return true;
            if (s.equalsIgnoreCase("n") || s.equalsIgnoreCase("no")) return false;
            System.out.println("Nhập y/n");
        }
    }

    public static String choice(String prompt, String[] allowed) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine().trim();
            for (String a : allowed) {
                if (a.equalsIgnoreCase(s)) return a.toUpperCase();
            }
            System.out.print("Chỉ nhận: ");
            for (int i = 0; i < allowed.length; i++) {
                System.out.print(allowed[i]);
                if (i < allowed.length - 1) System.out.print(", ");
            }
            System.out.println();
        }
    }
}