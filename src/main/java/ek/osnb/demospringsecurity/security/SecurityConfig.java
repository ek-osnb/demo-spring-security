package ek.osnb.demospringsecurity.security;

import ek.osnb.demospringsecurity.app.model.EntityAuthority;
import ek.osnb.demospringsecurity.app.repository.AuthorityRepository;
import ek.osnb.demospringsecurity.app.repository.UserRepository;
import ek.osnb.demospringsecurity.security.jwt.JwtAuthenticationFilter;
import ek.osnb.demospringsecurity.security.jwt.JwtAuthenticationProvider;
import ek.osnb.demospringsecurity.security.jwt.JwtService;
import ek.osnb.demospringsecurity.security.users.JpaUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/public").permitAll()
                        .requestMatchers("/api/auth/login").permitAll()
                        .requestMatchers("/api/**").authenticated()
                        .requestMatchers("/h2-console/**").permitAll()
                        .anyRequest().permitAll()
                )
//                .formLogin(Customizer.withDefaults())
//                .httpBasic(Customizer.withDefaults());
                .csrf(csrf -> csrf.disable())
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder,
                                                 UserRepository userRepository,
                                                 AuthorityRepository authorityRepository) {
//        var uds = new InMemoryUserDetailsManager();

        var uds = new JpaUserDetailsService(userRepository,authorityRepository);

        EntityAuthority authority = new EntityAuthority();
        authority.setName("ROLE_USER");
        authorityRepository.save(authority);

        EntityAuthority authorityAdmin = new EntityAuthority();
        authorityAdmin.setName("ROLE_ADMIN");
        authorityRepository.save(authorityAdmin);


        UserDetails u1 = User
                .withUsername("user")
                .password(encoder.encode("user"))
                .roles("USER")
                .build();


        UserDetails u2 = User
                .withUsername("admin")
                .password(encoder.encode("admin"))
                .roles("USER", "ADMIN")
                .build();

        uds.createUser(u1);
        uds.createUser(u2);

        return uds;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
//        return NoOpPasswordEncoder.getInstance();
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider jwtAuthenticationProvider(JwtService jwtService) {
        return new JwtAuthenticationProvider(jwtService);
    }


    @Bean
    public AuthenticationProvider daoAuthenticationProvider(UserDetailsService uds, PasswordEncoder passwordEncoder) {
        var authProvider = new DaoAuthenticationProvider(uds);
        authProvider.setPasswordEncoder(passwordEncoder);

        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(List<AuthenticationProvider> providers) {
        return new ProviderManager(providers);
    }
}
