package com.example.electro.config;
import com.example.electro.filter.JwtAuthFilter;
import com.example.electro.repository.CustomerRepository;
import com.example.electro.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private JwtAuthFilter authFilter;

    private CustomerRepository customerRepository;

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomerService(customerRepository); // Ensure UserInfoService implements UserDetailsService
    }

    @Autowired
    public SecurityConfig(JwtAuthFilter authFilter, CustomerRepository customerRepository) {
        this.authFilter = authFilter;
        this.customerRepository = customerRepository;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable()) // Disable CSRF for stateless APIs
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/auth/welcome", "/auth/addNewUser", "/auth/generateToken","/auth/login").permitAll()
//                        .requestMatchers("/auth/user/**").hasAuthority("ROLE_USER")
//                        .requestMatchers("/auth/admin/**").hasAuthority("ROLE_ADMIN")
//                        .anyRequest().authenticated() // Protect all other endpoints
//                )
//                .sessionManagement(sess -> sess
//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // No sessions
//                )
//                .authenticationProvider(authenticationProvider()) // Custom authentication provider
//                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class); // Add JWT filter
//
//        return http.build();

        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for stateless APIs
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // Allow all requests without restrictions
                )
                .sessionManagement(sess -> sess
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // No sessions
                )
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class); // Add JWT filter

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Password encoding
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
