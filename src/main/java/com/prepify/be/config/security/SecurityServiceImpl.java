package com.prepify.be.config.security;

import com.prepify.be.auth.AuthUser;
import com.prepify.be.config.jwt.JWTPayload;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service("SecurityServiceImpl")
@Slf4j
public class SecurityServiceImpl implements ServerSecurityService {
    @Value("${spring.jwt.private.key}")
    private String privateJwtKey;

    @Override
    public UserDetails get(String token) {
        try {
            verify(token);
            JWTPayload payload = JWTPayload.parserFromJWT(token);
            assert payload != null;
            List<SimpleGrantedAuthority> authorities;
            List<String> roles = Objects.nonNull(payload.getRole()) ? List.of(payload.getRole()) : Collections.emptyList();
            authorities = roles
                    .stream()
                    .filter(StringUtils::isNotBlank)
                    .map(item -> "ROLE_" + item.toUpperCase())
                    .map(SimpleGrantedAuthority::new)
                    .toList();
            String username = payload.getUsername();
            AuthUser authUser = new AuthUser(username, token, authorities);
            authUser.setUsername(payload.getUsername());
            authUser.setId(payload.getId());
            authUser.setRole(payload.getRole());
            return authUser;
        } catch (Exception e) {
            throw new UsernameNotFoundException("UserDetails exception", e);
        }
    }

    private boolean verify(String token) {
        SignatureAlgorithm sa = SignatureAlgorithm.HS256;
        SecretKeySpec secretKeySpec = new SecretKeySpec(privateJwtKey.getBytes(), sa.getJcaName());
        Jwts.parser().setSigningKey(secretKeySpec).parseClaimsJws(token);
        return true;
    }

}
