package murach.util;

import javax.servlet.http.Cookie;

public class CookieUtil {

    /**
     * Trả về giá trị cookie theo tên, hoặc null nếu không tìm thấy.
     */
    public static String getCookieValue(Cookie[] cookies, String name) {
        if (cookies == null || name == null) return null;
        for (Cookie c : cookies) {
            if (c != null && name.equals(c.getName())) {
                return c.getValue();
            }
        }
        return null;
    }

    /**
     * Tùy chọn: helper để tìm Cookie object theo tên (nếu cần thao tác thêm).
     */
    public static Cookie getCookie(Cookie[] cookies, String name) {
        if (cookies == null || name == null) return null;
        for (Cookie c : cookies) {
            if (c != null && name.equals(c.getName())) {
                return c;
            }
        }
        return null;
    }
}
