/*
package com.example.allinworks.module.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping("/index")
    public String index() {
        return "/index";
    }
}
*/
package com.example.allinworks.module.main.controller;

import com.example.allinworks.module.user.domain.User;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/index")
    public String index(HttpServletResponse response, HttpSession session, Model model) {
        // ✅ 캐시 방지 헤더 설정
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0
        response.setDateHeader("Expires", 0); // Proxies

        // ✅ 로그인된 사용자 정보가 있으면 모델에 추가
        User user = (User) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("userName", user.getUserName());
        }

        return "index"; // 뷰 이름
    }

    // sidenav All-in-Works 로고 클릭시 메인 페이지로 이동
    @GetMapping("/main")
    public String mainPage() {
        return "main";
    }

}
