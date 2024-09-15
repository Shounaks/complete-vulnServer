package com.example.servingwebcontent.controller.manager;

import com.example.servingwebcontent.controller.admin.AdminHomeController;
import com.example.servingwebcontent.repository.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.logging.Logger;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ManagerController {
    Logger logger = Logger.getLogger(AdminHomeController.class.getName());

    @RequestMapping(value = "/manager/dashboard", method = RequestMethod.GET)
    public String adminHomePage(Model model) {
        Data userData = (Data) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        model.addAttribute("name", userData.getName());
        model.addAttribute("email", userData.getEmail());
        return "manager-dashboard";
    }
}
