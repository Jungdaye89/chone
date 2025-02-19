package com.chone.server.domains.auth.filter;

import com.chone.server.commons.jwt.JwtUtil;
import com.chone.server.domains.auth.dto.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public JwtFilter(JwtUtil jwtUtil, UserDetailsService customUserDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //request에서 Authorization 헤더를 찾음
        String authorization = request.getHeader("Authorization");

        String requestURI = request.getRequestURI();
        // 회원가입 요청은 필터를 건너뛴다.
        if (requestURI.equals("/api/v1/users/signup") || requestURI.equals("/api/v1/users/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        //Authorization 헤더 검증
        if(authorization == null || !authorization.startsWith("Bearer ")) {
            log.info("토큰이 비어있습니다.");
            filterChain.doFilter(request, response);
            return;
        }

        //Bearer 부분 제거 후 순수 토큰만 획득
        //공백으로 나누어 리스트로 한다음 1번 인덱스 값을 token에 저장하면 Bearer를 제거한 값만 남게됨
        String token = authorization.split(" ")[1];

        //토큰 소멸 시간 검증
        if(jwtUtil.isExpired(token)){
            log.info("토큰이 만료되었습니다.");
            filterChain.doFilter(request, response);

            return;
        }

        //토큰에서 username과 role 획득
        String username = jwtUtil.getUsername(token);

        //UserDetails에 회원 정보 객체 담기
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(username);

        //스프링 시큐리티 인증 토큰 생성
        //customeUserDetails를 담고 있는 token
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        //세션에 사용자 등록
        //세션에 사용자를 등록했기 때문에 특정한 경로에 접근을 할 수 있도록 한다.
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
