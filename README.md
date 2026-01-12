# Spring Security JWT example

This is a simple example of how to implement JWT (JSON Web Token) authentication using Spring Security in a Spring Boot application.
In this demo, the frontend is served from the `src/main/resources/static` directory. 

The frontend stores the JWT token in local storage and includes it in the `Authorization` header for requests to protected endpoints.

Note that storing JWT tokens in local storage can expose your application to XSS (Cross-Site Scripting) attacks.

**The application has been upgraded to use Spring Boot 4.0.1 from Spring Boot 3.5.7.**

## Authentication Flow
1. The user sends a POST request to the `/login` endpoint with their username and password:
    ```json
    {
        "username": "user",
        "password": "password"
    }
    ```
2. The server validates the credentials and, if valid, generates a JWT token and returns it in the response:
    ```json
    {
        "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
    }
    ```
3. The frontend stores the JWT token in local storage.
4. For subsequent requests to protected endpoints, the frontend includes the JWT token in the `Authorization` header:
    ```Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...```
5. The server validates the JWT token and, if valid, processes the request and returns the requested data.

## Preconfigured Users and Roles
By default, there are two users and two roles configured in this demo application:
- User: `user`, Password: `password`, Role: `USER`
- User: `admin`, Password: `password`, Role: `ADMIN`, `USER`
- You can modify the users and roles in the `src/main/java/ek.osnb.demospringsecurity/security/SecurityConfig.java`.
- Passwords are encoded using BCryptPasswordEncoder.

## Endpoint Security
Endpoints are secured by default. You can configure which endpoints are public and which require authentication in the `SecurityConfig` class.
Here is how the security is configured in this demo application:

```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
    http
            // Endpoint authorization configuration
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/api/public").permitAll()
                    .requestMatchers("/api/auth/login").permitAll()
                    .requestMatchers("/api/**").authenticated()
                    .requestMatchers("/h2-console/**").permitAll()
                    .anyRequest().permitAll()
            )
            // We don't need CSRF for JWT based authentication
            .csrf(csrf -> csrf.disable()) 
            // We don't need sessions to be created or used by Spring Security
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // We add our custom JWT security filter before the UsernamePasswordAuthenticationFilter
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
}
```
**Note**: The request matchers are evaluated in order, so the most specific matchers should be defined first!

## Method-level security
This example also demonstrates method-level security using the `@PreAuthorize` annotation.
The `@PreAuthorize` annotation is used to restrict access to specific methods based on user roles. For example, the `adminEndpoint` method is restricted to users with the `ROLE_ADMIN` role.

```java
@GetMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<GreetingDto> adminHome() {
    // Only users with ROLE_ADMIN can access this endpoint
}
```
**Note**: User roles are prefixed with `ROLE_` by Spring Security. So, a user with the role `ADMIN` should be assigned the authority `ROLE_ADMIN`. The role is stored as `ROLE_ADMIN` in the database. When using method-level security annotations, you should refer to the role without the `ROLE_` prefix.

## API Endpoints
- `GET /api/public`: Public endpoint accessible without authentication.
- `POST /api/auth/login`: Public endpoint to authenticate user and return JWT token.
- `GET /api/protected`: Protected endpoint accessible only with a valid JWT token, but no specific role required.
- `GET /api/protected/admin`: Protected endpoint accessible only to users with the `ADMIN` role
- `GET /api/protected/user`: Protected endpoint accessible only to users with the `USER` role
- `GET /h2-console/**`: H2 database console, accessible without authentication (for demo purposes).

## JWT Structure
The JWT token consists of three parts: Header, Payload, and Signature.
- Header: Contains metadata about the token, such as the signing algorithm.
- Payload: Contains the claims, such as the username and roles.
- Signature: Used to verify the authenticity of the token.

### `jwt.io` Debugger
You can use the [jwt.io](https://jwt.io/) debugger to decode and verify the JWT token.

Try running the following command in your terminal to obtain a JWT token::
```bash
curl -X POST http://localhost:8080/api/auth/login -H "Content-Type: application/json" -d '{"username":"user","password":"user"}'
```

Paste the returned token into the `jwt.io` debugger to see its contents. You should be able the see the username and roles in the payload section.
