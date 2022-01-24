package ru.aivar.webpdfapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.aivar.webpdfapp.services.UserService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @PostMapping("/block/{id}")
    public ResponseEntity<?> block(@PathVariable Long id){
        userService.block(id);
        return ResponseEntity.ok().build();
    }

}
