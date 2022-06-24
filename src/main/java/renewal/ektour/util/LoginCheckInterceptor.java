package renewal.ektour.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import renewal.ektour.exception.AdminException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {

    /**
     * 관리자 페이지 접근 시 로그인 체크하는 인터셉터
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        if (session == null || session.getAttribute(SessionConst.ADMIN) == null) {
            log.info("로그인되지 않은 사용자 접근");
            throw new AdminException("인증 거부 : 허가되지 않은 사용자");
        }
        return true;
    }
}
