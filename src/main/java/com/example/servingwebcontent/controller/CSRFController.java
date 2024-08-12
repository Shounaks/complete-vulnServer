package com.example.servingwebcontent.controller;

import com.example.servingwebcontent.DataRepository;
import com.example.servingwebcontent.repository.Data;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class CSRFController {
    private final DataRepository dataRepository;

    @RequestMapping("/csrf")
    public String csrf(@RequestParam String csrf_payload, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        Data user = (Data) session.getAttribute("user");
        user.setEmail(csrf_payload);
        session.setAttribute("user", dataRepository.save(user));
        model.addAttribute("name",user.getName());
        model.addAttribute("email",user.getEmail());
        model.addAttribute("email_message", "Changed Email ID");
        return "home";
    }
}
