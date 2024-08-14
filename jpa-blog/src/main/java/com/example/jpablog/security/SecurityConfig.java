package com.example.jpablog.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import com.example.jpablog.repository.UserRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig{
	private final UserDetailsService userDetailsService;

	public SecurityConfig(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
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
					.defaultSuccessUrl("/profile", true)
					.permitAll()  // 로그인 페이지는 누구나 접근 가능
			)
			.logout(logout ->
				logout
					.permitAll()  // 로그아웃은 누구나 접근 가능
			)
			.userDetailsService(userDetailsService); // UserDetailsService 설정
		return http.build();

	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}


}
