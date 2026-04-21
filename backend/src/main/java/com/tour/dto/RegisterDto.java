package com.tour.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 注册请求 DTO
 */
@Schema(description = "注册请求参数")
@Data
public class RegisterDto {

    @Schema(description = "用户名（3~50 个字符）", example = "zhangsan", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 50, message = "用户名长度必须在 3~50 个字符之间")
    private String username;

    @Schema(description = "密码（6~100 个字符）", example = "123456", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 100, message = "密码长度必须在 6~100 个字符之间")
    private String password;

    @Schema(description = "邮箱", example = "zhangsan@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;
}
