package com.bekassyl.todoapp.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;

@Component
public class JWTUtil {
    @Value("${jwt-secret}")
    private String jwtSecret;

    public String generateToken(String username) {
        Date expirationDate = Date.from(ZonedDateTime.now().plusMinutes(60).toInstant());

        return JWT.create()
                .withSubject("AppUser details")
                .withClaim("username", username)
                .withIssuedAt(new Date())
                .withIssuer("bekassyl")
                .withExpiresAt(expirationDate)
                .sign(Algorithm.HMAC256(jwtSecret));
    }

    public String validateTokenAndRetrieveClaim(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(jwtSecret))
                .withSubject("AppUser details")
                .withIssuer("bekassyl")
                .build();

        DecodedJWT jwt = verifier.verify(token); // throws Exception

        return jwt.getClaim("username").asString();
    }
}
