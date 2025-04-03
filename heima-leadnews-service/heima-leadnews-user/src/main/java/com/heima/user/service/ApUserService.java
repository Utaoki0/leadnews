package com.heima.user.service;

import com.heima.common.domain.ResponseResult;
import com.heima.user.domain.dto.LoginDto;
import com.heima.user.domain.po.ApUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Lenovo
* @description 针对表【ap_user(APP用户信息表)】的数据库操作Service
* @createDate 2025-03-28 22:13:28
*/
public interface ApUserService extends IService<ApUser> {

    /**
     * 登录
     * @param loginDto
     * @return
     */
    ResponseResult login(LoginDto loginDto);
}
