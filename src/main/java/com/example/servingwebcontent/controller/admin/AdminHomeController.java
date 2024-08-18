package com.example.servingwebcontent.controller.admin;

import com.example.servingwebcontent.DataRepository;
import com.example.servingwebcontent.repository.Data;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import static com.example.servingwebcontent.utils.LogUtils.logging;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AdminHomeController {
    private final DataRepository dataRepository;
    Logger logger = Logger.getLogger(AdminHomeController.class.getName());

    @RequestMapping(value = "/admin/home", method = RequestMethod.GET)
    public String adminHomePage(Model model) {
        Data userData = (Data) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        model.addAttribute("name", userData.getName());
        model.addAttribute("email", userData.getEmail());
        return "admin-home";
    }

    @RequestMapping("admin/xss")
    public String index(Model model, HttpServletRequest request, HttpServletResponse response) {
        String xssString = request.getParameter("payload");
        if (xssString.contains(">") || xssString.contains("<")) {
            logging(logger, "Suspected XSS String: " + xssString);
            xssString = xssString.replaceAll("[<>?!@.#*$%]", " ");
        }
        model.addAttribute("payload", xssString);
        return "admin-xss";
    }

    @RequestMapping("/admin/sqli")
    public String sqlInjection(@RequestParam String sqli, Model model, HttpServletRequest request, HttpServletResponse response) {
        log.info("Delegatign SQL Stuff to Spring Data JPA!");
        if (sqli.contains("--") || Pattern.compile("[0-9]+=[0-9]+").matcher(sqli).find()) {
            logging(logger, "Suspected SQL Injection String: " + sqli);
        }
        List<Data> dataList = dataRepository.findByNameContaining(sqli);
        System.out.println(dataList);
        model.addAttribute("dataList", dataList);

        return "sql_injection";
    }

    @PostMapping(path = "/admin/fileupload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String unrestrictedUploadOfFiles(@RequestParam("uuf") MultipartFile file, Model model) {
        try {
            final String UPLOAD_DIR = "src/main/resources/static/uploads/";
            if (file == null || file.getOriginalFilename() == null || file.isEmpty()) {
                return "redirect:/admin/home";
            }
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            Path path = Paths.get(UPLOAD_DIR + fileName);
            if (!Objects.equals(file.getContentType(), "image/jpeg") && !Objects.equals(file.getContentType(), "image/png")) {
                logging(logger, "Invalid File format: " + fileName);
                throw new IOException("File Not Supported");
            }
            model.addAttribute("fileName", "../uploads/" + fileName);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("fileUploadError", "FILE NOT SUPPORTED - Provide only JPEG/PNG");
        }
        return "upload";
    }

    @RequestMapping("/admin/csrf")
    public String csrf(@RequestHeader(value = "Referer", required = false) String referer, @RequestParam String csrf_payload, HttpServletRequest request, Model model) {
        if (!referer.startsWith("http://localhost:8080")) {
            logging(logger, "DETECTED DIFFERENT REFERER - CSRF from referer : " + referer);
            log.warn("SUSPECTED CSRF DETECTED!");
            return "redirect:/admin/home";
        }
        HttpSession session = request.getSession();
//        Data user = (Data) session.getAttribute("user");
        Data user = (Data) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        user.setEmail(csrf_payload);
        session.setAttribute("user", dataRepository.save(user));

        model.addAttribute("name", user.getName());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("email_message", "Changed Email ID");
        return "redirect:/admin/home";
    }

    private String fullBaseUrl(HttpServletRequest request) {
        // Get context path
//        String contextPath = request.getContextPath();

        // Get base URL (scheme, server name, and port)
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();

        // Full URL including context path
        return baseUrl; //+ contextPath;
    }

    @RequestMapping("/admin/ssrf")
    public String callOuterService(@RequestParam String ssrf, Model model, HttpServletRequest request) {
        String base = fullBaseUrl(request);
        if (ssrf.isBlank() || !ssrf.startsWith(base)) {
            logging(logger, "SUSPECTED SSRF DETECTED: " + ssrf);
            model.addAttribute("ssrfError", "INVALID URL DETECTED");
            return "ssrf";
        }
        RestClient restClient = RestClient.builder().baseUrl(ssrf).build();
        Object retrieved = restClient.get().retrieve().body(String.class);
        model.addAttribute("externalData", retrieved);
        return "ssrf";
    }
}