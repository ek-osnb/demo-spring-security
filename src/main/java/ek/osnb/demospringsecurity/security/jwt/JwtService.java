package ek.osnb.demospringsecurity.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Service
public class JwtService {

    private final String SECRET_KEY;
    private final long EXPIRATION_TIME_MS;

    public JwtService(@Value("${jwt.secret}") String key, @Value("${jwt.expirationTimeSeconds}") long expirationTime) {
        this.SECRET_KEY = key;
        this.EXPIRATION_TIME_MS = expirationTime * 1000;
    }

    // Verifier a JWT
    public boolean validateToken(String token) {
        JwtParser parser = Jwts.parserBuilder()
                .setClock(Date::new)
                .setSigningKey(this.getSignKey())
                .build();

        Jws<Claims> jws = parser.parseClaimsJws(token);

        if (!"HS256".equals(jws.getHeader().getAlgorithm())) {
            throw new SecurityException("Not supported alg." + jws.getHeader().getAlgorithm());
        }

        return true;
    }

    // Generate JWT Token
    public String generateToken(String username, Collection<? extends GrantedAuthority> authorities) {
        List<String> roles = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MS))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        Claims c = Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return c.getSubject();
    }

    public List<String> getRolesFromToken(String token) {
        Claims c = Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return  c.get("roles", List.class);
    }


    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
