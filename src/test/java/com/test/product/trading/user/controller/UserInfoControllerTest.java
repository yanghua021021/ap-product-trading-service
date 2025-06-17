package com.test.product.trading.user.controller;

import com.alibaba.fastjson.JSON;
import com.test.product.trading.common.rsp.JsonResponse;
import com.test.product.trading.user.entity.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import javax.annotation.Resource;


@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class UserInfoControllerTest {

    @Resource
    private UserInfoController userInfoController;

    /**
     * 添加用户
     *
     * @return JsonResponse
     */
    @Test
    public void addUser() throws Exception {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserNumber("13920002002");
        userInfo.setUserName("王小虎");
        JsonResponse rsp = userInfoController.addUser(userInfo);
        log.info(JSON.toJSONString(rsp));
        Assert.assertTrue(200 == rsp.getStatus());
    }
}
