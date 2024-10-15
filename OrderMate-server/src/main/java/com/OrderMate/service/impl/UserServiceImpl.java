package com.OrderMate.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.OrderMate.constant.MessageConstant;
import com.OrderMate.dto.UserLoginDTO;
import com.OrderMate.entity.User;
import com.OrderMate.exception.LoginFailedException;
import com.OrderMate.mapper.UserMapper;
import com.OrderMate.properties.WeChatProperties;
import com.OrderMate.service.UserService;
import com.OrderMate.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: UserServiceImpl
 * Package: com.OrderMate.service.impl
 * Description:
 *
 * @Author Gush
 * @Create 2024-02-26 15:21
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    // 微信服务接口地址
    private static final String WX_LOGIN = "https://api.weixin.qq.com/sns/jscode2session";
    @Autowired
    private WeChatProperties weChatProperties;
    @Autowired
    private UserMapper userMapper;
    /*
    * 微信用户登录
    * */
    public User wxLogin(UserLoginDTO userLoginDTO) {

        // 调用定义的函数，获得openid
        String openid = getOpenid(userLoginDTO.getCode());

        // 判断openid是否为空，如果为空表示登录失败，抛出业务异常
        if(openid == null) {
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }
        // 判断当前用户是否为新用户
        User user = userMapper.getByOpenid(openid);
        // 若是新用户，自动完成注册
        if(user == null) {
            user = User.builder()
                    .openid(openid)
                    .createTime(LocalDateTime.now())
                    .build();
            userMapper.insert(user);
        }
        // 返回这个用户对象
        return user;
    }

    private String getOpenid(String code) {
        // 调用微信接口服务，获得当前微信用户的openid
        Map<String, String> map = new HashMap<>();
        map.put("appid",weChatProperties.getAppid());
        map.put("secret",weChatProperties.getSecret());
        map.put("js_code",code);
        map.put("grant_type","authorization_code");
        String json = HttpClientUtil.doGet(WX_LOGIN, map);

        JSONObject jsonObject = JSON.parseObject(json);
        String openid = jsonObject.getString("openid");
        return openid;
    }
}
