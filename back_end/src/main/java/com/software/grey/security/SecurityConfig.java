package com.software.grey.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

import static com.software.grey.utils.EndPoints.SIGNUP;
import static com.software.grey.utils.EndPoints.TEST;

@Configuration
public class SecurityConfig {
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource){
        JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);
        userDetailsManager.setUsersByUsernameQuery("SELECT username, password, enabled FROM user WHERE username=?");
        userDetailsManager.setAuthoritiesByUsernameQuery("SELECT username, role FROM user WHERE username=?");
        return userDetailsManager;
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth ->
                    auth
                            .requestMatchers(HttpMethod.POST, SIGNUP).permitAll()
                            .requestMatchers("/").hasAnyRole("USER", "MODERATOR", "ADMIN")
                            .requestMatchers(HttpMethod.GET, TEST).hasRole("ADMIN")
                            .anyRequest().authenticated()
            )
            .formLogin(Customizer.withDefaults())
            .logout(LogoutConfigurer::permitAll)
            .formLogin(f ->
                    f.defaultSuccessUrl("/")
            )
            .httpBasic(Customizer.withDefaults())
            .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

}