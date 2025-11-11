package com.cjrequena.security.shared.common.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.cjrequena.security.configuration.security.JWTConfigurationProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;

@Component
@RequiredArgsConstructor
public final class JwtUtil {

  public static final String CLAIM_USER_ID = "user_id";
  public static final String CLAIM_USER_NAME = "user_name";
  public static final String CLAIM_EMAIL = "email";
  public static final String CLAIM_ROLES = "roles";
  public static final String CLAIM_AUTHORITIES = "authorities";

  private final JWTConfigurationProperties jwtConfigurationProperties;

  public String create(String userName, Map<String, Object> claims) {
    Instant now = Instant.now();
    return JWT
      .create()
      .withSubject(userName)
      .withIssuedAt(now)
      .withExpiresAt(now.plus(jwtConfigurationProperties.getTokenDuration()))
      .withPayload(claims)
      .sign(Algorithm.HMAC256(jwtConfigurationProperties.getSecretKey()));
  }

  public DecodedJWT decode(String token) {
    Algorithm algorithm = Algorithm.HMAC256(jwtConfigurationProperties.getSecretKey());
    return JWT
      .require(algorithm)
      .build()
      .verify(token);
  }

  public boolean verify(String token) {
    try {
      this.decode(token);
      return true;
    } catch (JWTVerificationException exception) {
      // Invalid signature or claims
      return false; // Token is invalid
    }
  }

}
