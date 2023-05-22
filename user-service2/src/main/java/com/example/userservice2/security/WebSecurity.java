package com.example.userservice2.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.IpAddressMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurity {

    private String[] WHITE_LIST = {"/user-service/**", "/**"};
    @Bean
    protected SecurityFilterChain config(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.headers().frameOptions().disable();

//        http.authorizeHttpRequests(authorize -> authorize
//                .requestMatchers(WHITE_LIST).permitAll());
        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
        http.authorizeHttpRequests((authorizationManagerRequestMatcherRegistry -> {
            authorizationManagerRequestMatcherRegistry.requestMatchers("/**").access(((authentication, context) -> {
                IpAddressMatcher ipAddressMatcher = new IpAddressMatcher("127.0.0.1");
                HttpServletRequest request = context.getRequest();
                return new AuthorizationDecision(ipAddressMatcher.matches(request));
            })).and().addFilter(getAuthenticationFilter(authenticationManager));
        }));
        return http.build();
    }

    private AuthenticationFilter getAuthenticationFilter(AuthenticationManager authenticationManager) {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter();
        authenticationFilter.setAuthenticationManager(authenticationManager);
        return authenticationFilter;
    }
}
