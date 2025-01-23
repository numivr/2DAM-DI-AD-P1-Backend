package org.example.diadp1backend.Security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.diadp1backend.modelos.Usuario;
import org.example.diadp1backend.servicios.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JWTFilter extends OncePerRequestFilter {

   @Autowired
    private  JWTService jwtService;

   @Autowired
   private UsuarioService usuarioService;



    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
                                    throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

       //Si viene por la url "auth" lo dejamos pasar
      if (request.getServletPath().contains("/auth")){
        filterChain.doFilter(request, response);
        return;
      }

      if(authHeader==null || !authHeader.startsWith("Bearer ")){
        filterChain.doFilter(request, response);
        return;
      }

      String token = authHeader.substring(7);
      TokenDataDTO tokenDataDTO = jwtService.extractTokenData(token);

      if(tokenDataDTO != null && SecurityContextHolder.getContext().getAuthentication() == null){
        Usuario usuario = usuarioService.loadUserByUsername(tokenDataDTO.getUsername());
      })

        filterChain.doFilter(request, response);
    }
}
