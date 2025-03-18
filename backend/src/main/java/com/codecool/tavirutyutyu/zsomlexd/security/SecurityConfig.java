package com.codecool.tavirutyutyu.zsomlexd.security;

import com.codecool.tavirutyutyu.zsomlexd.security.jwt.AuthEntryPointJwt;
import com.codecool.tavirutyutyu.zsomlexd.security.jwt.AuthTokenFilter;
import com.codecool.tavirutyutyu.zsomlexd.security.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final AuthEntryPointJwt unauthorisedHandler;

    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService, JwtUtil jwtUtil, AuthEntryPointJwt unauthorisedHandler) {
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.unauthorisedHandler = unauthorisedHandler;
    }

    public AuthTokenFilter getAuthTokenFilter() {
        return new AuthTokenFilter(jwtUtil, userDetailsService);
    }

    public DaoAuthenticationProvider getAuthProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(getPasswordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorisedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/api/user/**").hasRole("USER")
                                .requestMatchers("/api/comment/addComment").hasRole("USER")
                                .requestMatchers("/api/comment/id/**").permitAll()
                                .requestMatchers("/api/song/all").permitAll()
                                .requestMatchers("/api/song/search").permitAll()
                                .requestMatchers("/api/song/stream/**").permitAll()
                                .requestMatchers("/api/song/id/**").permitAll()
                                .requestMatchers("/api/song/delete/id/**").hasRole("USER")
                                .requestMatchers("/api/song/upload").hasRole("USER")
                                .requestMatchers("/api/auth/**").permitAll()
                                .requestMatchers("/api/playlist/all").permitAll()
                                .requestMatchers("/api/playlist/id/**").permitAll()
                                .requestMatchers("/api/playlist/upload").hasRole("USER")
                                .anyRequest().authenticated()
                );
        http.addFilterBefore(getAuthTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        http.authenticationProvider(getAuthProvider());
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(
                "http://localhost:3000",
                "http://localhost:8080",
                "http://rollenxd_frontend:3000",
                "http://rollenxd_frontend:8080",
                "http://frontend:3000"
        ));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("Authorization"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
