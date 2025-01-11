package com.ht.qlktx.filter;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws IOException, ServletException {

        String requestURI = request.getRequestURI();
        if (requestURI.startsWith("/api/v1/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String sub;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            sendUnauthorizedResponse(response);
            return;
        }
        try {
            jwt = authHeader.substring(7);
            sub = jwtService.extractSub(jwt);

            if (sub != null) {
                String role = jwtService.extractRole(jwt);
                boolean isValid = jwtService.isTokenValid(jwt);
                if (!isValid) {
                    sendUnauthorizedResponse(response);
                } else {
                    request.setAttribute("role", role);
                    request.setAttribute("sub", sub);
                    filterChain.doFilter(request, response);
                }
                return;
            }
            sendUnauthorizedResponse(response);
        } catch (Exception e) {
            sendUnauthorizedResponse(response, e.getMessage());
        }
    }

    private void sendUnauthorizedResponse(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        ObjectMapper objectMapper = new ObjectMapper();
        String errors = objectMapper.writeValueAsString(Map.of(
                "status", HttpServletResponse.SC_UNAUTHORIZED,
                "message", "Yêu cầu chưa được xác thực"
        ));
        response.getWriter().write(errors);
    }

    private void sendUnauthorizedResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        ObjectMapper objectMapper = new ObjectMapper();
        String errors = objectMapper.writeValueAsString(Map.of(
                "status", HttpServletResponse.SC_UNAUTHORIZED,
                "message", message
        ));
        response.getWriter().write(errors);
    }
}