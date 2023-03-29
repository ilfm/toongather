package com.toongather.toongather.global.security.jwt;

import com.toongather.toongather.domain.member.domain.MemberRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
@Component
public class JwtTokenProvider {

    private String secretKey = "webtoonsecret";

    //토큰시간 (default 30분)
    private Long tokenValidTime = 30 * 60 * 1000L;
    //리프레시 토큰(default 2주)
    private Long refreshValidTime =  14 * 24 * 60 * 60 * 1000l;

    private final UserDetailsService userDetailsService;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    //JWT 토큰 생성
    public String createToken(Long userPk, List<MemberRole> roles) {

        //권한 리스트 추출
        List<String> memberRoles = roles.stream()
            .map(target -> target.getRole().getName().name())
            .collect(Collectors.toList());

        //payload에 저장되는 정보단위, user 식별값
        Claims claims = Jwts.claims().setSubject(String.valueOf(userPk));
        claims.put("roles", memberRoles);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValidTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String createRefreshToken(Long userPk) {
        Claims claims = Jwts.claims().setSubject(String.valueOf(userPk));
        Date now = new Date();

        return Jwts.builder()
          .setClaims(claims)
          .setIssuedAt(now)
          .setExpiration(new Date(now.getTime() + refreshValidTime))
          .signWith(SignatureAlgorithm.HS256, secretKey)
          .compact();
    }

    //토큰에서 인증정보를 조회
    @Transactional
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPk(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    //토큰에서 회원정보 추출
    public String getUserPk(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    //request header에서 토큰값 가져옴 "Authoriztion" : "token값"
    public JwtToken resolveToken(HttpServletRequest request) {
        String accessToken = request.getHeader("Authorization");
        String refreshToken = request.getHeader("X-RT_TOKEN");

        if(accessToken != null) accessToken = accessToken.substring(7);
        if(refreshToken != null) refreshToken = refreshToken.substring(7);

        return  JwtToken.builder()
          .accessToken(accessToken)
          .refreshToken(refreshToken).build();
    }

    //토큰 유효성 + 만료일자 확인
    public JwtCode validateToken(String jwtToken) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return JwtCode.ACCESS;
        }catch (MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e ) {
            log.warn(JwtCode.DENIED.getMessage());
            return JwtCode.DENIED;
        }catch (ExpiredJwtException e) {
            log.warn(JwtCode.EXPIRED.getMessage());
            return JwtCode.EXPIRED;
        }
    }



}
