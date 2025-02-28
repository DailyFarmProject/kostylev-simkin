package telran.daily_farm.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.security.SecureRandom;
import java.util.Date;
import java.util.function.Function;


@Service
@Slf4j
public class JwtService {
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.verification.token.validity}")
    private long verificationTikenValidity;
    
    @Value("${jwt.access.token.validity}")
    private long accessTokenValidity;
    
    @Value("${jwt.refresh.token.validity}")
    private long refreshTokenValidity ;

    private Key getSigningKey() {
    	Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)); 
        return  key;
    }
    
    public String generatePassword(int length) {
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_+=<>?";
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(chars.length());
            password.append(chars.charAt(index));
        }
        
        return password.toString();
    }
    
    public String generateVerificationTokenForUpdateEmail(String uuid, String email, String newEmail) {
    	String token = Jwts.builder()
                .subject(uuid)
                .claim("email", email)
                .claim("newEmail", newEmail)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + verificationTikenValidity))
                
                .signWith(getSigningKey()) 
                .compact();
    	log.debug("JwtService. Token for farmer " + email+ "generated and returned to user");
        return token;
    }

    public String generateVerificationToken(String uuid, String email) {
    	String token = Jwts.builder()
                .subject(uuid)
                .claim("email", email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + verificationTikenValidity))
                
                .signWith(getSigningKey()) 
                .compact();
    	log.debug("JwtService. Token for farmer " + email+ "generated and returned to user");
        return token;
    }
    public String generateAccessToken(String uuid, String email) {
        return Jwts.builder()
        		 .subject(uuid)
                 .claim("email", email)
                 .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + accessTokenValidity))
                .signWith(getSigningKey()) 
                .compact();
    }

    public String generateRefreshToken(String uuid, String email) {
        return Jwts.builder()
        		 .subject(uuid)
                 .claim("email", email)
                 .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + refreshTokenValidity))
                .signWith(getSigningKey()) 
                .compact();
    }
    
    public String extractUserId(String token) {
        return extractClaim(token, Claims::getSubject);
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

    public String extractUserEmail(String token) {
        return extractClaim(token, claims -> claims.get("email", String.class));
    }
    
    public String extractUserNewEmail(String token) {
        return extractClaim(token, claims -> claims.get("newEmail", String.class));
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public boolean isTokenValid(String token, String email) {
        return extractUserEmail(token).equals(email) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}