package com.software.grey.security;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


import javax.sql.DataSource;

import static com.software.grey.utils.EndPoints.*;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final OAuth2LoginSuccessHandler oauth2LoginSuccessHandler;
    private final BasicLoginSuccessHandler basicLoginSuccessHandler;
    private final BasicLoginFailureHandler basicLoginFailureHandler;


    @Value("${front.url}")
    private String frontUrl;

    public SecurityConfig(OAuth2LoginSuccessHandler oauth2LoginSuccessHandler, BasicLoginSuccessHandler basicLoginSuccessHandler, BasicLoginFailureHandler basicLoginFailureHandler) {
        this.oauth2LoginSuccessHandler = oauth2LoginSuccessHandler;
        this.basicLoginSuccessHandler = basicLoginSuccessHandler;
        this.basicLoginFailureHandler = basicLoginFailureHandler;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);
        userDetailsManager.setUsersByUsernameQuery("""
                SELECT username, password, enabled
                FROM user
                JOIN user_basic_auth ON user.id = user_basic_auth.local_id
                WHERE username=?""");
        userDetailsManager.setAuthoritiesByUsernameQuery("SELECT username, role FROM user WHERE username=?");
        return userDetailsManager;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers(HttpMethod.POST, SIGNUP).permitAll()
                                .requestMatchers(HttpMethod.GET, LOGIN_SUCCESS).permitAll()
                                .requestMatchers(HttpMethod.GET, LOGIN_FAIL).permitAll()
                                .requestMatchers(HttpMethod.PUT, VERIFY_REGISTERATION).permitAll()
                                .requestMatchers(HttpMethod.GET, TEST).permitAll()
                                .anyRequest()
                                .authenticated()
                )
                .oauth2Login(oauth2 ->
                        oauth2.successHandler(oauth2LoginSuccessHandler))
                .formLogin(success ->
                        success.successHandler(basicLoginSuccessHandler)
                                .failureHandler(basicLoginFailureHandler))
                .logout(LogoutConfigurer::permitAll)
                .httpBasic(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults());
        return http.build();
    }
}