package com.example.servingwebcontent.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class XSSController {
    @RequestMapping("/xss")
    public String index(Model model, HttpServletRequest request, HttpServletResponse response) {
        model.addAttribute("payload",request.getParameter("payload"));
        return "xss";
    }
}
