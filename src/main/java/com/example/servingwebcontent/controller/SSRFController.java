package com.example.servingwebcontent.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestClient;

@Controller
public class SSRFController {

    @RequestMapping("/ssrf")
    public String callOuterService(@RequestParam String ssrf, Model model){
        RestClient restClient = RestClient.builder().baseUrl(ssrf).build();
        Object retrieved = restClient.get().retrieve().body(String.class);
        model.addAttribute("externalData", retrieved);
        return "ssrf";
    }
}
