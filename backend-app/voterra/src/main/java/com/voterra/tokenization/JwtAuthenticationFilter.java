package com.voterra.tokenization;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    public static final String USERS = "/users/";
    public static final String POSTS = "/posts/";
    @Autowired
    private JwtUtils jwtUtils;

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Add CORS headers
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");
        response.setHeader("Access-Control-Allow-Credentials", "true");

        // Handle preflight requests
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        // Check if the request is for /signup or /login
        String requestUri = request.getRequestURI();
        if ((USERS + "signup").equals(requestUri) || (USERS + "login").equals(requestUri) || (USERS + "signupWithGoogle").equals(requestUri) ||
                (USERS + "signupWithFacebook").equals(requestUri) || (USERS + "loginWithGoogle").equals(requestUri)
                || (USERS + "loginWithFacebook").equals(requestUri) || (USERS + "forgetPassword").equals(requestUri) || (POSTS  + "savePost").equals(requestUri)
                || (POSTS + "reportPost").equals(requestUri) || (POSTS + "deleteReportedPost").equals(requestUri)
                || (POSTS + "leaveReportedPost").equals(requestUri)){
            // If the request is for /signup or /login, skip the filter and continue the chain
            filterChain.doFilter(request, response);
            return;
        }

        // Extract token from the Authorization header
        String token = getJwtFromRequest(request);
        if (token != null && jwtUtils.validateToken(token)) {
            // Token is valid, extract user information (e.g., account)
            String account = jwtUtils.extractAccount(token);
            // Create authentication object and set it in the Security context
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(account, null, null);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            // Log the issue or take action
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid or missing token.");
            return; // Stop further processing
        }

        // Continue the filter chain
        filterChain.doFilter(request, response);
    }
}
