package com.toongather.toongather.global.security.jwt;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Component
public class JwtTokenProvider {

    private String secretKey = "webtoonsecret";

    //토큰시간 (default 30분)
    private Long tokenValidTime = 30 * 60 * 1000L;

    private final UserDetailsService userDetailsService;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    //JWT 토큰 생성
    public String createToken(Long userPk, List<String> roles) {

        //payload에 저장되는 정보단위, user 식별값
        Claims claims = Jwts.claims().setSubject(String.valueOf(userPk));
        claims.put("roles", roles);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValidTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

    }

    //토큰에서 인증정보를 조회
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPk(token));
        return  new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    //토큰에서 회원정보 추출
    public String getUserPk(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    //request header에서 토큰값 가져옴 "Authoriztion" : "token값"
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }

    //토큰 유효성 + 만료일자 확인
    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        }catch (MalformedJwtException e) {
            log.warn("잘못된 jwt 서명");
            return false;
        }catch (ExpiredJwtException e) {
            log.warn("만료된 jwt 토큰");
            return false;
        }catch (UnsupportedJwtException e) {
            log.warn("지원되지 않는 jwt 토큰");
            return false;
        }catch (IllegalArgumentException e) {
            log.warn("잘못된 토큰");
            return false;
        }
    }



}
