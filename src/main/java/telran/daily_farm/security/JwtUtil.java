package telran.daily_farm.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;


@Component
@Slf4j
public class JwtUtil {
    @Value("${jwt.secret:MySuperSecretKeyMySuperSecretKeyMySuperSecretKey}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expirationTime;

    private Key getSigningKey() {
    	Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)); 
        return  key;
    }

    public String generateToken(String email) {
    	String token = Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSigningKey()) 
                .compact();
        return token;
    }

    @SuppressWarnings("deprecation")
	private JwtParser getParser() {
        return Jwts.parser() 
                .setSigningKey(getSigningKey())  
                .build();
    }

    @SuppressWarnings("deprecation")
	private Claims extractAllClaims(String token) {
        return getParser().parseClaimsJws(token).getBody();  
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public boolean isTokenValid(String token, String email) {
        return extractUsername(token).equals(email) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}