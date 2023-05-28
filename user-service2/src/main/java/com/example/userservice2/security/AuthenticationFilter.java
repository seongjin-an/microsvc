package com.example.userservice2.security;

import com.example.userservice2.vo.RequestLogin;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.ArrayList;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    // 인증 요청이 들어왔을 경우
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            // 로그인 요청은 post 요청이므로 파라미터로 받을 수 없음, 따라서 inputstream 사용
            RequestLogin creds = new ObjectMapper().readValue(request.getInputStream(), RequestLogin.class);
            // 스프링 시큐리티에서 사용할 수 있는 것을 넘겨줘야 하기에,
            // UsernamePasswordAuthenticationToken 으로 변경하고 인증처리해주는 매니저에게 넘겨주면 됨.
            return getAuthenticationManager()
                    .authenticate(new UsernamePasswordAuthenticationToken(creds.getEmail(), creds.getPassword(), new ArrayList<>()));
        } catch (IOException error) {
            throw new RuntimeException(error);
        }
    }

    // 로그인 성공했을 때 어떤 처리를 할지?(보통 토큰 만료시간 설정하고, 어떤 반환 값을 전달하지 정한다.)
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
//        super.successfulAuthentication(request, response, chain, authResult);
    }
}
