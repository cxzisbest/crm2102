package com.bjpowernode.crm.settings.web.controller;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.MD5Util;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 2021/4/23
 */
@Controller
public class UserController {


    @Autowired
    UserService userService;

    @RequestMapping("/settings/qx/user/toLogin.do")
    public String toLoing(HttpServletRequest request){
        //获取cookie
        Cookie[] cookies=request.getCookies();
        String loginAct=null;
        String loginPwd=null;

        for (Cookie cookie:cookies){
            String name=cookie.getName();
            if("loginAct".equals(name)){
                loginAct=cookie.getValue();
                continue;
            }

            if("loginPwd".equals(name)){
                loginPwd=cookie.getValue();
            }
        }

        if(loginAct!=null&&loginPwd!=null){
            //封装map
            Map<String,Object> map=new HashMap<>();
            map.put("loginAct", loginAct);
            map.put("loginPwd",MD5Util.getMD5(loginPwd));
            //调用业务层验证用户名和密码
            User user=userService.queryUserByLoginActAndPwd(map);
            request.getSession().setAttribute(Contants.SESSION_USER, user);
            //重定向：两次
            return "redirect:/workbench/index.do";
        }else{
            return "settings/qx/user/login";
        }

    }

    @RequestMapping("/setting/qx/user/login.do")
    //@ResponseBody将服务器返回对象（user,userList)->json user->{"username":"tom","passwrod":"123"} user.usernmae
    public @ResponseBody Object login(String loginAct, String loginPwd, String isRemPwd,HttpServletRequest request,HttpServletResponse response, HttpSession session){
        Map<String,Object> map=new HashMap<>();
        map.put("loginAct", loginAct);
        map.put("loginPwd",MD5Util.getMD5(loginPwd));
        //调用业务层验证用户名和密码
        User user=userService.queryUserByLoginActAndPwd(map);
        ReturnObject returnObject=new ReturnObject();

        if(user==null){
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("用户名或密码错误");

        }else {
            if(DateUtils.formatDateTime(new Date()).compareTo(user.getExpireTime())>0){
                //账号已经过期
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("账号已经过期");
            }else if("0".equals(user.getLockState())){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("账户被锁定");
            }/*else if(!user.getAllowIps().contains(request.getRemoteAddr())){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("ip受限");
            }*/else{
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                //要user保存在session,以后要在做登录验证时，要从seesion取出user
                session.setAttribute(Contants.SESSION_USER, user);
                if("true".equals(isRemPwd)){
                    //保存用户和密码
                    Cookie c1=new Cookie("loginAct",loginAct);
                    c1.setMaxAge(10*24*60*60);
                    response.addCookie(c1); //在客户端浏览保存c1

                    Cookie c2=new Cookie("loginPwd",loginPwd);
                    c2.setMaxAge(10*24*60*60);
                    response.addCookie(c2); //在客户端浏览保存c1
                }else{
                    Cookie c1=new Cookie("loginAct",null);
                    c1.setMaxAge(0);
                    response.addCookie(c1); //在客户端浏览保存c1

                    Cookie c2=new Cookie("loginPwd",null);
                    c2.setMaxAge(0);
                    response.addCookie(c2); //在客户端浏览保存c1
                }
            }
        }


        return returnObject;
    }
    @RequestMapping("/settings/qx/user/logout.do")
    public String logout(HttpServletResponse response,HttpSession session){
        Cookie c1=new Cookie("loginAct",null);
        c1.setMaxAge(0);
        response.addCookie(c1); //在客户端浏览保存c1

        Cookie c2=new Cookie("loginPwd",null);
        c2.setMaxAge(0);
        response.addCookie(c2); //在客户端浏览保存c1

        //销毁session
        session.invalidate();

        //重定向
        return "redirect:/"; //login.jsp
    }

}
