package com.software.grey.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig {

    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource){
        JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);
        userDetailsManager.setUsersByUsernameQuery("SELECT username, password, enabled FROM user WHERE username=?");
        userDetailsManager.setAuthoritiesByUsernameQuery("SELECT username, role FROM user WHERE username=?");
        return userDetailsManager;
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(configurer ->
            configurer
                    .requestMatchers("/").hasAnyRole("USER", "MODERATOR", "ADMIN")
                    .requestMatchers(HttpMethod.GET, "/test").hasRole("ADMIN")
                    .anyRequest().authenticated()
        ).formLogin(Customizer.withDefaults())
                .logout(logout -> logout.permitAll())
                .formLogin( f -> f.defaultSuccessUrl("/"));
//                .formLogin(form -> form
//                .loginPage("/login")
//                .loginProcessingUrl("/authenticateTheUser")
//                .permitAll()
//        );


        http.httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
