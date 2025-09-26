package com.photo_lab.photo_lab.controllers;

import com.photo_lab.photo_lab.services.PhotoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class PhotoController {
    private final PhotoService service;

    public PhotoController(PhotoService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String viewGallery(Model model) {
        model.addAttribute("photos", service.listPhotos());
        return "gallery";
    }

    @PostMapping("/upload")
    public String uploadPhoto(@RequestParam("file") MultipartFile file,
                              @RequestParam("description") String description) throws Exception {
        service.uploadPhoto(file, description);
        return "redirect:/";
    }
}
