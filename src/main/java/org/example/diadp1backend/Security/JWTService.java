package org.example.diadp1backend.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.example.diadp1backend.modelos.Usuario;

import java.security.Key;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

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
              .compact();
  }

  //Método que a  partir de un token te el usuario

  // Este metodo nos saca todo la informacion del token, con cabecera y clave

  private Claims extractDatosToken(String token){
    return Jwts
              .parserBuilder()
              .setSigningKey(getSignInKey())
              .build()
              .parseClaimsJws(token)
              .getBody();
  }

  public String extractUsername(String token){
    return Jwts.parserBuilder()
      .setSigningKey(getSignInKey())
      .build()
      .parseClaimsJws(token)
      .getBody().getSubject();
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
    final Claims claims = extractDatosToken(token);
    return claimsResolver.apply(claims);
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


  public boolean isTokenValid(String token, Usuario usuario){
    final String username = extractUsername(token);
    return (username.equals(usuario.getUsername())) && !isTokenExpired(token);

  }



  //Metodo que coge la clave secreta y ver si el token es válido


    //Metodo que sabe la clave de encriptación
  private Key getSignInKey() {
    return Keys.hmacShaKeyFor("claveSecreta".getBytes());
   }

  private boolean isTokenExpired(String token){
    return extractExpiration(token).before(new Date());
  }

  private Date extractExpiration(String token){
    return extractClaim(token, Claims::getExpiration);
  }




}
