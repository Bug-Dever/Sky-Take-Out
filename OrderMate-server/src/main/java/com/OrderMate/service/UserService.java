package com.OrderMate.service;

import com.OrderMate.dto.UserLoginDTO;
import com.OrderMate.entity.User;

/**
 * ClassName: UserService
 * Package: com.OrderMate.service
 * Description:
 *
 * @Author Gush
 * @Create 2024-02-26 15:21
 */
public interface UserService {
    User wxLogin(UserLoginDTO userLoginDTO);
}
