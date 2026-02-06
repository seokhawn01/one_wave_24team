package com.gdg.workfit.controller;

import com.gdg.workfit.dto.TokenRequest;
import com.gdg.workfit.dto.TokenResponse;
import com.gdg.workfit.security.JwtTokenProvider;
import com.gdg.workfit.security.TokenType;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtTokenProvider tokenProvider;

    public AuthController(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("/enterprise/token")
    public TokenResponse issueEnterpriseToken(@Valid @RequestBody TokenRequest request) {
        String token = tokenProvider.createToken(request.getSubject(), TokenType.ENTERPRISE);
        return new TokenResponse(token, TokenType.ENTERPRISE.name());
    }

    @PostMapping("/user/token")
    public TokenResponse issueUserToken(@Valid @RequestBody TokenRequest request) {
        String token = tokenProvider.createToken(request.getSubject(), TokenType.USER);
        return new TokenResponse(token, TokenType.USER.name());
    }
}
