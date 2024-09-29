package com.example.demo;

import com.example.demo.user.AccDeniedHandle;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final AccDeniedHandle accDeniedHandle;

    public SecurityConfig(AccDeniedHandle accDeniedHandle) {
        this.accDeniedHandle = accDeniedHandle;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                                //.requestMatchers("/create", "/annon/create").hasRole("SUPERADMIN")
                        .requestMatchers("/**"
                        //        "/signpage",
                        //        "/css/**",
                        //        "/js/**",
                        //        "/",
                        //        "/board/list",
                        //        "/annon/main",
                        //        "/acc-denied",
                        //        "/chk-user",
                        //        "/chk-signup",
                        //        "annon/load"
                ).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/login") //사용자 정의 PostMapping 로그인 페이지
                        .defaultSuccessUrl("/", true) //로그인 성공 후 이동할 페이지
                        .permitAll()
                )
                .logout((logout) -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout")) //로그아웃을 받을 GetMapping
                        .logoutSuccessUrl("/") //로그아웃 완료 후 이동할 페이지
                        .invalidateHttpSession(true)) //세션 해제
                .exceptionHandling(exceptionHandling -> exceptionHandling.accessDeniedHandler(accDeniedHandle)) //특정페이지에 권한문제로 접속하지 못할경우 accDeineHandle 클래스 실행
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/chk-user", "chk-signup") // 특정 경로에 대해 CSRF 비활성화
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }



}