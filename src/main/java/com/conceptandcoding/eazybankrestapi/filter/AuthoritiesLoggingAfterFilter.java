package com.conceptandcoding.eazybankrestapi.filter;

import jakarta.servlet.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.Collection;
import java.util.logging.Logger;

public class AuthoritiesLoggingAfterFilter implements Filter {

    private final Logger LOGGER = Logger.getLogger(AuthoritiesLoggingAfterFilter.class.getName());

    /**
     * This method processes the request and response, logging details of the authenticated user just after authentication is completed.
     *
     * <p>For scenarios where business logic needs to be executed immediately after authentication,
     * we can configure a custom authentication filter to run just after the BasicAuthenticationFilter.</p>
     *
     * <p>In this custom authentication filter, the following steps are performed:</p>
     * <ul>
     *     <li>Retrieve the current authenticated user details from the SecurityContext.</li>
     *     <li>Check if the authentication object is not null, indicating successful authentication.</li>
     *     <li>Log the authenticated user's name and authorities.</li>
     *     <li>Additional actions like logging, auditing, or sending an email to the user about successful authentication can be included.</li>
     * </ul>
     *
     * @param request  the HttpServletRequest object that contains the request the client has made of the servlet
     * @param response the HttpServletResponse object that contains the response the servlet sends to the client
     * @param chain    the FilterChain object provided by the servlet container to the developer
     */

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain) throws IOException, ServletException
    {
        // Retrieve the current authenticated user details
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            // Log the authenticated user's name and authorities
            String username = authentication.getName();
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

            LOGGER.info("Authenticated user: " + username + ", Authorities: " + authorities);

            // Additional actions like logging, auditing, or sending an email can be performed here
            // Example: auditService.logUserLogin(username);
            // Example: emailService.sendAuthenticationSuccessEmail(username);
        }
        chain.doFilter(request,response);
    }
}
