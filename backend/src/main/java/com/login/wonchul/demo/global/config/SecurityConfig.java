package com.login.wonchul.demo.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // Bean을 수동 등록하기 위함
@EnableWebSecurity // Spring Security 활성화
@RequiredArgsConstructor // private final로 선언된 클래서 DI
public class SecurityConfig {
    private final ObjectMapper objectMapper;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http    .csrf(AbstractHttpConfigurer::disable) // CSRF 보안 비활성화
                .httpBasic(AbstractHttpConfigurer::disable) // 로그인 이후 refresh 토큰이 만료되기 전까지 토큰을 통한 인증을 진행할 것
                .formLogin(AbstractHttpConfigurer::disable) //
                .authorizeHttpRequests((authorize) -> authorize // 인증, 인가에 필요한 URL 지정
                        .requestMatchers("/signup", "/", "/login").permitAll() // 얘들은 인가 없이도 접근 허용
                        .anyRequest().authenticated()) // 그외 나머지 -> 인증이 필요함
                .logout((logout) -> logout
                        .logoutSuccessUrl("/login") // 로그아웃 이후 이동할 페이지
                        .invalidateHttpSession(true)) //로그아웃 이후 전체 세션 삭제 여부 : true
                .sessionManagement(session -> session // 세션 생성 및 사용 여부에 대한 정책 설정
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );
        return http.build();
    }
}
