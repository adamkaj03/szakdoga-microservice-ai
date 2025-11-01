package com.adam.buzas.webshop.main.restcontrollers;

import com.adam.buzas.webshop.main.converter.JsonStringToObjectConverter;
import com.adam.buzas.webshop.main.services.ImageUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("/api")
@RestController
@CrossOrigin(origins = {"http://localhost:4200", "https://purple-river-0f0577f03.4.azurestaticapps.net"})
public class ImageUploadController {

    @Autowired
    private ImageUploadService imageUploadService;
    @Autowired
    private JsonStringToObjectConverter converter;

    @PostMapping("/uploadImage")
    public ResponseEntity<Map<String, String>> handleFileUpload(@RequestParam("file") MultipartFile file) {
        System.out.println("controller");
        try {
            String response = imageUploadService.uploadImage(file, file.getOriginalFilename());
            Map<String, String> jsonResponse = new HashMap<>();
            jsonResponse.put("url", response); // response helyett a kép URL-jét kell beállítani
            return ResponseEntity.ok(jsonResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", "Failed to upload image."));
        }
    }


}
