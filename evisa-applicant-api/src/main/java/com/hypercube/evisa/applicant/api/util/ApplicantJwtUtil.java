package com.hypercube.evisa.applicant.api.util;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.security.Key;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * @author sivasreenivas
 *
 */
@Service
public class ApplicantJwtUtil {

    /**
     * 
     */
    private String SECRET_KEY = "Djib#Visa@hYperC&bE@$$##653421";
    private static final long ACCESS_TOKEN_EXPIRATION = 1000 * 60 * 60 * 10;
    // private static final long ACCESS_TOKEN_EXPIRATION = 1000 * 60 * 15;
    private static final long REFRESH_TOKEN_EXPIRATION = 1000 * 60 * 60 * 24;

    /**
     * @param token
     * @return
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * @param token
     * @return
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * @param token
     * @param claimsResolver
     * @return
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }


    // private Key getSigningKey() {
    //     return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    // }


    /**
     * @param token
     * @return
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    /**
     * @param token
     * @return
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * @param userName
     * @return
     */
    public String generateToken(String userName) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userName);
    }


    // public String generateToken(String userName) {
    //     return createToken(new HashMap<>(), userName, ACCESS_TOKEN_EXPIRATION);
    // }

    // public String generateRefreshToken(String userName) {
    //     return createToken(new HashMap<>(), userName, REFRESH_TOKEN_EXPIRATION);
    // }



    /**
     * @param claims
     * @param subject
     * @return
     */
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }

    //     /**
    //  * @param claims
    //  * @param subject
    //  * @return
    //  */
    // private String createToken(Map<String, Object> claims, String subject, long expirationTime) {
    //     return Jwts.builder()
    //             .setClaims(claims)
    //             .setSubject(subject)
    //             .setIssuedAt(new Date(System.currentTimeMillis()))
    //             .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
    //             .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
    //             .compact();
    // }

    /**
     * @param token
     * @param userDetails
     * @return
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}