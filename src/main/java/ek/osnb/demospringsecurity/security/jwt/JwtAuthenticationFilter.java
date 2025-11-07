package ek.osnb.demospringsecurity.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws ServletException, IOException {
        String authHeader = req.getHeader("Authorization");

        if (authHeader == null || authHeader.isBlank() || authHeader.isEmpty()) {
            chain.doFilter(req,res);
            return;
        }
        String token = authHeader.substring(7);
        Authentication result = authenticationManager.authenticate(new JwtAuthenticationToken(token));

        if (result.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(result);
        }
        chain.doFilter(req,res);
    }
}
