package org.example.diadp1backend.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.example.diadp1backend.modelos.Usuario;

import java.security.Key;
import java.util.LinkedHashMap;
import java.util.Map;

public class JWTService {


  //Método que a partir de un usuario genere un token
  public String generateToken(Usuario usuario){
    TokenDataDTO tokenDataDTO = TokenDataDTO
      .builder()
      .username(usuario.getUsername())
      .rol(usuario.getEsAdmin().toString())
      .fecha_creacion(System.currentTimeMillis())
      .fecha_expiracion(System.currentTimeMillis() + 1000*60*60*3)
      .build();

    return Jwts
              .builder()
              .claim("TokenDataDTO", tokenDataDTO)
              .signWith(getSignInKey(), SignatureAlgorithm.HS256)
              .comparct();
  }

  //Método que a  partir de un token te el usuario

  // Este metodo nos saca todo la informacion del token, con cabecera y clave

  private Claims extractDatosToken(String token){
    return Jwts
              .parser()
              .setSigningKey(getSignInKey())
              .build()
              .parseClaimsJws(token)
              .getBody();
  }

  //Este metodo solo saca la información que va dentro del json, es decir los datos del DTO

  public TokenDataDTO extractTokenData (String token){
    Claims claims = extractDatosToken(token);
    Map<String, Object> mapa = (LinkedHashMap<String,Object>) claims.get("tokenDataDTO");
    return TokenDataDTO.builder()
            .username((String) mapa.get("username"))
            .rol((String) mapa.get("rol"))
            .fecha_creacion((Long) mapa.get("fecha_creacion"))
            .fecha_expiracion((Long) mapa.get("fecha_expiracion"))
            .build();
  }



  //Método que coge la clave secreta y ver si el token es válido


  //Método que sabe la clave de encriptación
private Key getSignInKey(){
  return Keys.hmacShaKeyFor("claveSecreta".getBytes());




}
