package vn.intracom.chuongtrinhdaotao.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import vn.intracom.chuongtrinhdaotao.security.AuthEntryPoint;
import vn.intracom.chuongtrinhdaotao.security.CustomUserDetailsService;
import vn.intracom.chuongtrinhdaotao.security.JwtAuthFilter;
import org.springframework.http.HttpMethod;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JwtAuthFilter jwtAuthFilter;
    private final AuthEntryPoint authEntryPoint;

    private static final String[] PUBLIC_URLS = {
            "/api/auth/**",
            "/api/files/decuong/**",   // ← thêm dòng này — xem PDF không cần login
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configure(http))
            .exceptionHandling(ex -> ex
                    .authenticationEntryPoint(authEntryPoint))
            .sessionManagement(session -> session
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            .authorizeHttpRequests(auth -> auth
                    .requestMatchers(PUBLIC_URLS).permitAll()
                    .requestMatchers("/api/admin/**")
                        .hasAuthority("ADMIN")
                    .requestMatchers(HttpMethod.POST, "/api/**")
                        .hasAnyAuthority("ADMIN", "GIANG_VIEN")
                    .requestMatchers(HttpMethod.PUT, "/api/**")
                        .hasAnyAuthority("ADMIN", "GIANG_VIEN")
                    .requestMatchers(HttpMethod.DELETE, "/api/**")
                        .hasAuthority("ADMIN")
                    .requestMatchers(HttpMethod.GET, "/api/**")
                        .authenticated()
                    .anyRequest().authenticated())

            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // ✅ PRODUCTION: return new BCryptPasswordEncoder();
        // 🚧 TESTING:
        return NoOpPasswordEncoder.getInstance();
    }
}