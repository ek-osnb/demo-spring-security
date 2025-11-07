package ek.osnb.demospringsecurity.security.jwt;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JwtService jwtService;

    public JwtAuthenticationProvider(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token = (String) authentication.getCredentials();

        if (token.isEmpty() || token.isBlank()) {
            throw new BadCredentialsException("Not a valid token");
        }

        if (!jwtService.validateToken(token)) {
            throw new BadCredentialsException("Not a valid token");
        }

        String username = jwtService.getUsernameFromToken(token);
        List<String> roles = jwtService.getRolesFromToken(token);
        var grantedRoles = roles.stream()
                .map(r -> (r.startsWith("ROLE_") ? r : "ROLE_" + r))
                .map(SimpleGrantedAuthority::new)
                .toList();

        return new JwtAuthenticationToken(username,grantedRoles);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
