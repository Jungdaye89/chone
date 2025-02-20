package com.chone.server.domains.auth.filter;

import com.chone.server.commons.exception.ApiBusinessException;
import com.chone.server.commons.jwt.JwtUtil;
import com.chone.server.domains.auth.dto.CustomUserDetails;
import com.chone.server.domains.user.domain.Role;
import com.chone.server.domains.user.dto.request.LoginRequestDto;
import com.chone.server.domains.user.dto.response.LoginResponseDto;
import com.chone.server.domains.user.exception.UserExceptionCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j(topic = "로그인 및 JWT 생성")
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtUtil jwtUtil;

    public LoginFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl("/api/v1/users/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            LoginRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);

            Authentication authentication = getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getUsername(),
                            requestDto.getPassword(),
                            null
                    )
            );
            // 🔹 로그인한 사용자 정보 가져오기
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            //삭제된 유저인지 체크
            if(userDetails.getUser().getDeletedAt() != null){
                throw new ApiBusinessException(UserExceptionCode.USER_DELETED);
            }
            // 🔹 isAvailable 체크
            if (!userDetails.isEnabled()) {
                throw new ApiBusinessException(UserExceptionCode.USER_CANT_LOGIN);
            }
            return authentication;
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    //로그인 성공시 실행하는 메서드(Jwt 발급)
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        String username = ((CustomUserDetails) authResult.getPrincipal()).getUsername();
        Role role = ((CustomUserDetails) authResult.getPrincipal()).getUser().getRole();

        String token = jwtUtil.createToken(username, role);
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, token);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(new ObjectMapper().writeValueAsString(new LoginResponseDto(username,token)));
    }

    //로그인 실패시 실행 메서드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(401);
    }
}
