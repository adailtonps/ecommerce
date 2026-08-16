package com.adps.e_commerce.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class ApiKeyFilter extends OncePerRequestFilter {
    @Value("${ECOMMERCE_API_KEY}")
    private String ecommerceApiKey;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain Filterchain
    ) throws ServletException, IOException {

        String apiKey = request.getHeader("X-Service-Key");

        if (apiKey == null || !apiKey.equals(ecommerceApiKey)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        } Filterchain.doFilter(request, response);
    }
}
