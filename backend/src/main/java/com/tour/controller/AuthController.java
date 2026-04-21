package com.tour.controller;

import com.tour.common.R;
import com.tour.dto.LoginDto;
import com.tour.dto.RegisterDto;
import com.tour.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * 认证控制器 —— 注册、登录
 */
@Tag(name = "认证", description = "用户注册与登录接口")
@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    private UserService userService;

    /** 注册新用户 */
    @Operation(summary = "注册", description = "注册新用户账号，默认角色为 USER")
    @PostMapping("/register")
    public R<Void> register(@Valid @RequestBody RegisterDto registerDto) {
        userService.register(registerDto);
        return R.success();
    }

    /** 登录并获取 JWT Token */
    @Operation(summary = "登录", description = "用户名密码登录，成功后返回 JWT Token")
    @PostMapping("/login")
    public R<Map<String, String>> login(@Valid @RequestBody LoginDto loginDto) {
        String token = userService.login(loginDto);
        Map<String, String> result = new HashMap<>();
        result.put("token", token);
        return R.success(result);
    }
}
