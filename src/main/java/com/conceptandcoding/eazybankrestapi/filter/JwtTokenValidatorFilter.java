package com.conceptandcoding.eazybankrestapi.filter;

import com.conceptandcoding.eazybankrestapi.constant.SecurityConstant;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class is responsible for validating the JWT token received from client applications.
 *
 * <p>After successful authentication, this filter validates the JWT token for all subsequent requests.</p>
 *
 * <p>The steps to validate the JWT token are as follows:</p>
 * <ul>
 *     <li>Retrieve the JWT token from the Authorization header.</li>
 *     <li>Generate the secret key using the same method as during token generation.</li>
 *     <li>Use the Jwts.parserBuilder to set the signing key and parse the claims from the JWT token.</li>
 *     <li>Create an authentication object with the username and authorities from the token claims.</li>
 *     <li>Set the authentication object in the SecurityContextHolder.</li>
 *     <li>Handle exceptions for expired, unsupported, or malformed tokens, throwing a BadCredentialsException if any issues arise.</li>
 *     <li>Continue with the next filter in the chain.</li>
 * </ul>
 *
 * <p>Code snippet for validating the JWT token:</p>
 * <pre>
 * {@code
 * String jwt = request.getHeader("Authorization");
 * if (jwt != null) {
 *     Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
 *     Claims claims = Jwts.parserBuilder()
 *         .setSigningKey(key)
 *         .build()
 *         .parseClaimsJws(jwt)
 *         .getBody();
 *     String username = claims.getSubject();
 *     List<GrantedAuthority> authorities = extractAuthorities(claims);
 *     Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
 *     SecurityContextHolder.getContext().setAuthentication(authentication);
 * }
 * chain.doFilter(request, response);
 * }
 * </pre>
 *
 * <p>The shouldNotFilter method ensures this filter is not executed during the login process:</p>
 * <pre>
 * {@code
 * @Override
 * protected boolean shouldNotFilter(HttpServletRequest request) {
 *     return request.getServletPath().equals("/user");
 * }
 * }
 * </pre>
 *
 * <p>To inject this filter into the Spring Security filter chain, use the addFilterBefore method:</p>
 * <pre>
 * {@code
 * http.addFilterBefore(new JWTTokenValidatorFilter(), BasicAuthenticationFilter.class);
 * }
 * </pre>
 */
public class JwtTokenValidatorFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException
    {
        // Retrieve the JWT token from the Authorization header
        String jwt = request.getHeader(SecurityConstant.JWT_HEADER);

        if (jwt != null) {
            try {
                // Generate the secret key
                SecretKey secretKey = Keys.hmacShaKeyFor(
                        SecurityConstant.JWT_KEY.getBytes(StandardCharsets.UTF_8));

                // Parse(Read) the JWT token
                Claims claims = Jwts.parser()
                        .verifyWith(secretKey)
                        .build()
                        .parseSignedClaims(jwt)
                        .getPayload();

                // Retrieve the username and authorities from the token claims
                String username = (String) claims.get("username");
                List<GrantedAuthority> authorities = extractAuthorities(claims);

                // Create the authentication object
                Authentication authentication =
                        new UsernamePasswordAuthenticationToken(username, null, authorities);

                // Set the authentication object in the SecurityContextHolder
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException e) {
                throw new BadCredentialsException("Invalid JWT token", e);
            }
        }

        // Continue with the next filter in the chain
        filterChain.doFilter(request, response);
    }

    /**
     * Extracts the authorities from the JWT claims.
     *
     * @param claims the JWT claims
     * @return a list of GrantedAuthority
     */
    private List<GrantedAuthority> extractAuthorities(Claims claims) {
        return Arrays.stream(claims.get("authorities").toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        // Skip this filter during the login process
        return request.getServletPath().equals("/user");
    }
}
