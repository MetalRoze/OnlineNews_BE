package com.example.onlinenews.jwt.provider;

import com.example.onlinenews.jwt.dto.JwtToken;
import com.example.onlinenews.jwt.service.JpaUserDetailService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtTokenProvider {
    private static final String BEARER_PREFIX = "Bearer ";

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.accessExpiration}")
    private long accessExpiration;

    @Value("${jwt.refreshExpiration}")
    private long refreshExpiration;

    private Key key;

    private final JpaUserDetailService userDetailService;

    public JwtTokenProvider(JpaUserDetailService userDetailService) {
        this.userDetailService = userDetailService;
    }

    @PostConstruct
    protected void init() {
        key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(String account) {
        Claims claims = Jwts.claims().setSubject(account);

        UserDetails userDetails = userDetailService.loadUserByUsername(account);
        claims.put("role", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList());

        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessExpiration))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(String account) {
        Claims claims = Jwts.claims().setSubject(account);

        UserDetails userDetails = userDetailService.loadUserByUsername(account);
        claims.put("role", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList());

        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + refreshExpiration))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }


    /**
     * JWT 토큰에서 권한 인증하는 메소드
     *
     * @param token
     * @return 인증 정보
     */

    public Authentication getAuthentication(String token) {
        String account = this.getAccount(token);
        UserDetails userDetails = userDetailService.loadUserByUsername(account);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    /**
     * JWT 토큰에서 email 가져오는 메소드
     *
     * @param token
     * @return email
     */
    public String getAccount(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * Http header에서 JWT 토큰을 가져오는 메소드
     *
     * @param request
     * @return JWT 토큰
     */
    public String resolveToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith(BEARER_PREFIX)) {
            return token.substring(BEARER_PREFIX.length()).trim(); // "Bearer " 부분 제거
        }
        return null;
    }

    /**
     * JWT 유효성 검사 메소드
     *
     * @param token
     * @return 유효성 유무
     */
    public boolean validateToken(String token) {
        try {
            if (token.startsWith(BEARER_PREFIX)) {
                token = token.substring(BEARER_PREFIX.length()).trim();
            }

            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            return !claims.getBody().getExpiration().before(new Date());

        } catch (Exception e) {
            log.error("Invalid JWT token: {}", token, e);
            return false;
        }
    }

    /**
     * JWT 토큰 남은 만료 시간
     *
     * @param accessToken
     * @return 남은 시간
     */
    public long getExpiration(String accessToken) {
        Date expriration = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(accessToken)
                .getBody()
                .getExpiration();

        long now = new Date().getTime();
        return expriration.getTime() - now;
    }

    /**
     * RefreshToken으로 accessToken 재발급 하는 메소드
     *
     * @param refreshToken
     * @return
     */
    public JwtToken reissueToken(String refreshToken) {
        String email = getAccount(refreshToken);

        JwtToken jwtDto = JwtToken.builder()
                .accessToken(generateAccessToken(email))
                .refreshToken(generateRefreshToken(email))
                .grantType("Bearer")
                .build();

        return jwtDto;
    }
}
