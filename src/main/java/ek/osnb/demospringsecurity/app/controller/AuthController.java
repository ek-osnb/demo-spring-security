package ek.osnb.demospringsecurity.app.controller;

import ek.osnb.demospringsecurity.security.jwt.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthController(JwtService jwtService, AuthenticationManager authenticationManager) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }


    public record LoginReq(String username, String password) {}
    public record LoginResp(String token) {}


    @PostMapping("/login")
    public ResponseEntity<LoginResp> login(@RequestBody LoginReq req) {
        UsernamePasswordAuthenticationToken reqToken = new UsernamePasswordAuthenticationToken(req.username(), req.password());

        Authentication authResult = authenticationManager.authenticate(reqToken);

        if(!authResult.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
//        List<String> authoritites = authResult.getAuthorities().stream().map(Object::toString).toList();

        var token = jwtService.generateToken(authResult.getName(),authResult.getAuthorities());


        return ResponseEntity.ok(new LoginResp(token));

    }
}
