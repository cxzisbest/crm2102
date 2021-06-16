package com.bjpowernode.crm.commons.utils;

import sun.security.provider.MD5;

/**
 * 2021/4/25
 */
public class testMD5 {

    public static void main(String[] args) {
        String username=MD5Util.getMD5(MD5Util.getMD5("admin")+"jerry"); //盐值 salt
        System.out.println(username);
    }
}
