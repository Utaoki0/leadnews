package com.heima.user.service.impl;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.alibaba.nacos.common.utils.MD5Utils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.common.domain.ResponseResult;
import com.heima.common.enums.AppHttpCodeEnum;
import com.heima.common.utils.AppJwtUtil;
import com.heima.user.domain.dto.LoginDto;
import com.heima.user.domain.po.ApUser;
import com.heima.user.service.ApUserService;
import com.heima.user.mapper.ApUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * @author Lenovo
 * @description 针对表【ap_user(APP用户信息表)】的数据库操作Service实现
 * @createDate 2025-03-28 22:13:28
 */
@Service
@RequiredArgsConstructor
public class ApUserServiceImpl extends ServiceImpl<ApUserMapper, ApUser>
        implements ApUserService {

    @Override
    public ResponseResult login(LoginDto loginDto) {
        //1.正常登录
        if (StringUtils.isNotBlank(loginDto.getPhone())
                && StringUtils.isNotBlank(loginDto.getPassword())) {
            // 1.1 根据手机号查询用户
            ApUser user = getOne(Wrappers.<ApUser>lambdaQuery()
                    .eq(ApUser::getPhone, loginDto.getPhone()));
            // 1.2 判断用户是否存在
            if (user == null) {
                return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST, "用户信息不存在");
            }
            // 1.3比对密码 todo 未加密
            if (!user.getPassword().equals(loginDto.getPassword())) {
                return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR, "密码错误");
            }
            user.setPassword("");// 密码不返回
            user.setSalt("");// 盐值不返回
            HashMap<String, Object> map = new HashMap<>();
            map.put("token", AppJwtUtil.getToken(user.getId().longValue()));
            map.put("user", user);
            return ResponseResult.okResult(map);
        }
        //2.游客登录
        else {
            HashMap<String, Object> map = new HashMap<>();
            String token = AppJwtUtil.getToken(0L);
            map.put("token", token);
            return ResponseResult.okResult(map);
        }
    }
}




