package com.tour.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger / OpenAPI 文档配置
 *
 * <p>访问地址：http://localhost:8080/swagger-ui/index.html</p>
 * <p>接口 JSON：http://localhost:8080/v3/api-docs</p>
 *
 * <p>所有需要登录的接口请先在右上角 "Authorize" 中填写 JWT Token（格式：Bearer &lt;token&gt;）</p>
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("旅游分享平台 API")
                        .description("旅游分享平台后端接口文档。\n\n"
                                + "**认证方式**：登录后将返回的 token 填入右上角 Authorize → Bearer Authentication。\n\n"
                                + "**统一响应格式**：`{\"code\": 0, \"message\": \"success\", \"data\": {}}`")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Tour Team")
                                .email("support@tour.com")))
                // 全局安全方案：JWT Bearer Token
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components().addSecuritySchemes("Bearer Authentication",
                        new SecurityScheme()
                                .name("Bearer Authentication")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("请输入登录接口返回的 token，无需加 Bearer 前缀")));
    }
}
