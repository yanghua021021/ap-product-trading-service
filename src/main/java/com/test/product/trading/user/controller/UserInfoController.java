package com.test.product.trading.user.controller;


import com.alibaba.fastjson.JSON;
import com.test.product.trading.common.config.UrlConfig;
import com.test.product.trading.common.rsp.JsonResponse;
import com.test.product.trading.user.entity.UserInfo;
import com.test.product.trading.user.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用户信息表 前端控制器
 * </p>
 *
 * @author yh
 * @since 2025-06-15
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class UserInfoController {

    private final UserInfoService userInfoService;

    /**
     * 添加用户
     *
     * @param userInfo 参数
     * @return JsonResponse
     */
    @PostMapping(value = UrlConfig.URL_USER_INFO_ADD_V1, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonResponse addUser(@RequestBody UserInfo userInfo) {
        log.info("UserInfoController addUser userInfo:{} ", JSON.toJSONString(userInfo));
        return userInfoService.addUser(userInfo);
    }

}

