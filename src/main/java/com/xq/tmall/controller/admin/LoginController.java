package com.xq.tmall.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.xq.tmall.entity.Admin;
import com.xq.tmall.service.AdminService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * 后台管理-登录页
 */
@Controller
public class LoginController {
    private Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);
    @Resource(name = "adminService")
    private AdminService adminService;

    //转到后台管理-登录页
    @RequestMapping("admin/login")
    public String goToPage(){
        logger.info("转到后台管理-登录页");
        return "admin/loginPage";
    }

    //登陆验证-ajax
    @ResponseBody
    @RequestMapping(value = "admin/login/{username}",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    public String checkLogin(HttpSession session, @PathVariable String username, @RequestParam String password) {
        logger.info("管理员登录验证");
        Admin admin = adminService.login(username,password);

        JSONObject object = new JSONObject();
        if(admin == null){
            logger.info("登录验证失败");
            object.put("success",false);
        } else {
            logger.info("登录验证成功，管理员ID传入会话");
            session.setAttribute("adminId",admin.getAdmin_id());
            object.put("success",true);
        }

        return object.toJSONString();
    }

    //获取管理员头像路径-ajax
    @ResponseBody
    @RequestMapping(value = "admin/login/profile_picture/{username}",method = RequestMethod.GET,produces = "application/json;charset=utf-8")
    public String getAdminProfilePicture(@PathVariable("username") String username){
        logger.info("根据用户名获取管理员头像路径");
        Admin admin = adminService.get(username,null);

        JSONObject object = new JSONObject();
        if(admin == null){
            logger.info("未找到头像路径");
            object.put("success",false);
        } else {
            logger.info("成功获取头像路径");
            object.put("success",true);
            object.put("srcString",admin.getAdmin_profile_picture_src());
        }

        return object.toJSONString();
    }
}
