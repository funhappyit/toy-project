package com.example.bloghome.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig{
	private final UserDetailsService userDetailsService;
	private final AuthenticationSuccessHandler authenticationSuccessHandler;

	public SecurityConfig(UserDetailsService userDetailsService, AuthenticationSuccessHandler authenticationSuccessHandler) {
		this.userDetailsService = userDetailsService;
		this.authenticationSuccessHandler = authenticationSuccessHandler;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
		http.csrf(csrf -> csrf
			.disable()  // CSRF 보호를 비활성화할 경로
			)
			.authorizeHttpRequests(authorizeHttpRequests ->
				authorizeHttpRequests
					.requestMatchers("/", "/home", "/signup", "/login").permitAll()  // 접근 허용 설정
					.requestMatchers("/profile").authenticated()  // 인증된 사용자만 접근 가능한 경로
					.anyRequest().authenticated()  // 그 외의 모든 요청은 인증이 필요
			)

			.formLogin(formLogin ->
				formLogin
					.loginPage("/login")  // 사용자 정의 로그인 페이지 설정
					.successHandler(authenticationSuccessHandler)
					.permitAll()  // 로그인 페이지는 누구나 접근 가능
			)
			.logout(logout ->
				logout
					.permitAll()  // 로그아웃은 누구나 접근 가능
			)
			.sessionManagement(sessionManagement ->
				sessionManagement
					.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)  // 세션 필요 시 생성
					.sessionFixation().none()  // 세션 고정 공격 방지
					.maximumSessions(1)  // 최대 세션 수 설정 (필요에 따라 조정)
					.expiredUrl("/login?expired")  // 세션 만료 시 리다이렉트 URL
					.and()
					.sessionFixation().newSession()  // 로그인 후 세션을 새로 생성
			)
			.userDetailsService(userDetailsService); // UserDetailsService 설정
		return http.build();

	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}


}
