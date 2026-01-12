package ek.osnb.demospringsecurity.app.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public")
public class PublicController {

    @GetMapping
    public ResponseEntity<GreetingDto> home() {
        return ResponseEntity.ok(new GreetingDto("Welcome to public endpoint!"));
    }
}
