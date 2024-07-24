package com.marketsim.task.configuration;

import com.marketsim.task.security.JwtRequestFilter;
import com.marketsim.task.service.MyAdminDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
@Configuration
@EnableWebSecurity // web security support
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    //  method-level security with @PreAuthorize.
        @Autowired
        private MyAdminDetails myUserDetailsService;
        @Autowired
        private JwtRequestFilter jwtRequestFilter;

        // Bean ,which is BCrypt in this paaword
        @Bean
        public PasswordEncoder passwordEncoder() {

            return new BCryptPasswordEncoder();
        }

        /* handles authentication
        When you call the authenticate method, it processes the credentials and determines if they are valid.*/
        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
            return authenticationConfiguration.getAuthenticationManager();
        }

        // Bean to define the AuthenticationProvider, which integrates with the custom UserDetailsService

        @Bean
        public AuthenticationProvider authenticationProvider() {
            DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();//which retrives user details from userDetailsService
            authProvider.setUserDetailsService(myUserDetailsService); // Set custom user details service, compare the both passwords
            authProvider.setPasswordEncoder(passwordEncoder()); // Set password encoder
            return authProvider;
        }

        // Bean to define the SecurityFilterChain, which configures HTTP security
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http.csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(authorize -> authorize //Spring boot 3.1.1 request matchers
                            .requestMatchers("/auth").permitAll()
                            .requestMatchers("/actuator/**").permitAll() // Permit access to health and info endpoints
                            .requestMatchers("**").hasRole("ADMIN")
                            .anyRequest().authenticated() // All other requests require authentication
                    )
                    .sessionManagement(session -> session
                            .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Use stateless session management as we are using JWTs
                    );

            http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
            // Add JWT filter before the UsernamePasswordAuthenticationFilter

            return http.build(); // Build the SecurityFilterChain
        }
    }


