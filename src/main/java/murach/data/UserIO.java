package murach.data;

import murach.business.User;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class UserIO {

    public static User getUser(String email, String filePath) {
        if (email == null || filePath == null) return null;

        Path path = Paths.get(filePath);
        if (!Files.exists(path)) return null;

        try (BufferedReader br = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String line;
            while ((line = br.readLine()) != null) {
                // bỏ qua dòng rỗng
                if (line.trim().isEmpty()) continue;
                // split theo dấu phẩy, tối giản (nếu tên chứa dấu phẩy thì cần CSV parser)
                String[] parts = line.split(",", -1);
                if (parts.length >= 1) {
                    String e = parts[0].trim();
                    if (e.equalsIgnoreCase(email.trim())) {
                        String first = parts.length > 1 ? parts[1].trim() : "";
                        String last = parts.length > 2 ? parts[2].trim() : "";
                        return new User(e, first, last);
                    }
                }
            }
        } catch (IOException ex) {
            // log nếu cần
            ex.printStackTrace();
        }
        return null;
    }


    public static boolean add(User user, String filePath) {
        if (user == null || filePath == null) return false;

        // đảm bảo thư mục chứa file tồn tại
        Path path = Paths.get(filePath);
        try {
            Path parent = path.getParent();
            if (parent != null && !Files.exists(parent)) {
                Files.createDirectories(parent);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        // escape cơ bản: thay dấu newline, thay dấu phẩy bằng dấu khác hoặc quote
        // (ở đây dùng cách đơn giản: thay dấu phẩy bằng space)
        String email = sanitize(user.getEmail());
        String first = sanitize(user.getFirstName());
        String last = sanitize(user.getLastName());

        String line = String.join(",", email, first, last) + System.lineSeparator();

        // ghi append bằng Files.newBufferedWriter
        try (BufferedWriter bw = Files.newBufferedWriter(path,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND)) {
            bw.write(line);
            bw.flush();
            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private static String sanitize(String s) {
        if (s == null) return "";
        // loại bỏ newline, carriage return; thay dấu phẩy để không phá format CSV đơn giản này
        return s.replace("\r", " ").replace("\n", " ").replace(",", " ");
    }
}
