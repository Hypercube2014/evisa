package com.hypercube.evisa.applicant.api.configuration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hypercube.evisa.applicant.api.util.ApplicantJwtUtil;
import com.hypercube.evisa.applicant.api.util.ApplicantUserDetailsService;
import com.hypercube.evisa.common.api.constants.CommonsConstants;

import io.jsonwebtoken.MalformedJwtException;
import lombok.Getter;
import lombok.Setter;

/**
 * @author SivaSreenivas
 *
 */
@Component
@Getter
@Setter
public class JwtRequestFilter extends OncePerRequestFilter {
    /**
     *
     */
    @Autowired(required = true)
    private ApplicantUserDetailsService userDetailsService;
    /**
     *
     */
    @Autowired(required = true)
    private ApplicantJwtUtil jwtUtil;
    /**
     *
     */
    // @Override
    // protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
    //         throws ServletException, IOException {
    //     final String authorizationHeader = request.getHeader(CommonsConstants.AUTHORIZATION);
    //     System.out.println("authorization service " + authorizationHeader);
    //     String username = null;
    //     String jwt = null;


    //     System.out.println("authorization => " + authorizationHeader);

    //     if (authorizationHeader == null || authorizationHeader.isEmpty() || !authorizationHeader.startsWith("Bearer ")) {
    //         chain.doFilter(request, response);
    //         return;
    //     }

    //     if (authorizationHeader != null && authorizationHeader.startsWith(CommonsConstants.BEARER)) {
    //         jwt = authorizationHeader.substring(7);
    //         System.out.println("jwt token =" + jwt);
    //         username = jwtUtil.extractUsername(jwt);
    //     }

    //     if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

    //         UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

    //         if (jwtUtil.validateToken(jwt, userDetails)) {

    //             UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
    //                         userDetails, null, userDetails.getAuthorities());
    //                 usernamePasswordAuthenticationToken
    //                         .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    //                 SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

    //         }
    //     }
    //     chain.doFilter(request, response);
    // }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader(CommonsConstants.AUTHORIZATION);
        LOGGER.debug("Authorization header received");

        String username = null;
        String jwt = null;

        if (authorizationHeader == null || authorizationHeader.isEmpty() || !authorizationHeader.startsWith(CommonsConstants.BEARER)) {
            LOGGER.trace("No Bearer token, skipping JWT validation");
            chain.doFilter(request, response);
            return;
        }
        jwt = authorizationHeader.substring(CommonsConstants.BEARER.length()).trim();

        if (jwt.isEmpty()) {
            LOGGER.trace("Empty JWT token");
            chain.doFilter(request, response);
            return;
        }

        if (jwt.split("\\.").length != 3) {
            LOGGER.debug("Invalid JWT token format");
            chain.doFilter(request, response);
            return;
        }

        try {
            username = jwtUtil.extractUsername(jwt);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

                if (jwtUtil.validateToken(jwt, userDetails)) {

                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }  catch (Exception e) {
            LOGGER.warn("JWT validation error", e);
            SecurityContextHolder.clearContext();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized");
            return;
        }

        chain.doFilter(request, response);
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtRequestFilter.class);
}

