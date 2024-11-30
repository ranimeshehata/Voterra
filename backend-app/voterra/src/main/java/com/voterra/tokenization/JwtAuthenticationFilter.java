package com.voterra.tokenization;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        System.out.println(bearerToken);
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    @Override
    protected void doFilterInternal(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response, jakarta.servlet.FilterChain filterChain) throws ServletException, IOException {
        // Check if the request is for /signup or /login
        String requestUri = request.getRequestURI();
        System.out.println(requestUri);
        if ("/users/signup".equals(requestUri) || "/users/login".equals(requestUri) || "/users/signupWithGoogle".equals(requestUri) || "/users/signupWithFacebook".equals(requestUri)) {
            // If the request is for /signup or /login, skip the filter and continue the chain
            filterChain.doFilter(request, response);
            return;
        }

        // Extract token from the Authorization header
        String token = getJwtFromRequest((HttpServletRequest) request);
        System.out.println(token);
        if (token != null && jwtUtils.validateToken(token)) {
            // Token is valid, extract user information (e.g., account)
            String account = jwtUtils.extractAccount(token);
            // Create authentication object and set it in the Security context
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(account, null, null);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        else {
            // Log the issue or take action
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid or missing token.");
            return; // Stop further processing
        }

        // Continue the filter chain
        filterChain.doFilter(request, response);
    }
}
