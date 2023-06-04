package com.crewwork.structure.security.jwtauth;

import com.crewwork.domain.token.RefreshToken;
import com.crewwork.domain.token.RefreshTokenRepository;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JwtTokenProvider {
    private String secretKey = "mysecretkey";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";

//    private long accessTokenValidTime = 30 * 60 * 1000L; // 30분
//    private long refreshTokenValidTime = 14 * 24 * 60 * 60 * 1000L; // 2주

    private long accessTokenValidTime = 1 * 60 * 1000L; // 1분
    private long refreshTokenValidTime = 2 * 60 * 1000L; // 2분

    private final RefreshTokenRepository refreshTokenRepository;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public JwtToken createToken(String subject, Long memberId) {
        String accessToken = createAccessToken(subject, memberId);
        String refreshToken = createRefreshToken(subject, memberId);

        return new JwtToken(accessToken, refreshToken);
    }


    // 토큰에서 회원 정보 추출
    public Long getMemberIdFromToken(String token) {
        Object memberId = getClaims(token).get("memberId");
        if (memberId instanceof Integer) {
            return Long.valueOf((Integer) memberId);
        }
        else {
            return (Long) getClaims(token).get("memberId");
        }
    }

    public String getTokenIdFromRefreshToken(String refreshToken) {
        return (String) getClaims(refreshToken).get("tokenId");
    }
    public String reIssuanceAccessToken(String refreshToken) {
        RefreshToken findRefreshToken = refreshTokenRepository.findById(getTokenIdFromRefreshToken(refreshToken))
                .orElseThrow(() -> new JwtRuntimeException("refreshToken을 찾을 수 없습니다."));
        if(!findRefreshToken.getToken().equals(refreshToken)) {
            throw new JwtRuntimeException("refreshToken이 일치하지 않습니다.");
        }

        /*
        boolean hasChildren = refreshTokenRepository.existsByParentId(findRefreshToken.getId());
        // 이미 사용된 refreshToken인지 확인
        if(hasChildren){
            // refreshToken 전체 삭제 로직
            throw new JwtRuntimeException("이미 사용된 refreshToken 입니다.");
        }
        */

        return createAccessToken("Crew Work", getMemberIdFromToken(refreshToken));
    }

    @Transactional
    public void saveRefreshToken(String refreshToken) {
        RefreshToken refreshTokenEntity = new RefreshToken(
                getTokenIdFromRefreshToken(refreshToken),
                refreshToken,
                null);
        refreshTokenRepository.save(refreshTokenEntity);
    }

    @Transactional
    public void removeRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if(cookie.getName().equals("refreshToken")){
                String refreshToken = cookie.getValue();

                refreshTokenRepository.deleteById(getTokenIdFromRefreshToken(refreshToken));
                return;
            }
        }
    }

    public void verify(String token) {
        Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

    private Claims getClaims(String token) {
        try{
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
            // 토큰 유효성 확인
        } catch (SignatureException e) {    //잘못된 JWT 서명
            throw new JwtRuntimeException("Invalid JWT signature.");
        } catch (MalformedJwtException e) {     //유효하지 않은 구성의 JWT 토큰
            throw new JwtRuntimeException("Invalid JWT token.");
        } catch (ExpiredJwtException e) {       //만료된 JWT 토큰
            throw new JwtRuntimeException("Expired JWT token.");
        } catch (UnsupportedJwtException e) {       //지원되지 않는 형식이거나 구성의 JWT 토큰
            throw new JwtRuntimeException("Unsupported JWT token.");
        } catch (IllegalArgumentException e) {      //잘못된 JWT
            throw new JwtRuntimeException("JWT token compact of handler are invalid.");
        }
    }

    private String createAccessToken(String subject, Long memberId) {
        Date now = new Date();

        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessTokenValidTime))

                .claim("memberId", memberId)

                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    private String createRefreshToken(String subject, Long memberId) {
        Date now = new Date();

        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + refreshTokenValidTime))

                .claim("tokenId", UUID.randomUUID().toString())
                .claim("memberId", memberId)

                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
}
