package com.example.userservice2.security;

import com.example.userservice2.dto.UserDto;
import com.example.userservice2.service.UserService;
import com.example.userservice2.vo.RequestLogin;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.ArrayList;
import java.util.Date;

@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final UserService userService;
    private final Environment env;

    public AuthenticationFilter(AuthenticationManager authenticationManager, UserService userService, Environment env) {
//        super(authenticationManager);
        super.setAuthenticationManager(authenticationManager);
        this.userService = userService;
        this.env = env;
    }

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
//        super.successfulAuthentication(request, response, chain, authResult);// 이거 주석 안하면 로그인 시 에러가 발생함
        String username = ((User) authResult.getPrincipal()).getUsername();
        log.debug(username);
        UserDto userDetails = userService.getUserDetailsByEmail(username);
        /*
        Claims claims = Jwts.claims();
        claims.setSubject("user_token");
        Date date = new Date();
        Date exp = new Date(date.getTime() + 1000 * 60 * 60 * 24);
        claims.setIssuedAt(date);
        claims.setExpiration(exp);

        SecretKey secretKey = Keys.hmacShaKeyFor("6v9y$B&E)H@MbQeThWmZq4t7w!z%C*F-".getBytes(StandardCharsets.UTF_8));
//        JwtParser parser = Jwts.parserBuilder().setSigningKey(secretKey).build();

        String token = Jwts.builder()
                .setClaims(claims)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
         */
        String token = Jwts.builder()
                .setSubject(userDetails.getUserId())
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(env.getProperty("token.expiration_time"))))
                .signWith(Keys.hmacShaKeyFor("6v9y$B&E)H@MbQeThWmZq4t7w!z%C*F-Ev78q(*u$@h%)Sdk@%*df930-452J*I)@*$skdfst#(oeskejf13854ry@)*$#EU9".getBytes(StandardCharsets.UTF_8)),
                        SignatureAlgorithm.HS512)
                .compact();

        response.addHeader("token", token);
        response.addHeader("userId", userDetails.getUserId());
        // https://targetcoders.com/jjwt-%ec%82%ac%ec%9a%a9-%eb%b0%a9%eb%b2%95/
    }
}
