package com.example.allinworks.module.user.controller;

import com.example.allinworks.module.user.domain.Department;
import com.example.allinworks.module.user.domain.User;
import com.example.allinworks.module.user.dto.JoinUserRequest;
import com.example.allinworks.module.user.dto.LoginUser;
//import com.example.allinworks.module.user.dto.UserRequest;
import com.example.allinworks.module.user.dto.UserUpdateRequest;
import com.example.allinworks.module.user.repository.PositionRepository;
import com.example.allinworks.module.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.allinworks.module.user.domain.Position;
import java.util.List;


@Controller
@RequestMapping("/user")
@AllArgsConstructor
@Slf4j
public class UserController {
    private UserService userService;

    @GetMapping("/login")
    public String loginPage() {
        //return "sign-in";
        return "index";
    }

    @GetMapping("/sign-up")
    public String signUpPage(Model model) {
        List<Department> departments = userService.getAllDepartments();
        System.out.println("부서 개수: " + departments.size()); // 로그 찍어보기
        model.addAttribute("departments", departments);

        //List<String> positions = userService.getAllPositions();
        List<Position> positions = userService.getAllPositions(); // ✅ 직급 리스트 가져오기
        System.out.println("직급 개수: " + positions.size()); // ✅ 로그로 확인
        model.addAttribute("positions", positions); // ✅ 모델에 추가

        return "sign-up";
    }


    @Autowired
    private PositionRepository positionRepository;
    @GetMapping("/profile")
    public String profilePage(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("user"); // 세션에서 로그인된 사용자 정보 가져오기!
        if (loggedInUser == null) {
            return "redirect:/user/login";
        }
        if (loggedInUser.getDepartment() != null) { // 부서 정보가 있을 때만!
            String departmentName = loggedInUser.getDepartment().getDeptName();
            System.out.println("--- 로그인된 사용자의 부서 이름 (System.out): " + departmentName + " ---"); // 콘솔 출력!
        } else {
            System.out.println("--- 로그인된 사용자는 부서 정보가 없습니다 (System.out). ---");
        }

        String positionName = "";
        if (loggedInUser.getPosition() != null) {
            Position position = positionRepository.findByPositionNo(loggedInUser.getPosition());
            if (position != null) {
                positionName = position.getPositionName();
            }
        }

        model.addAttribute("user", loggedInUser); // 모델에 'user'라는 이름으로 담기!
        model.addAttribute("positionName", positionName); // ✅ 이 줄 추가
        return "profile"; // profile.html 뷰 반환
    }

    @PostMapping("/signUpSubmit")
    public String signUpSubmit(Model model, @ModelAttribute JoinUserRequest request) {
        //System.out.println("회원가입 요청시 비밀번호"+ request.getUserPw());
        userService.joinUser(request);
        return "index";
        //return "sign-in";
    }

    @PostMapping("/userLogin")
    public String userLogin(Model model, @ModelAttribute LoginUser request, HttpSession session) {
        User user = userService.loginUser(request);
        if(user == null){
            model.addAttribute("error", "아이디나 비밀번호를 확인해주세요");
           // return "sign-in";
            return "index";
        }else{
            session.setAttribute("user", user);
            model.addAttribute("userName", user.getUserName());}
        //return "redirect:/index";
        return "redirect:/schedule/calendar";
    }

    @GetMapping("/session-user")
    @ResponseBody
    public User getSessionUser(HttpSession session) {
        User user = (User) session.getAttribute("user");
        System.out.println(user.getDepartment().getDeptNo());

        return (User) session.getAttribute("user");
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // 세션 무효화
        return "redirect:/index";
    }

    @PostMapping("/update")
    public String updateUser(@ModelAttribute UserUpdateRequest userUpdateRequest, HttpSession session, Model model) {

        User sessionUser = (User) session.getAttribute("user");

        userService.updateUser(userUpdateRequest, sessionUser);

        // 세션 정보도 업데이트
        sessionUser.setUserName(userUpdateRequest.getUserName());
        sessionUser.setEmail(userUpdateRequest.getEmail());
        sessionUser.setAddress(userUpdateRequest.getAddress());
        sessionUser.setBirthday(userUpdateRequest.getBirthday());
        session.setAttribute("user", sessionUser);

        return "redirect:/user/profile";
    }

/*    @PostMapping("/sign-up")
    public String signUp(User user) {
        userService.registerUser(user); // 회원가입 처리
        return "redirect:/user/login";  // 가입 후 로그인 페이지로 이동
    }*/

    /*@GetMapping("/sign-up")
    public String signUpPage() {
        return "sign-up";
    }*/

        /*@GetMapping("/profile")
    public String profilePage() {
        return "profile";
    }
*/

}
