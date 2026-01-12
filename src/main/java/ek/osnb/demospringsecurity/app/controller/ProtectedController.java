package ek.osnb.demospringsecurity.app.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/protected")
public class ProtectedController {

    @GetMapping
    public ResponseEntity<GreetingDto> home() {
        return ResponseEntity.ok(new GreetingDto("Welcome to the protected endpoint!"));
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GreetingDto> adminHome() {
        return ResponseEntity.ok(new GreetingDto("Welcome to the admin protected endpoint!"));
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<GreetingDto> userHome() {
        return ResponseEntity.ok(new GreetingDto("Welcome to the user protected endpoint!"));
    }
}
