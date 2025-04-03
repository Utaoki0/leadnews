package com.heima.user.controller.v1;

import com.heima.common.domain.ResponseResult;
import com.heima.user.domain.dto.LoginDto;
import com.heima.user.service.ApUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/login")
@Api(value = "用户登录", tags = "用户登录")
@RequiredArgsConstructor
@Slf4j
public class UserLoginController {
    @GetMapping("/hi")
    public ResponseResult hi() {
        return ResponseResult.okResult("hi");
    }

    private final ApUserService apUserService;

    @ApiOperation("登录")
    @PostMapping("/login_auth")
    public ResponseResult login(@RequestBody LoginDto loginDto) {
        log.info("登录参数：{}", loginDto);
        return apUserService.login(loginDto);
    }
}
