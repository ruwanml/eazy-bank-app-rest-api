package com.conceptandcoding.eazybankrestapi.config;

import com.conceptandcoding.eazybankrestapi.constant.SecurityConstant;
import com.conceptandcoding.eazybankrestapi.filter.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class ProjectSecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // configure KeycloakRoleConverter
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KeycloakRoleConverter());

        // CORS configurations - globally
        // Ref: https://docs.spring.io/spring-security/reference/servlet/integrations/cors.html

        http.cors((cors) -> cors
                .configurationSource(apiConfigurationSource())
        );

        // By default, CSRF protection is enabled.
        // You can also consider whether only certain endpoints do not require CSRF protection and configure an ignoring rule.
        // ex: '/contact' and '/register' - public POST APIs

        // Ref: https://docs.spring.io/spring-security/reference/servlet/exploits/csrf.html

        http.csrf((csrf) -> csrf
                .ignoringRequestMatchers("/contact", "/register")
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
        );

        // Custom filters

        http.addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class);

        //http.addFilterBefore(new RequestValidationBeforeFilter(), BasicAuthenticationFilter.class);

        //http.addFilterAfter(new AuthoritiesLoggingAfterFilter(), BasicAuthenticationFilter.class);

        //http.addFilterAfter(new JwtTokenGeneratorFilter(), BasicAuthenticationFilter.class);

        //http.addFilterBefore(new JwtTokenValidatorFilter(), BasicAuthenticationFilter.class);

        // By default, Spring Security framework protects all the paths (request calls) present inside the web application.
        // Ref: SpringBootWebSecurityConfiguration class ---> defaultSecurityFilterChain()

        // We can secure the web application APIs/paths as per our custom requirements.

        http.authorizeHttpRequests((requests) -> requests
                // authorization rules by AUTHORITY
                //.requestMatchers("/myAccount").hasAuthority("VIEWACCOUNT")
                //.requestMatchers("/myBalance").hasAnyAuthority("VIEWBALANCE", "VIEWACCOUNT")
                //.requestMatchers("/myLoans").hasAuthority("VIEWLOANS")
                //.requestMatchers("/myCards").hasAuthority("VIEWCARDS")
                // authorization rules by ROLES
                .requestMatchers("/myAccount").hasRole("USER")
                .requestMatchers("/myBalance").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/myLoans").authenticated()
                .requestMatchers("/myCards").hasRole("USER")
                    // authentication rules
                    //.anyRequest().authenticated()
                    //.requestMatchers("/myAccount", "/myBalance", "/myLoans", "/myCards", "/user").authenticated()
                    .requestMatchers("/user").authenticated()
                    .requestMatchers("/notices", "/contact", "/register").permitAll()
        );

        //http.formLogin(Customizer.withDefaults());

        //http.httpBasic(Customizer.withDefaults());

        //http.csrf(csrf -> csrf.disable());

        http.oauth2ResourceServer(rsc -> rsc
                .jwt(jwtConfigurer -> jwtConfigurer
                        .jwtAuthenticationConverter(jwtAuthenticationConverter)
                )
        );

        return http.build();
    }

    private CorsConfigurationSource apiConfigurationSource() {
        CorsConfiguration configs = new CorsConfiguration();
        configs.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
        //configs.setAllowedMethods(Arrays.asList("GET","POST"));
        configs.setAllowedMethods(Arrays.asList("*"));
        configs.setAllowCredentials(true);
        configs.setAllowedHeaders(Arrays.asList("*"));
        //configs.setExposedHeaders(Arrays.asList(SecurityConstant.JWT_HEADER));
        configs.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configs);

        return source;
    }

    //@Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
