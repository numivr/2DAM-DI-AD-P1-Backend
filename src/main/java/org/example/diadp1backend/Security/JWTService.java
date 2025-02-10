package org.example.diadp1backend.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.example.diadp1backend.modelos.Usuario;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTService {

  private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

  //Método que a partir de un usuario genere un token
  public String generateToken(Usuario usuario) {
    TokenDataDTO tokenDataDTO = TokenDataDTO
      .builder()
      .username(usuario.getUsername())
      .rol(String.valueOf(usuario.getEsAdmin())) // Convertir a String
      .fecha_creacion(System.currentTimeMillis())
      .fecha_expiracion(System.currentTimeMillis() + 1000 * 60 * 60 * 3) // Expira en 3 horas
      .build();

    String token = Jwts.builder()
      .setClaims(Map.of("TokenDataDTO", tokenDataDTO))
      .setIssuedAt(new Date())
      .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 3))
      .signWith(getSignInKey(), SignatureAlgorithm.HS256)
      .compact();

    System.out.println("✅ Token generado correctamente: " + token);
    return token;
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

  public String extractUsername(String token) {
    try {
      System.out.println("🔹 Token recibido para extraer username: " + token);

      Claims claims = Jwts.parserBuilder()
        .setSigningKey(getSignInKey())
        .build()
        .parseClaimsJws(token)
        .getBody();

      System.out.println("🔹 Claims obtenidos: " + claims);

      // Extraer datos del objeto TokenDataDTO
      Object tokenDataObj = claims.get("TokenDataDTO");

      if (tokenDataObj == null) {
        throw new RuntimeException("❌ No se encontró TokenDataDTO en el token");
      }

      if (!(tokenDataObj instanceof Map)) {
        throw new RuntimeException("❌ TokenDataDTO no tiene el formato correcto");
      }

      @SuppressWarnings("unchecked")
      Map<String, Object> tokenData = (Map<String, Object>) tokenDataObj;

      if (tokenData.containsKey("username")) {
        return tokenData.get("username").toString();
      } else {
        throw new RuntimeException("❌ El campo 'username' no está en TokenDataDTO.");
      }

    } catch (Exception e) {
      System.err.println("❌ Error al extraer username del JWT: " + e.getMessage());
      return null;
    }
  }


  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
    final Claims claims = extractDatosToken(token);
    return claimsResolver.apply(claims);
  }

  //Este metodo solo saca la información que va dentro del json, es decir los datos del DTO

  public TokenDataDTO extractTokenData(String token) {
    Claims claims = extractDatosToken(token);

    System.out.println("Claims obtenidos: " + claims); // <- Ver qué contiene el token

    if (claims == null) {
      throw new RuntimeException("El token es inválido o no contiene claims");
    }

    Object tokenDataObj = claims.get("TokenDataDTO"); // <- Aquí cambia el nombre

    if (tokenDataObj == null) {
      throw new RuntimeException("El token no contiene TokenDataDTO");
    }

    if (!(tokenDataObj instanceof Map)) {
      throw new RuntimeException("El TokenDataDTO no tiene el formato esperado");
    }

    @SuppressWarnings("unchecked")
    Map<String, Object> mapa = (Map<String, Object>) tokenDataObj;

    // Verificar si el mapa tiene las claves necesarias
    if (!mapa.containsKey("username") || !mapa.containsKey("rol") ||
      !mapa.containsKey("fecha_creacion") || !mapa.containsKey("fecha_expiracion")) {
      throw new RuntimeException("El TokenDataDTO está incompleto");
    }

    return TokenDataDTO.builder()
      .username((String) mapa.getOrDefault("username", ""))
      .rol((String) mapa.getOrDefault("rol", ""))
      .fecha_creacion(mapa.get("fecha_creacion") instanceof Number
        ? ((Number) mapa.get("fecha_creacion")).longValue()
        : null)
      .fecha_expiracion(mapa.get("fecha_expiracion") instanceof Number
        ? ((Number) mapa.get("fecha_expiracion")).longValue()
        : null)
      .build();
  }





  public boolean isTokenValid(String token, Usuario usuario) {
    try {
      final String username = extractUsername(token);
      return (username != null && username.equals(usuario.getUsername())) && !isTokenExpired(token);
    } catch (Exception e) {
      System.err.println("❌ Error en isTokenValid: " + e.getMessage());
      return false;
    }
  }




  //Metodo que coge la clave secreta y ver si el token es válido


    //Metodo que sabe la clave de encriptación
  private Key getSignInKey() {
    return key;
   }

  private boolean isTokenExpired(String token){
    return extractExpiration(token).before(new Date());
  }

  private Date extractExpiration(String token){
    return extractClaim(token, Claims::getExpiration);
  }




}
