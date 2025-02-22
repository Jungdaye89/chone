package com.chone.server.commons.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@OpenAPIDefinition(
        info = @Info(
                //서비스 이름
                title = "Chone",
                //서비스설명
                description = "광화문 인근 음식 주문 서비스",
                //서비스 버전
                version = "1.0"
        ),
        servers = {@Server(url = "${server.url}", description = "Our Server URL")}
)
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {

        // 인증 요청 방식에 HEADER 추가
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP) // HTTP 인증 방식 사용
                .scheme("bearer")               // Bearer 토큰 방식 설정
                .bearerFormat("JWT")            // JWT 형식 사용
                .in(SecurityScheme.In.HEADER)   // 인증 정보를 HTTP 헤더에서 받음
                .name("Authorization");         // 헤더의 키를 "Authorization"으로 지정

        // bearerAuth 이름으로 보안 요구사항 정의
        SecurityRequirement securityRequirement = new SecurityRequirement().addList("bearerAuth");

        return new OpenAPI()

                // SecurityRequirement에 정의한 bearerAuth, 위에서 정의한 securityScheme 추가
                .components(new Components().addSecuritySchemes("bearerAuth", securityScheme))

                // 보안 규칙 추가
                .security(Arrays.asList(securityRequirement));
    }
}
