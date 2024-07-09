package com.conceptandcoding.eazybankrestapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class ProjectSecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // By default, Spring Security framework protects all the paths (request calls) present inside the web application.
        // Ref: SpringBootWebSecurityConfiguration class ---> defaultSecurityFilterChain()

        // We can secure the web application APIs/paths as per our custom requirements.

        http.authorizeHttpRequests(requests ->
            requests
                    //.anyRequest().authenticated()
                    .requestMatchers("/myAccount", "/myBalance", "/myLoans", "/myCards").authenticated()
                    .requestMatchers("/notices", "/contact").permitAll()
        );

        http.formLogin(Customizer.withDefaults());

        http.httpBasic(Customizer.withDefaults());

        return http.build();
    }

    // If you saved plaintext passwords without prefix `{noop}` in db,
    // you should have to define NoOpPasswordEncoder bean

    @Bean
    PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance(); // new NoOpPasswordEncoder() - incorrect - its immutable class
    }

}
