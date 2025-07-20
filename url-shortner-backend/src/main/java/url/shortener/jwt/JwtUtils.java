package url.shortener.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import url.shortener.service.UserDetailsImpl;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String jwtSeceret;

    @Value("${jwt.expiration}")
    private long expiration;

    //Authorization -> Bearer <TOKEN>
    public String getJwtFromHeader(HttpServletRequest req){
        String bearerToken = req.getHeader("Authorization");
        if(bearerToken != null && bearerToken.startsWith("Bearer ")){
            return  bearerToken.substring(7);
        }
        return null;
    }

    public String generateToken(UserDetailsImpl userDetails){
        String username = userDetails.getUsername();

        String roles = userDetails.getAuthorities().stream()
                .map(authority ->"ROLE_" + authority.getAuthority())
                .collect(Collectors.joining(","));

        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", roles);

        String t =  Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + expiration))
                .signWith(key())
                .compact();
         return t;
    }

    public String getUsernameFromJwtToken(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key()) // key() method should return the signing Key
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject(); // The subject is where username was stored during token creation
    }

    public boolean validateToken(String authToken){
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key()) // Your secret key
                    .build()
                    .parseClaimsJws(authToken); // Parses and validates the JWT
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            System.out.println("Invalid JWT signature: " + e.getMessage());
        } catch (ExpiredJwtException e) {
            System.out.println("JWT token is expired: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.out.println("JWT token is unsupported: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("JWT claims string is empty: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unknown error occurred : " + e.getMessage());
        }
        return false;
    }


    //imported from key.security
    // and the k of Keys is capital
    // and jwtSecret is coming from application properties
    private Key key(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSeceret));
    }
}
