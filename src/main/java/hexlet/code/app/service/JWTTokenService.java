package hexlet.code.app.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Clock;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.compression.GzipCompressionCodec;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Service
public class JWTTokenService implements TokenService, Clock {

    private SecretKey secretKey;
    private String issuer;
    private Long expSec;
    private Long clockSkewSec;

    public JWTTokenService() {
        this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        this.issuer = "issuer";
        this.expSec = 10000L;
        this.clockSkewSec = 500L;
    }

    @Override
    public String getToken(Map<String, Object> attribs) {
        return Jwts.builder()
                .signWith(secretKey)
                .compressWith(new GzipCompressionCodec()) // ???
                .setClaims(createClaims(attribs, expSec))
                .compact();
    }

    private Claims createClaims(Map<String, Object> attribs, Long expSec) {
        Claims claims = Jwts.claims();
        claims.setIssuer(issuer);
        claims.setIssuedAt(now());
        claims.putAll(attribs);
        if (expSec > 0) {
            claims.setExpiration(new Date(System.currentTimeMillis() + expSec * 1000));
        }

        return claims;
    }

    @Override
    public Map<String, Object> parse(String token) {
        return Jwts.parserBuilder()
                .requireIssuer(issuer)
                .setClock(this)
                .setAllowedClockSkewSeconds(clockSkewSec)
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    @Override
    public Date now() {
        return new Date();
    }
}
