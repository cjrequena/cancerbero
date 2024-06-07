package com.cjrequena.security.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.cjrequena.security.configuration.security.BasicAuthUserDetails;
import com.cjrequena.security.configuration.security.JWTComponent;
import com.cjrequena.security.model.dto.AuthAccessTokenDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthAccessTokenService {

  private final JWTComponent jwtComponent;

  public AuthAccessTokenDTO generateAccessToken(BasicAuthUserDetails basicAuthUserDetails) {
    String userName = basicAuthUserDetails.getUsername();
    Map<String, Object> claims = new HashMap<>();
    claims.put(jwtComponent.CLAIM_USER_ID, basicAuthUserDetails.getUserId());
    claims.put(jwtComponent.CLAIM_USER_NAME, basicAuthUserDetails.getUsername());
    claims.put(jwtComponent.CLAIM_EMAIL, basicAuthUserDetails.getEmail());
    claims.put(jwtComponent.CLAIM_ROLES, basicAuthUserDetails.getRoles().stream().toList());
    claims.put(jwtComponent.CLAIM_AUTHORITIES, basicAuthUserDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList());
    DecodedJWT decodedJWT = jwtComponent.decode(jwtComponent.create(userName, claims));

    AuthAccessTokenDTO authAccessTokenDTO = new AuthAccessTokenDTO();
    authAccessTokenDTO.setTokenType("Bearer");
    authAccessTokenDTO.setClientId(decodedJWT.getSubject());
    authAccessTokenDTO.setAccessToken(decodedJWT.getToken());
    authAccessTokenDTO.setIssuedAt(decodedJWT.getIssuedAt().getTime());
    authAccessTokenDTO.setExpiresAt(decodedJWT.getExpiresAt().getTime());
    return authAccessTokenDTO;
  }
}
