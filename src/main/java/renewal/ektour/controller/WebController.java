package renewal.ektour.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    /**
     * 리액트 라우팅 리다이렉션
     */
    @GetMapping(value = {
            "",
            "/estimate/list/**",
            "/notice",
            "/list",
            "/introduce",
            "/bus",
            "/request",
            "/my",
            "estimate/my/list/**",
            "/search/my/estimate",
            "/service-center",
            "/mobile",
            "/mobile/**"
    })
    public String redirect() {
        return "forward:/index.html";
    }

}
