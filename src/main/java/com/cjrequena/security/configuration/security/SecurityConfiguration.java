package com.cjrequena.security.configuration.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
@RequiredArgsConstructor
public class SecurityConfiguration {

  //private final JWTApplicationPrincipalAuthenticationFilter jwtApplicationPrincipalAuthenticationFilter;
  private final BasicAuthUserDetailsService basicAuthUserDetailsService;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    return httpSecurity
      .csrf(AbstractHttpConfigurer::disable)
      .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .formLogin(AbstractHttpConfigurer::disable)
      .logout(AbstractHttpConfigurer::disable)
      .httpBasic(Customizer.withDefaults())
      .authorizeHttpRequests(registry -> {
        registry.requestMatchers(toH2Console()).permitAll();
        // registry.requestMatchers("/api/rbac/users/**").hasAnyAuthority("create_user", "read_user", "update_user");
        // registry.requestMatchers("/api/rbac/permissions/**").hasAnyAuthority("create_permission", "read_permission", "update_permission");
        // registry.requestMatchers("/api/rbac/roles/**").hasAnyAuthority("create_role", "read_role", "update_role");
        registry.requestMatchers("/api/rbac/admin/**").hasRole("ADMIN");
        registry.requestMatchers("/api/rbac/public/**").permitAll();
        registry.anyRequest().authenticated();
      })
      .build();
  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(this.basicAuthUserDetailsService);
    provider.setPasswordEncoder(passwordEncoder());
    return provider;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
