package com.example.servingwebcontent.controller;

import com.example.servingwebcontent.repository.Data;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

	@GetMapping("/home")
	public String greeting(Model model, HttpServletRequest request, HttpServletResponse response) {
//		Data userData = (Data) request.getSession().getAttribute("user");
		Data userData = (Data) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		model.addAttribute("name",userData.getName());
		model.addAttribute("email",userData.getEmail());
		return "home";
	}

}
