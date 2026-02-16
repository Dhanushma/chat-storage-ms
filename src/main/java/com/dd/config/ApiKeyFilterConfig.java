package com.dd.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Configuration
public class ApiKeyFilterConfig {

    @Bean
    public FilterRegistrationBean<ApiKeyFilter> apiKeyFilterRegistration(
            @Value("${application.api-key}") String apiKey) {

        FilterRegistrationBean<ApiKeyFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new ApiKeyFilter(apiKey));
        registration.addUrlPatterns("/*");
        registration.setOrder(1);

        // Exclude paths from the filter
        registration.addInitParameter("excluded-paths", "/actuator,/swagger-ui,/v3/api-docs");

        return registration;
    }

    public static class ApiKeyFilter extends OncePerRequestFilter {

        private final String apiKey;
        private static final String API_KEY_HEADER = "X-API-KEY";

        public ApiKeyFilter(String apiKey) {
            this.apiKey = apiKey;
        }

        @Override
        protected boolean shouldNotFilter(HttpServletRequest request) {
            String path = request.getRequestURI();
            return path.startsWith("/actuator") || path.startsWith("/swagger-ui") || path.startsWith("/v3/api-docs");
        }

        @Override
        protected void doFilterInternal(HttpServletRequest request,
                                        HttpServletResponse response,
                                        FilterChain filterChain) throws ServletException, IOException {
            String requestApiKey = request.getHeader(API_KEY_HEADER);

            if (apiKey.equals(requestApiKey)) {
                filterChain.doFilter(request, response);
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Unauthorized: Invalid API Key");
            }
        }
    }
}
