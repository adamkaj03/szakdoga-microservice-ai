package com.adam.buzas.webshop.main.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
        http
                .cors()
                .and()
                .csrf()
                .disable()
                .authorizeHttpRequests()
                .requestMatchers(HttpMethod.GET,
                        "/api/books/**",
                        "/api/felhasznalok/**",
                        "/api/kategoriak/**",
                        "/api/kosar/**",
                        "/api/szallitasi_tipus/**")
                .permitAll()
                .requestMatchers(HttpMethod.POST,
                        "/api/auth/**",
                        "/api/uploadImage",
                        "/api/kosar/**")
                .permitAll()
                .requestMatchers(HttpMethod.PUT, "/api/kosar/**")
                .permitAll()
                .requestMatchers(HttpMethod.GET,"/api/rendelesek/**")
                .hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST,
                        "/api/kategoriak/**",
                        "api/books",
                        "/api/archive/**")
                .hasRole("ADMIN")
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)

                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}
