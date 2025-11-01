package com.adam.buzas.webshop.main.restcontrollers;

import com.adam.buzas.webshop.main.model.ArchiveLog;
import com.adam.buzas.webshop.main.services.ArchiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api")
@RestController()
@CrossOrigin(origins = {"http://localhost:4200", "https://purple-river-0f0577f03.4.azurestaticapps.net"})
public class ArchiveController {

    @Autowired
    private ArchiveService archiveService;

    @PostMapping("/archive")
    public ResponseEntity<Void> archive(@RequestParam int year) {
        archiveService.archiveOrdersFromYear(year);
        return ResponseEntity.ok().build();
    }

    @GetMapping ("/archive/logs")
    public List<ArchiveLog> getArchiveLogs() {
        return archiveService.getArchiveLogs();
    }
}
