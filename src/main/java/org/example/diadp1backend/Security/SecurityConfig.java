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
      .cors(AbstractHttpConfigurer::disable)
      .csrf(AbstractHttpConfigurer::disable)
      .authorizeHttpRequests(req ->
        req
          .requestMatchers(("/auth/**")).permitAll()
          .requestMatchers(GET, "/usuario/eliminarUsuario").permitAll()
          .requestMatchers(GET, "/usuario/banearUsuario").hasAnyAuthority("true")
          .requestMatchers(GET, "/publicacion/eliminarPublicacion").permitAll()
          .requestMatchers(GET, "/publicacion/eliminarComentario").permitAll()
          .requestMatchers(GET, "/publicacion/**").permitAll()
          .requestMatchers(GET, "/chat/**").permitAll()
          .requestMatchers(GET, "/perfil/**").permitAll()
          .requestMatchers(GET, "/comentario/**").permitAll()
          .requestMatchers(GET,"/publicacion/buscar").permitAll()
                .anyRequest().authenticated()
      )
      .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
      .authenticationProvider(authenticationProvider)
      .addFilterBefore(jwtFilterChain, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }


}
