package com.conceptandcoding.eazybankrestapi.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Base64;

public class RequestValidationBeforeFilter implements Filter {

    /**
     * This method processes an HTTP request and response, performing the following steps:
     * <ul>
     *     <li>Typecasts the request and response to HttpServletRequest and HttpServletResponse.</li>
     *     <li>Extracts the authorization header sent by the Angular UI application.</li>
     *     <li>Trims any spaces from the authorization header value.</li>
     *     <li>Extracts the Base64-encoded email and password from the authorization header.</li>
     *     <li>Decodes the Base64 token and splits it using a colon delimiter to separate the email and password.</li>
     *     <li>Validates the email to check if it contains the string "test".</li>
     *     <li>If the email contains "test", returns a 400 Bad Request status.</li>
     *     <li>If the email is valid, invokes the next filter in the chain using the provided chain object.</li>
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

        // Typecast request and response
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Get authorization header
        // ex: authorization: Basic aGFwcHlAZXhhbXBsZS5jb206MTIzNDU=
        String authHeader = httpRequest.getHeader("Authorization");

        // Trim spaces from the authorization header
        if (authHeader != null) {
            authHeader = authHeader.trim();

            // Extract Base64 token from the authorization header
            if (authHeader.startsWith("Basic ")) {
                String base64Token = authHeader.substring(6);  // Skip "Basic " part

                // Decode the Base64 token
                String decodedToken = new String(Base64.getDecoder().decode(base64Token));
                String[] credentials = decodedToken.split(":");  // Split email and password

                // Validate the email
                if (credentials.length > 0 && credentials[0].contains("test")) {
                    httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
            }
        }

        // Continue with the next filter in the chain
        chain.doFilter(request, response);
    }
}
