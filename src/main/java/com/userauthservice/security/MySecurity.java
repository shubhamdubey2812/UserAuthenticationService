package com.userauthservice.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.AlreadyBuiltException;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.userauthservice.exception.UnauthorizedException;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class MySecurity {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {
        try {
            httpSecurity
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/public/**", "/auth/**").permitAll()
                    .requestMatchers("/admin/**").hasRole("ADMIN")
//                    .anyRequest().authenticated()
                );

            return httpSecurity.build();

        } catch (IllegalStateException e) {
            throw new AlreadyBuiltException("This object has already been built");
        } catch (Exception e) {
            throw new UnauthorizedException("Invalid token");
        }
    }

    @Bean
    PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
		
	}

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
		
	}
}
