package com.cjrequena.security.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.cjrequena.security.configuration.security.BasicAuthUserDetails;
import com.cjrequena.security.controller.dto.AuthAccessTokenDTO;
import com.cjrequena.security.shared.common.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthAccessTokenService {

  private final JwtUtil jwtUtil;

  public AuthAccessTokenDTO generateAccessToken(BasicAuthUserDetails basicAuthUserDetails) {
    String userName = basicAuthUserDetails.getUsername();
    Map<String, Object> claims = new HashMap<>();
    claims.put(jwtUtil.CLAIM_USER_ID, basicAuthUserDetails.getUserId());
    claims.put(jwtUtil.CLAIM_USER_NAME, basicAuthUserDetails.getUsername());
    claims.put(jwtUtil.CLAIM_EMAIL, basicAuthUserDetails.getEmail());
    claims.put(jwtUtil.CLAIM_ROLES, basicAuthUserDetails.getRoles().stream().toList());
    claims.put(jwtUtil.CLAIM_AUTHORITIES, basicAuthUserDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList());
    DecodedJWT decodedJWT = jwtUtil.decode(jwtUtil.create(userName, claims));

    AuthAccessTokenDTO authAccessTokenDTO = new AuthAccessTokenDTO();
    authAccessTokenDTO.setTokenType("Bearer");
    authAccessTokenDTO.setClientId(decodedJWT.getSubject());
    authAccessTokenDTO.setAccessToken(decodedJWT.getToken());
    authAccessTokenDTO.setIssuedAt(decodedJWT.getIssuedAt().getTime());
    authAccessTokenDTO.setExpiresAt(decodedJWT.getExpiresAt().getTime());
    return authAccessTokenDTO;
  }
}
