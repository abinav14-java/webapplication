package com.abinav.webapplication.utility;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.abinav.webapplication.logic.UserLogic;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserLogic userLogic;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        String path = request.getRequestURI();
        String method = request.getMethod();

        // 🔓 Skip JWT filter for public endpoints
        if (path.startsWith("/api/auth") || 
             (path.startsWith("/api/users") && "GET".equals(method)) || 
            (path.startsWith("/api/posts") && "GET".equals(method)) ||  // Only GET is public
            path.equals("/login") || 
            path.equals("/register") || 
            path.equals("/dashboard") ||
            path.startsWith("/static/")) {
            chain.doFilter(request, response);  // skip JWT validation
            return;
        }

        String authHeader = request.getHeader("Authorization");
        String token = null;
        String email = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            try {
                email = jwtUtil.extractUsername(token);
            } catch (Exception e) {
                // Invalid or malformed token — respond 401 and stop filter chain
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"message\":\"Invalid or expired token\"}");
                return;
            }
        }

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                UserDetails userDetails = userLogic.loadUserByUsername(email);

                if (jwtUtil.validateToken(token)) {
                    UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            } catch (Exception e) {
                // If any error occurs while loading user or validating token, respond 401
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"message\":\"Invalid or expired token\"}");
                return;
            }
        }

        chain.doFilter(request, response);
    }

}

