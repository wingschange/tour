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

@Tag(name = "Auth", description = "Authentication endpoints")
@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    private UserService userService;

    @Operation(summary = "Register a new user")
    @PostMapping("/register")
    public R<Void> register(@Valid @RequestBody RegisterDto registerDto) {
        userService.register(registerDto);
        return R.success();
    }

    @Operation(summary = "Login and get JWT token")
    @PostMapping("/login")
    public R<Map<String, String>> login(@Valid @RequestBody LoginDto loginDto) {
        String token = userService.login(loginDto);
        Map<String, String> result = new HashMap<>();
        result.put("token", token);
        return R.success(result);
    }
}
