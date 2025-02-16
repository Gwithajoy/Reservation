package com.zerobase.reservation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 애플리케이션 전역에서 사용할 Bean들을 설정하는 클래스입니다.
 * 여기서는 패스워드 암호화를 위한 BCryptPasswordEncoder Bean을 등록합니다.
 */
@Configuration
public class AppConfig {

    /**
     * BCryptPasswordEncoder를 사용하여 사용자 비밀번호 암호화를 위한 PasswordEncoder Bean을 생성합니다.
     *
     * @return PasswordEncoder 인스턴스
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
