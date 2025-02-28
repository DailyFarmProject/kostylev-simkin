package telran.daily_farm.security;

import io.jsonwebtoken.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import static telran.daily_farm.api.messages.ErrorMessages.*;

import java.io.IOException;
import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        log.trace("OncePerRequestFilter. requestURI: " + requestURI);

        
        if (requestURI.equals("/farmers/refresh") || requestURI.equals("/customer/refresh") || 
            requestURI.equals("/swagger-ui.html") || requestURI.startsWith("/swagger") || 
            requestURI.startsWith("/v3")) {
            log.trace("OncePerRequestFilter. Refresh token run");
            chain.doFilter(request, response);
            return;
        }

        String token = request.getHeader("Authorization");
        log.info("JwtAuthenticationFilter. Token received from header: " + token);

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            log.trace("OncePerRequestFilter. Token is not null.");
            try {
                String username = jwtService.extractUserEmail(token);
                log.info("JwtAuthenticationFilter. Extracted user email from token: " + username);

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                log.info("OncePerRequestFilter. Received userDetails + role: " + userDetails.getAuthorities());

                log.info("JwtAuthenticationFilter. Checking token validity...");
                boolean isValid = jwtService.isTokenValid(token, userDetails.getUsername());
                Date tokenExpiration = jwtService.extractExpiration(token);
                boolean isNotExpired = tokenExpiration.after(new Date());

                log.info("JwtAuthenticationFilter. Token valid: " + isValid);
                log.info("JwtAuthenticationFilter. Token expiration date: " + tokenExpiration);
                log.info("JwtAuthenticationFilter. Current date: " + new Date());
                log.info("JwtAuthenticationFilter. Token not expired: " + isNotExpired);

                if (isValid && isNotExpired) {
                    log.info("JwtAuthenticationFilter. Token is valid. Authenticating user...");

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    throw new JwtException(INVALID_TOKEN);
                }
            } catch (ExpiredJwtException | SecurityException | MalformedJwtException e) {
                throw new JwtException(INVALID_TOKEN);
            }
        }
        chain.doFilter(request, response);
    }
}
