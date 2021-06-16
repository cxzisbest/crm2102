package com.bjpowernode.crm.settings.service;

import com.bjpowernode.crm.settings.domain.TblUser;
import com.bjpowernode.crm.settings.domain.User;

import java.util.List;
import java.util.Map;

/**
 * 2021/4/23
 */
public interface UserService {

    TblUser selectUserById(String id);

    User queryUserByLoginActAndPwd(Map<String, Object> map);

    List<User> queryAllUers();
}
