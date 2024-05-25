package sc.small.chat.global.security.service;import static java.util.Collections.emptyList;import io.jsonwebtoken.*;import io.jsonwebtoken.security.Keys;import jakarta.servlet.http.HttpServletRequest;import javax.crypto.SecretKey;import lombok.extern.slf4j.Slf4j;import org.springframework.beans.factory.annotation.Value;import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;import org.springframework.security.core.Authentication;import org.springframework.security.core.GrantedAuthority;import org.springframework.security.core.userdetails.User;import org.springframework.security.core.userdetails.UserDetails;import org.springframework.stereotype.Component;import java.util.Date;import java.util.stream.Collectors;import org.springframework.util.StringUtils;import sc.small.chat.global.dto.TokenInfoDTO;import sc.small.chat.oauth.util.OAuth2UserInfo;@Slf4j@Componentpublic class JwtTokenProvider {    private static final String AUTHORIZATION_HEADER = "Authorization";    private static final String BEARER_TYPE = "Bearer";    private static final long ACCESS_TOKEN_EXPIRE_TIME = 24 * 60 * 60 * 1000L;          // 1일    private static final long REFRESH_TOKEN_EXPIRE_TIME = 30 * 24 * 60 * 60 * 1000L;    // 30일    private static final int ACCESS_TOKEN_EXPIRE_TIME_COOKIE = 24 * 60 * 60;   // 쿠키도 마찬가지로 1일    private static final int REFRESH_TOKEN_EXPIRE_TIME_COOKIE = 30 * 24 * 60 * 60; // 쿠키도 마찬가지로 30일    private final SecretKey secretKey;    public JwtTokenProvider(@Value("${jwt.secret-key-base}") String secretKey) {        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes());    }//     유저 정보를 가지고 AccessToken, RefreshToken 을 생성하는 메서드    public TokenInfoDTO generateToken(Authentication authentication) {        // 권한 가져오기        String authorities = authentication.getAuthorities().stream()            .map(GrantedAuthority::getAuthority)            .collect(Collectors.joining(","));        long now = System.currentTimeMillis();        // Access Token 생성        String accessToken = Jwts.builder()            .subject(authentication.getName())            .expiration(new Date(now + ACCESS_TOKEN_EXPIRE_TIME))            .signWith(secretKey, Jwts.SIG.HS512)            .compact();        // Refresh Token 생성        String refreshToken = Jwts.builder()            .expiration(new Date(now + REFRESH_TOKEN_EXPIRE_TIME))            .signWith(secretKey, Jwts.SIG.HS512)            .compact();        return TokenInfoDTO.builder()            .accessToken(accessToken)            .refreshToken(refreshToken)            .refreshTokenExpirationTime(REFRESH_TOKEN_EXPIRE_TIME)            .build();    }    public TokenInfoDTO generateToken(OAuth2UserInfo userInfo) {        long now = System.currentTimeMillis();        // Access Token 생성        String accessToken = Jwts.builder()            .subject(userInfo.getProviderId())            .signWith(secretKey, Jwts.SIG.HS512)            .expiration(new Date(now + ACCESS_TOKEN_EXPIRE_TIME))            .compact();        // Refresh Token 생성        String refreshToken = Jwts.builder()            .subject(userInfo.getProviderId())            .expiration(new Date(now + REFRESH_TOKEN_EXPIRE_TIME))            .signWith(secretKey, Jwts.SIG.HS512)            .compact();        return TokenInfoDTO.builder()            .accessToken(accessToken)            .refreshToken(refreshToken)            .refreshTokenExpirationTime(REFRESH_TOKEN_EXPIRE_TIME)            .build();    }    // JWT 토큰을 복호화하여 토큰에 들어있는 정보를 꺼내는 메서드    public Authentication getAuthentication(String accessToken) {        // 토큰 복호화        Claims claims = parseClaims(accessToken);        if (claims.getSubject()== null) {            throw new RuntimeException("권한 정보가 없는 토큰입니다.");        }        // UserDetails 객체를 만들어서 Authentication 리턴        UserDetails principal = new User(claims.getSubject(), "", emptyList());        return new UsernamePasswordAuthenticationToken(principal, "", emptyList());    }    // Request Header 에서 토큰 정보 추출    public String resolveToken(HttpServletRequest request) {        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_TYPE)) {            return bearerToken.substring(7);        }        return null;    }    public Long getExpiration(String token) {        Claims claims = parseClaims(token);        Date expiration = claims.getExpiration();        return expiration.getTime() - new Date().getTime();    }    // 토큰 정보를 검증하는 메서드    public boolean validateToken(String token) {        try {            parseClaims(token);            return true;        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {            log.info("Invalid JWT Token", e);        } catch (ExpiredJwtException e) {            log.info("Expired JWT Token", e);        } catch (UnsupportedJwtException e) {            log.info("Unsupported JWT Token", e);        } catch (IllegalArgumentException e) {            log.info("JWT claims string is empty.", e);        }        return false;    }    private Claims parseClaims(String token) {        try {            return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();        } catch (ExpiredJwtException e) {            return e.getClaims();        }    }    public static int getRefreshTokenExpireTimeCookie() {        return REFRESH_TOKEN_EXPIRE_TIME_COOKIE;    }    public static int getAccessTokenExpireTimeCookie() {        return ACCESS_TOKEN_EXPIRE_TIME_COOKIE;    }//    public String generateRandomString() {//        SecureRandom secureRandom = new SecureRandom();//        byte[] bytes = new byte[16];//        secureRandom.nextBytes(bytes);////        StringBuilder sb = new StringBuilder();//        for (byte b : bytes) {//            sb.append(String.format("%02x", b));//        }//        return sb.toString();//    }}