package com.DevMatrix.StationManagementService.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    public JwtAuthorizationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        String header = request.getHeader("Authorization");
        System.out.println("Incoming token header: " + header);

        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);

        if (jwtUtil.validateToken(token)) {
            // Extract user info
            String email = jwtUtil.extractEmail(token);
            String role = jwtUtil.extractRole(token);

            // Create authentication token
            List<SimpleGrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority(role.startsWith("ROLE_") ? role : "ROLE_" + role)
            );

            UsernamePasswordAuthenticationToken authToken = 
                new UsernamePasswordAuthenticationToken(email, null, authorities);

            // Set the authentication in the security context
            SecurityContextHolder.getContext().setAuthentication(authToken);

            // Also attach user info to the request for easy access in controllers
            request.setAttribute("userEmail", email);
            request.setAttribute("userRole", role);

            System.out.println("User authenticated: " + email + " with role: " + role);
            System.out.println("Authorities set: " + authorities);
            System.out.println("SecurityContext authentication: " + SecurityContextHolder.getContext().getAuthentication());

        } else {
            System.out.println("Invalid token");
        }

        filterChain.doFilter(request, response);
    }
}