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
import org.springframework.web.filter.OncePerRequestFilter;

import daily_farm.messages.ErrorMessages;

import java.io.IOException;

import javax.security.sasl.AuthenticationException;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtUtil;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String token = request.getHeader("Authorization");
        log.error("JwtAuthenticationFilter(OncePerRequestFilter). Token received from header ");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            log.debug("OncePerRequestFilter. Token is not null. ");
            try {
                String username = jwtUtil.extractUserEmail(token);
                log.debug("JwtAuthenticationFilter(OncePerRequestFilter). User name recived from token");
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                log.debug("OncePerRequestFilter. Recived userDetails + role");
                if (jwtUtil.isTokenValid(token, userDetails.getUsername())) {
                	 log.debug("OncePerRequestFilter. Token checked - valid");
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }else
                	throw new JwtException(ErrorMessages.INVALID_TOKEN);
            } catch (ExpiredJwtException | SecurityException | MalformedJwtException e) {
//                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ErrorMessages.INVALID_TOKEN);
         	 throw new JwtException(ErrorMessages.INVALID_TOKEN);
                
            }
        }
        chain.doFilter(request, response);
    }
}