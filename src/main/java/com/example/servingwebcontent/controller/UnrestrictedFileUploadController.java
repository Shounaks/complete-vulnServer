package com.example.servingwebcontent.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Controller
public class UnrestrictedFileUploadController {

    @PostMapping(path = "/fileupload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String unrestrictedUploadOfFiles(@RequestParam("uuf") MultipartFile file, Model model) {
        try {
            final String UPLOAD_DIR = "src/main/resources/static/uploads/";
            if(file == null || file.getOriginalFilename() == null || file.isEmpty()){
                return "redirect:/home";
            }
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            Path path = Paths.get(UPLOAD_DIR + fileName);
            model.addAttribute("fileName", "/uploads/"+ fileName);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            BufferedImage image = ImageIO.read(path.toFile());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "upload";
    }
}
