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

import static telran.daily_farm.api.messages.ErrorMessages.*;

import java.io.IOException;
import java.util.Date;


@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private final JwtService jwtService;
	private final UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		String requestURI = request.getRequestURI();
		log.trace("OncePerRequestFilter. requestURI" + requestURI);
		if (requestURI.equals("/farmers/refresh") || requestURI.equals("/swagger-ui.html") 
				|| requestURI.startsWith("/swagger") || requestURI.startsWith("/v3")) {
			log.trace("OncePerRequestFilter. Refresh token run");
			chain.doFilter(request, response);
			return;
		}

		String token = request.getHeader("Authorization");
		log.info("JwtAuthenticationFilter(OncePerRequestFilter). Token received from header " + token);
		if (token != null && token.startsWith("Bearer ")) {
			token = token.substring(7);
			log.trace("OncePerRequestFilter. Token is not null. ");
			try {
				String username = jwtService.extractUserEmail(token);
				log.info("JwtAuthenticationFilter(OncePerRequestFilter). User name recived from token - " + username);
				UserDetails userDetails = userDetailsService.loadUserByUsername(username);
				log.info("OncePerRequestFilter. Recived userDetails + role");
				log.info("OncePerRequestFilter. jwtService.isTokenValid - "
						+ jwtService.isTokenValid(token, userDetails.getUsername()));
				log.info("OncePerRequestFilter. jwtService.extractExpiration - " + jwtService.extractExpiration(token));
				log.info("OncePerRequestFilter. Date now - " + new Date());
				log.info("OncePerRequestFilter. jwtService.extractExpiration - "
						+ jwtService.extractExpiration(token).after(new Date()));
				if (jwtService.isTokenValid(token, userDetails.getUsername())
						&& jwtService.extractExpiration(token).after(new Date())) {
					log.info("OncePerRequestFilter. Token checked - valid");

					UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					SecurityContextHolder.getContext().setAuthentication(authentication);
				} else
					throw new JwtException(INVALID_TOKEN);
			} catch (ExpiredJwtException | SecurityException | MalformedJwtException e) {
				throw new JwtException(INVALID_TOKEN);

			}
		}
		chain.doFilter(request, response);
	}
}