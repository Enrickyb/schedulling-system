package com.example.scheduling.config;


import com.example.scheduling.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(org.springframework.security.config.annotation.web.builders.HttpSecurity http, AccessDeniedHandler accessDeniedHandler, AuthenticationEntryPoint authenticationEntryPoint) throws Exception {
        return http
                .cors(cors -> cors.configure(http))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll() // 🔓 Permitir login e registro sem autenticação
                        .requestMatchers("/api/users/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/businesses").hasAnyAuthority("ADMIN", "BUSINESS_OWNER")  // 🔹 Permite GET para todos
                        .requestMatchers(HttpMethod.GET, "/api/businesses").permitAll() // 🔹
                        .requestMatchers(HttpMethod.GET, "/api/businesses/**").permitAll()  // 🔹 Permite GET para todos
                        .requestMatchers("/api/businesses/owner/**").hasAnyAuthority("BUSINESS_OWNER", "ADMIN")  // 🔹 Permite GET para BUSINESS_OWNER e ADMIN
                        .requestMatchers(HttpMethod.POST, "/api/appointments").permitAll()  // 🔹 Permite POST para todos
                        .requestMatchers(HttpMethod.GET, "/api/appointments/").hasAnyAuthority("CUSTOMER", "ADMIN", "BUSINESS_OWNER") // 🔹 Permite GET para todos
                        .requestMatchers("/api/appointments/business/**").hasAnyAuthority("BUSINESS_OWNER", "ADMIN")
                        .requestMatchers("/api/appointments/customer/**").hasAnyAuthority("CUSTOMER", "ADMIN")
                        .requestMatchers("/api/appointments/cancel/**").hasAnyAuthority("BUSINESS_OWNER", "ADMIN", "CUSTOMER")
                        .requestMatchers("/api/appointments/complete/**").hasAnyAuthority("BUSINESS_OWNER", "ADMIN")
                        .requestMatchers("/api/guest").permitAll()
                        .requestMatchers("/api/guest/all").hasAnyAuthority("BUSINESS_OWNER", "ADMIN")
                        .requestMatchers("/api/guest/**").permitAll()
                        .requestMatchers("/api/business-settings/**").hasAnyAuthority("BUSINESS_OWNER", "ADMIN")
                        .requestMatchers("/api/services/business/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/services/**").hasAnyAuthority("BUSINESS_OWNER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/services/**").hasAnyAuthority("BUSINESS_OWNER", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/services/**").hasAnyAuthority("BUSINESS_OWNER", "ADMIN")
                        .requestMatchers("api/services/available-times").permitAll()

                )
                .exceptionHandling(ex -> ex
                        .accessDeniedHandler(accessDeniedHandler)  // 🔹 Personaliza resposta de 403 (Forbidden)
                        .authenticationEntryPoint(authenticationEntryPoint)  // 🔹 Personaliza resposta de 401 (Unauthorized)
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(authProvider);
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
