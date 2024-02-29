package com.hanyoonsoo.springtoy.module.global.security;

import com.hanyoonsoo.springtoy.module.global.config.encryption.AES128Config;
import com.hanyoonsoo.springtoy.module.global.config.redis.RedisService;
import com.hanyoonsoo.springtoy.module.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.*;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
@Slf4j
public class SecurityConfiguration {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final AES128Config aes128Config;
    private final RedisService redisService;

    private static final List<String> EXCLUDE_URL =
            List.of("/",
                    "/h2",
                    "/users/signup",
                    "/auth/login",
                    "/auth/reissue",
                    "/docs/index.html",
                    "/items");
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.headers((headerConfig) -> headerConfig.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));
        http.csrf(AbstractHttpConfigurer::disable).cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource()));
        http.formLogin(FormLoginConfigurer::disable).httpBasic(HttpBasicConfigurer::disable)
                .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.exceptionHandling(configurer -> configurer.authenticationEntryPoint(new CustomAuthenticationEntryPoint()))
                .exceptionHandling(configurer -> configurer.accessDeniedHandler(new CustomAccessDenidedHandler()));
        http.apply(new CustomFilterConfigurer());
        http.authorizeHttpRequests(
                authorize -> authorize.requestMatchers(EXCLUDE_URL.stream()
                                .map(AntPathRequestMatcher::new)
                                .toArray(RequestMatcher[]::new)).permitAll()
                .requestMatchers("/orders/**").hasRole("VERIFIED_USER")
                        .anyRequest().hasAnyRole("USER", "VERIFIED_USER"));

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PATCH", "DELETE"));
        configuration.setAllowCredentials(true);
        configuration.addExposedHeader("Authorization");
        configuration.addExposedHeader("Refresh");
        configuration.addAllowedHeader("*");
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    public class CustomFilterConfigurer extends AbstractHttpConfigurer<CustomFilterConfigurer, HttpSecurity> {
        @Override
        public void configure(HttpSecurity builder) throws Exception {
            log.info("SecurityConfiguration.CustomFilterConfigurer.configure excute");
            AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);
            JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager,
                    jwtTokenProvider, userService, redisService, aes128Config);

            JwtVerificationFilter jwtVerificationFilter = new JwtVerificationFilter(jwtTokenProvider, redisService);

            jwtAuthenticationFilter.setFilterProcessesUrl("/auth/login");
            jwtAuthenticationFilter.setAuthenticationSuccessHandler(new LoginSuccessHandler());
            jwtAuthenticationFilter.setAuthenticationFailureHandler(new LoginFailurHandler());

            builder
                    .addFilter(jwtAuthenticationFilter)
                    .addFilterAfter(jwtVerificationFilter, JwtAuthenticationFilter.class);
        }
    }

    @Bean
    public static BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
