package com.conceptandcoding.eazybankrestapi.filter;

import com.conceptandcoding.eazybankrestapi.constant.SecurityConstant;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * This class is responsible for generating a JWT token after a successful login.
 *
 * <p>After the end user enters their credentials and logs into the web application,
 * a JWT token is generated upon successful authentication.</p>
 *
 * <p>The filter is executed only once per request by extending the OncePerRequestFilter
 * from the Spring framework. The method doFilterInternal is overridden to implement
 * the logic for generating the JWT token.</p>
 *
 * <p>The steps to generate the JWT token are as follows:</p>
 * <ul>
 *     <li>Retrieve the current authenticated user details from the SecurityContext.</li>
 *     <li>Generate a secret key based on a constant value defined in a SecurityConstants interface.</li>
 *     <li>Create the JWT token using the JWTS.builder, setting issuer, subject, claims (username and authorities), issue date, and expiration date.</li>
 *     <li>Sign the JWT token with the generated key and set it in the response header.</li>
 *     <li>Ensure the filter is executed only during the login process using the shouldNotFilter method.</li>
 * </ul>
 *
 * <p>Code snippet for generating the JWT token:</p>
 * <pre>
 * {@code
 * Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
 * if (authentication != null) {
 *     Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
 *     String jwtToken = Jwts.builder()
 *         .setIssuer("Eazy Bank")
 *         .setSubject("JWT Token")
 *         .claim("username", authentication.getName())
 *         .claim("authorities", populateAuthorities(authentication.getAuthorities()))
 *         .setIssuedAt(new Date())
 *         .setExpiration(new Date(System.currentTimeMillis() + 28800000)) // 8 hours
 *         .signWith(key)
 *         .compact();
 *     response.setHeader("Authorization", jwtToken);
 * }
 * chain.doFilter(request, response);
 * }
 * </pre>
 *
 * <p>The shouldNotFilter method ensures this filter is executed only during the login process:</p>
 * <pre>
 * {@code
 * @Override
 * protected boolean shouldNotFilter(HttpServletRequest request) {
 *     return !request.getServletPath().equals("/user");
 * }
 * }
 * </pre>
 *
 * <p>To inject this filter into the Spring Security filter chain, use the addFilterAfter method:</p>
 * <pre>
 * {@code
 * http.addFilterAfter(new JWTTokenGeneratorFilter(), BasicAuthenticationFilter.class);
 * }
 * </pre>
 */

public class JwtTokenGeneratorFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException
    {

        // Retrieve the current authenticated user details
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            // Generate a secret key
            Key secretKey = Keys.hmacShaKeyFor(SecurityConstant.JWT_KEY.getBytes(StandardCharsets.UTF_8));

            // Create the JWT token using user-details(claims), key, etc..
            String jwt = Jwts.builder()
                    .issuer("Eazy Bank")
                    .subject("JWT Token")
                    .claim("username", authentication.getName())
                    .claim("authorites", populateAuthorities(authentication.getAuthorities()))
                    .issuedAt(new Date())
                    .expiration(new Date(new Date().getTime() + 28800000)) // 8 hours
                    .signWith(secretKey)
                    .compact();

            // Set the JWT token in the response header
            response.setHeader(SecurityConstant.JWT_HEADER, jwt);
        }

        // Continue with the next filter in the chain
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        // Execute this filter only during the login process
        return !request.getServletPath().equals("/user");
    }

    private String populateAuthorities(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(", "));
    }
}
