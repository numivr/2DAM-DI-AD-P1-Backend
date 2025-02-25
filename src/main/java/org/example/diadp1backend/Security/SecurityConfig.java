package org.example.diadp1backend.Security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.security.authorization.AuthorityReactiveAuthorizationManager.hasAnyAuthority;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JWTFilter jwtFilterChain;
  private final AuthenticationProvider authenticationProvider;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
      .csrf(AbstractHttpConfigurer::disable)
      .cors(cors -> cors.configurationSource(request -> {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("https://twodam-di-ad-p1-frontend-vvilchesgarcia.onrender.com/",
          "https://localhost",
          "http://localhost:4200"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        config.setAllowCredentials(true);
        return config;
      }))
      .authorizeHttpRequests(req ->
        req
          .requestMatchers(("/auth/**")).permitAll()
          .requestMatchers(GET, "/usuarios/**").permitAll()
          .requestMatchers(GET, "/usuarios/banear").hasAnyAuthority("true")
          .requestMatchers(GET, "/publicacion/eliminarPublicacion").permitAll()
          .requestMatchers(GET, "/publicacion/eliminarComentario").permitAll()
          .requestMatchers(GET, "/publicacion/**").permitAll()
          .requestMatchers(GET, "/chat/**").permitAll()
          .requestMatchers(GET, "/perfil/**").permitAll()
          .requestMatchers(GET, "/comentario/**").permitAll()
          .requestMatchers(GET,"/publicacion/buscar").permitAll()
          .requestMatchers("/mensajes/**").permitAll()
          .requestMatchers("/chat/**").permitAll()
          .anyRequest().authenticated()
      )
      .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
      .authenticationProvider(authenticationProvider)
      .addFilterBefore(jwtFilterChain, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }


}
