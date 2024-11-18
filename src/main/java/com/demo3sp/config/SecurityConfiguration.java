package com.demo3sp.config;

//import com.demo.service.ShopmeUserDetailsService;

import com.demo3sp.service.CustomUserDetailService;
import com.demo3sp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class SecurityConfiguration {

    @Autowired
    private CustomUserDetailService userDetailsService;

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailService();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }



    // Creates a DaoAuthenticationProvider to handle user authentication
    @Bean
    public DaoAuthenticationProvider authenticationProvider(PasswordEncoder passwordEncoder, UserDetailsService userDetailsService) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
//        final List<GlobalAuthenticationConfigurerAdapter> globalAuthConfigurers = new ArrayList<>();
//        globalAuthConfigurers.add(new GlobalAuthenticationConfigurerAdapter() {
//            @Override
//            public void configure(AuthenticationManagerBuilder auth) throws Exception {
////                auth.authenticationProvider(authenticationProvider());
//                //do somthing
//            }
//        });
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/").hasAnyAuthority("USER", "ADMIN","EDITOR","CREATOR")
                        .requestMatchers("/new").hasAnyAuthority("ADMIN","CREATOR")
                        .requestMatchers("/edit/**").hasAnyAuthority("ADMIN","EDITOR")
                        .requestMatchers("/delete/**").hasAnyAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/**").permitAll()
                        .requestMatchers( "/api/**").permitAll()
                        .requestMatchers("/login","/error").permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .formLogin(
                        formLogin -> formLogin
                                .loginPage("/login")
                                .successForwardUrl("/login_success_handler") // Your custom login success handler
                                .failureForwardUrl("/login_failure_handler") // Your custom login failure handler
                )
                .logout(Customizer.withDefaults())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        .invalidSessionUrl("/login")
                )
                .exceptionHandling(e -> e
                        .accessDeniedPage("/403")
                )
                .build();
    }



}