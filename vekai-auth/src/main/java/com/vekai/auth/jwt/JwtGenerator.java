package com.vekai.auth.jwt;


import com.vekai.auth.autoconfigure.AuthProperties;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
// todo jwt expire
@Component
@ConditionalOnProperty(prefix = "com.vekai.auth", name = "enableJwtReplaceSession", havingValue = "true")
public class JwtGenerator {
    @Autowired
    private AuthProperties authProperties;

    public String generate(String id, long ttlMillis, String subject) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(authProperties.getJwtSecretKey());
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        JwtBuilder builder =
                Jwts.builder().setId(id).setIssuedAt(now).setSubject(subject).setIssuer(authProperties.getJwtIssuer())
                        .signWith(signatureAlgorithm, signingKey);

        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }
        return builder.compact();
    }
}
