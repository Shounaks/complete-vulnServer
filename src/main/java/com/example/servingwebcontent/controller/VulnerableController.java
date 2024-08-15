package com.example.servingwebcontent.controller;

import com.example.servingwebcontent.DataRepository;
import com.example.servingwebcontent.repository.Data;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class VulnerableController {
    private final DataRepository dataRepository;

    @RequestMapping("/xss")
    public String index(Model model, HttpServletRequest request, HttpServletResponse response) {
        model.addAttribute("payload", request.getParameter("payload"));
        return "xss";
    }

    @RequestMapping("/sqli")
    public String sqlInjection(@RequestParam String sqli, Model model, HttpServletRequest request, HttpServletResponse response) {
        String hql = "FROM Data WHERE name = '" + sqli + "'";

        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Data.class)
                .buildSessionFactory();
        Session session = factory.getCurrentSession();
        session.beginTransaction();

        //create the query
        Query query = session.createQuery(hql);
        List<Data> dataList = query.getResultList();

        System.out.println(dataList);
        model.addAttribute("dataList", dataList);

        session.close();
        return "sql_injection";
    }

    @PostMapping(path = "/fileupload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String unrestrictedUploadOfFiles(@RequestParam MultipartFile uuf, Model model) {
        try {
            final String UPLOAD_DIR = "src/main/resources/static/uploads/";
            if (uuf == null || uuf.getOriginalFilename() == null || uuf.isEmpty()) {
                return "redirect:/home";
            }
            String fileName = StringUtils.cleanPath(uuf.getOriginalFilename());
            Path path = Paths.get(UPLOAD_DIR + fileName);
            model.addAttribute("fileName", "/uploads/" + fileName);
            Files.copy(uuf.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            // This is required to load image on the RAM, to be immediately ready to serve.
            // Makes no sense, but before it was added, we would have to manually open the
            // file to be able to serve. so now its working, and hence not questioning it.
            BufferedImage image = ImageIO.read(path.toFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "upload";
    }

    @RequestMapping("/csrf")
    public String csrf(@RequestParam String csrf_payload, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        // Data user = (Data) session.getAttribute("user");
        Data user = (Data) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        user.setEmail(csrf_payload);
        session.setAttribute("user", dataRepository.save(user));

        model.addAttribute("name", user.getName());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("email_message", "Changed Email ID");
        return "home";
    }

    @RequestMapping("/ssrf")
    public String callOuterService(@RequestParam String ssrf, Model model) {
        RestClient restClient = RestClient.builder().baseUrl(ssrf).build();
        Object retrieved = restClient.get().retrieve().body(String.class);
        model.addAttribute("externalData", retrieved);
        return "ssrf";
    }
}
