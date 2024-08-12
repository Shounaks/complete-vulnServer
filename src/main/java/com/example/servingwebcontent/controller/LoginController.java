package com.example.servingwebcontent.controller;

import com.example.servingwebcontent.DataRepository;
import com.example.servingwebcontent.repository.Data;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {
    private final DataRepository repository;

    @RequestMapping({"/login", "/"})
    public String index(Model model, HttpServletRequest request, HttpServletResponse response) {
        return "login";
    }

    @RequestMapping("/auth")
    public String authenticateTheUser(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            Model model,
            HttpServletRequest request,
            HttpServletResponse response) {

        log.info("Authenticating user: " + username);
        Optional<Data> userDataByName = repository.findByName(username);
        if (userDataByName.isEmpty()) {
            log.warn("User not found: " + username);
            //Logic for when the client doesn't exist
            model.addAttribute("errorMessage", "Wrong username");
            return "login";
        }
        Optional<String> userDataByNameAndPassword = repository.findDataPasswordByName(username);
        if (userDataByNameAndPassword.isEmpty() || !userDataByNameAndPassword.get().equals(password)) {
            log.warn("Password not Correct: " + username);
            //Logic for when the client doesn't exist
            model.addAttribute("errorMessage", "Wrong password");
            return "login";
        }
        Optional<Data> userData = repository.findByNameAndPassword(username, password);
        //We handle the logic to the Other Controllers
        Cookie foo = new Cookie("shounak_app", "YOU_USED_SHOUNAK_APP");
        foo.setHttpOnly(true);
        response.addCookie(foo);

        HttpSession session = request.getSession();
        session.setAttribute("user", userData.get());
        return "redirect:/home";
    }
}
