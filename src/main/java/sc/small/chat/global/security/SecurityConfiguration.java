package sc.small.chat.global.security;import java.util.Arrays;import lombok.RequiredArgsConstructor;import org.springframework.context.annotation.Bean;import org.springframework.context.annotation.Configuration;import org.springframework.core.annotation.Order;import org.springframework.data.redis.core.RedisTemplate;import org.springframework.http.HttpHeaders;import org.springframework.security.config.Customizer;import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;import org.springframework.security.config.annotation.web.builders.HttpSecurity;import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;import org.springframework.security.config.annotation.web.configurers.RequestCacheConfigurer;import org.springframework.security.config.http.SessionCreationPolicy;import org.springframework.security.web.SecurityFilterChain;import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;import org.springframework.web.cors.CorsConfiguration;import org.springframework.web.cors.CorsConfigurationSource;import org.springframework.web.cors.UrlBasedCorsConfigurationSource;import sc.small.chat.oauth.handler.CustomLogoutSuccessHandler;import sc.small.chat.oauth.handler.OAuth2AuthenticationFailureHandler;import sc.small.chat.oauth.handler.OAuth2AuthenticationSuccessHandler;import sc.small.chat.oauth.repository.OAuth2AuthorizationRequestBasedOnCookieRepository;import sc.small.chat.global.security.filter.JwtAuthenticationFilter;import sc.small.chat.global.security.urls.SecurityUrls;import sc.small.chat.global.security.service.JwtTokenProvider;import sc.small.chat.oauth.service.CustomOAuth2UserService;@Configuration@EnableWebSecurity@EnableMethodSecurity@RequiredArgsConstructorpublic class SecurityConfiguration {    private final CustomOAuth2UserService oAuth2UserService;    private final SecurityUrls securityUrls;    private final JwtTokenProvider jwtTokenProvider;    private final RedisTemplate<String, String> redisTemplate;    /**     * 퍼블릭 경로 허용 설정 Bean 등록     * WebSecurityCustomizer.ignore() 처리가 권장되지 않는다고 warning이 출력되어 교체     * spring security 6.0 에서 HttpSecurity.requestMatcher(s) 제거됨     * @param http     * @return     * @throws Exception     */    @Bean    @Order(0)    public SecurityFilterChain allowPublicPathFilterChain(HttpSecurity http) throws Exception {        http            .securityMatcher(securityUrls.publicRequestMatcher()) // 특정 URL 패턴에 대해 보안 설정 적용            .authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll()) // 모든 요청을 허용            .csrf(AbstractHttpConfigurer::disable) // CSRF 비활성화            .cors(AbstractHttpConfigurer::disable) // CORS 비활성화            .requestCache(RequestCacheConfigurer::disable) // 요청 캐시 비활성화            .sessionManagement(AbstractHttpConfigurer::disable); // 세션 관리 비활성화        return http.build();    }    /**     * Token 인증 필터 체인 설정     * public 을 제외한 모든 url 에 해당     */    @Bean    @Order(1)    public SecurityFilterChain httpBasicSecurityFilterChain(HttpSecurity http) throws Exception {        getAuthenticationHttpSecurity(http)            .oauth2Login(oauth2Login -> oauth2Login                .authorizationEndpoint(authorizationEndpoint -> authorizationEndpoint                    .baseUri("/oauth2/authorization")                    .authorizationRequestRepository(oAuth2AuthorizationRequestBasedOnCookieRepository())                )                .redirectionEndpoint(redirectionEndpoint -> redirectionEndpoint                    .baseUri("/*/oauth2/code/*")                )                .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint                    .userService(oAuth2UserService)                )                .successHandler(oAuth2AuthenticationSuccessHandler())                .failureHandler(oAuth2AuthenticationFailureHandler())            )            .logout(logout ->                logout                    .logoutUrl("/oauth2/logout")                    .invalidateHttpSession(true)                    .clearAuthentication(true)                    .logoutSuccessHandler(logoutSuccessHandler())            );        http.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, redisTemplate), UsernamePasswordAuthenticationFilter.class);        return http.build();    }    public HttpSecurity getAuthenticationHttpSecurity(HttpSecurity http) throws Exception {        http            .anonymous(AbstractHttpConfigurer::disable) // 익명 유저 비허용            .csrf(AbstractHttpConfigurer::disable) // 필요시 구현 필요            .cors(Customizer.withDefaults()) // 공통 설정 사용            .requestCache(RequestCacheConfigurer::disable) // cache disable            .sessionManagement(sessionManagement ->                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 토큰기반 -> 세션 Stateless            );        return http;    }    /**     * 쿠키 기반 인가 Repository     * 인가 응답을 연계 하고 검증할 때 사용.     * */    @Bean    public OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository() {        return new OAuth2AuthorizationRequestBasedOnCookieRepository();    }    /**     * Oauth 인증 성공 핸들러     * */    @Bean    public OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler() {        return new OAuth2AuthenticationSuccessHandler(            oAuth2AuthorizationRequestBasedOnCookieRepository(),            jwtTokenProvider,            redisTemplate);    }    /**     * Oauth 인증 실패 핸들러     * */    @Bean    public OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler() {        return new OAuth2AuthenticationFailureHandler(oAuth2AuthorizationRequestBasedOnCookieRepository());    }    /**     * 로그아웃 성공 핸들러     */    @Bean    public LogoutSuccessHandler logoutSuccessHandler() {        return new CustomLogoutSuccessHandler(oAuth2AuthorizationRequestBasedOnCookieRepository());    }    @Bean    public CorsConfigurationSource corsConfigurationSource() {        CorsConfiguration configuration = new CorsConfiguration();//        configuration.setAllowedOrigins(SecurityUrls.CORS_URLS); // 허용 urls        configuration.addAllowedOriginPattern("*"); // 허용 urls        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE")); // 허용 method        configuration.setAllowedHeaders(Arrays.asList(HttpHeaders.AUTHORIZATION, HttpHeaders.CONTENT_TYPE)); // 허용 헤더//        configuration.addAllowedHeader("*");        configuration.setAllowCredentials(true);        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();        source.registerCorsConfiguration("/**", configuration);        return source;    }}