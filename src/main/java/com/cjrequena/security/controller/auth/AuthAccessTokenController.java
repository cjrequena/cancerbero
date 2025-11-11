package com.cjrequena.security.controller.auth;

import com.cjrequena.security.configuration.security.BasicAuthUserDetails;
import com.cjrequena.security.controller.dto.AuthAccessTokenDTO;
import com.cjrequena.security.service.AuthAccessTokenService;
import com.cjrequena.security.shared.common.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.cjrequena.security.shared.common.Constant.HEADER_VND_SECURITY_AUTHORIZATION_SERVICE_V1;
import static org.springframework.http.HttpHeaders.CACHE_CONTROL;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping(value = AuthAccessTokenController.ENDPOINT, headers = {AuthAccessTokenController.ACCEPT_VERSION})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthAccessTokenController {

  public static final String ENDPOINT = "/api/auth/token";
  public static final String ACCEPT_VERSION = "Accept-Version=" + HEADER_VND_SECURITY_AUTHORIZATION_SERVICE_V1;

  private final AuthAccessTokenService authAccessTokenService;

  @PostMapping(
    //path = "/auth/token",
    produces = {APPLICATION_JSON_VALUE}
  )
  public ResponseEntity<AuthAccessTokenDTO> accessToken(@AuthenticationPrincipal BasicAuthUserDetails basicAuthUserDetails) {
    HttpHeaders responseHeaders = new HttpHeaders();
    responseHeaders.set(CACHE_CONTROL, "no store, private, max-age=0");
    AuthAccessTokenDTO authAccessTokenDTO = authAccessTokenService.generateAccessToken(basicAuthUserDetails);
    return new ResponseEntity<>(authAccessTokenDTO, responseHeaders, HttpStatus.OK);
  }

  @PostMapping(
    path = "/verify",
    produces = {APPLICATION_JSON_VALUE}
  )
  public ResponseEntity<Boolean> accessToken(@RequestBody AuthAccessTokenDTO authAccessTokenDTO) {
    HttpHeaders responseHeaders = new HttpHeaders();
    responseHeaders.set(CACHE_CONTROL, "no store, private, max-age=0");
    return new ResponseEntity<>(JwtUtil.verify(authAccessTokenDTO.getAccessToken()), responseHeaders, HttpStatus.OK);
  }
}
