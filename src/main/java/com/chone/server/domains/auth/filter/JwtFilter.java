package com.chone.server.domains.auth.filter;

import com.chone.server.commons.exception.ApiBusinessException;
import com.chone.server.commons.jwt.JwtUtil;
import com.chone.server.domains.auth.exception.AuthExceptionCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j(topic = "JWT 검증 및 인가")
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public JwtFilter(JwtUtil jwtUtil, UserDetailsService customUserDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 스웨거 경로를 확인하고 필터 적용을 건너뛰기
        if (request.getRequestURI().startsWith("/api/swagger-ui") ||
                request.getRequestURI().startsWith("/api/v3/api-docs")) {
            filterChain.doFilter(request, response);
            return;
        }

        //request에서 Authorization 헤더를 찾음
        String tokenValue = jwtUtil.getJwtFromHeader(request);

        if (StringUtils.hasText(tokenValue)) {
            try{
                // 토큰 검증 (여기서 만료된 토큰이면 ExpiredJwtException 발생)
                if (!jwtUtil.validateToken(tokenValue)) {
                    throw new ApiBusinessException(AuthExceptionCode.INVALID_TOKEN);
                }

                // 토큰에서 사용자 정보 추출
                Claims info = jwtUtil.getUserInfoFromToken(tokenValue);
                setAuthentication(info.getSubject());
            }catch (ExpiredJwtException e) {
                // 만료된 토큰 예외 처리
                handleException(response, "TOKEN_EXPIRED", "만료된 토큰입니다.");
                return;
            } catch (JwtException e) {
                // 유효하지 않은 토큰 예외 처리
                handleException(response, "INVALID_TOKEN", "유효하지 않은 토큰입니다.");
                return;
            } catch (Exception e) {
                // 기타 인증 관련 예외 처리
                handleException(response, "AUTH_ERROR", "인증 과정에서 오류가 발생했습니다.");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    // 클라이언트 응답 처리
    private void handleException(HttpServletResponse response, String errorCode, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String jsonResponse = String.format("{\"error\": \"%s\", \"message\": \"%s\"}", errorCode, message);
        response.getWriter().write(jsonResponse);
    }

    // 인증 객체 생성
    private Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    // 인증 처리
    public void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(username);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }
}
