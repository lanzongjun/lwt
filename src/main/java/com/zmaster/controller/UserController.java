package com.zmaster.controller;

import javax.servlet.http.HttpSession;

import com.zmaster.mapper.UserMapper;
import com.zmaster.model.ResObject;
import com.zmaster.model.User;

import com.zmaster.util.Constant;
import com.zmaster.util.DateUtil;
import com.zmaster.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import java.util.Date;
import java.util.List;

/**
 * 用户管理
 */
@Controller
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private HttpSession httpSession;




    /**
     * 登录跳转
     *
     * @param model
     * @return
     */
    @GetMapping("/user/login")
    public String loginGet(Model model) {
        return "login";
    }

    /**
     * 登录
     *
     * @param
     * @param model
     * @param
     * @return
     */
    @PostMapping("/user/login")
    public String loginPost(User user, Model model) {
        User user1 = userMapper.selectByNameAndPwd(user);
        if (user1 != null) {
            httpSession.setAttribute("user", user1);
            User name = (User) httpSession.getAttribute("user");
            return "redirect:/dashboard";
        } else {
            model.addAttribute("error", "用户名或密码错误，请重新登录！");
            return "login";
        }
    }

    /**
     * 注册
     *
     * @param model
     * @return
     */
    @GetMapping("/user/register")
    public String register(Model model) {
        return "register";
    }

    /**
     * 注册
     *
     * @param model
     * @return
     */
    @PostMapping("/user/register")
    public String registerPost(User user, Model model) {
        System.out.println("用户名" + user.getUserName());
        try {
            userMapper.selectIsName(user);
            model.addAttribute("error", "该账号已存在！");
        } catch (Exception e) {
            Date date = new Date();
            user.setAddDate(date);
            user.setUpdateDate(date);
            userMapper.insert(user);
            System.out.println("注册成功");
            model.addAttribute("error", "恭喜您，注册成功！");
            return "login";
        }

        return "register";
    }


    /**
     * 登录跳转
     *
     * @param model
     * @return
     */
    @GetMapping("/user/list_{pageCurrent}_{pageSize}_{pageCount}")
    public String list(User user,@PathVariable Integer pageCurrent,
                            @PathVariable Integer pageSize,
                            @PathVariable Integer pageCount,Model model) {
        if (pageSize == 0) pageSize = 2;
        if (pageCurrent == 0) pageCurrent = 1;

        int rows = userMapper.count(user);
        if (pageCount == 0) pageCount = rows % pageSize == 0 ? (rows / pageSize) : (rows / pageSize) + 1;
        user.setEnd((pageCurrent - 1) * pageSize);
        user.setStart(pageSize);
        List<User> userList = userMapper.list(user);
        for (User i : userList) {
            i.setDateStr(DateUtil.getDateStr(i.getUpdateDate()));
        }
        String pageHTML = PageUtil.getPageContent("/user/list_{pageCurrent}_{pageSize}_{pageCount}", pageCurrent, pageSize, pageCount);
        model.addAttribute("pageHTML", pageHTML);
        model.addAttribute("userlist",userList);
        return "/user/userlist";
    }
    @GetMapping("/user/userEdit")
    public String userEdit(Model model, User user) {
        User user1= userMapper.getUserById(user);
        if(user1==null){
            model.addAttribute("item",user);
        }else{
            model.addAttribute("item",user1);
        }

        return "user/userEdit";
    }

    @PostMapping("/user/userEditState")
    @ResponseBody
    public ResObject<Object> userEditState(Model model, User user) {
        userMapper.deleteUser(user);
        ResObject<Object> object = new ResObject<Object>(Constant.Code01, Constant.Msg01, null, null);
        return object;
    }

    @PostMapping("/user/userEdit")
    public String puserEdit(Model model, User user) {
        Date date = new Date();
        user.setAddDate(date);
        user.setUpdateDate(date);
        if (user.getId() != 0) {
            userMapper.update(user);
        } else {
            userMapper.insert(user);
        }
        return "redirect:list_0_0_0";
    }

    @GetMapping("/user/forget")
    public String forgetGet(Model model) {

        return "forget";
    }
    /**
     * 登录
     *
     * @param
     * @param model
     * @param
     * @return
     */
    @PostMapping("/user/forget")
    public String forgetPost(User user, Model model) {
        String password = userMapper.selectPasswordByName(user);
        if (password == null) {
            model.addAttribute("error", "帐号不存在或邮箱不正确！");
        } else {
//            String email = user.getEmail();
//            SimpleMailMessage message = new SimpleMailMessage();
//            message.setFrom(Sender);
//            message.setTo(email); //接收者邮箱
//            message.setSubject("YX后台信息管理系统-密码找回");
//            StringBuilder sb = new StringBuilder();
//            sb.append(user.getUserName() + "用户您好！您的注册密码是：" + password + "。感谢您使用YX信息管理系统！");
//            message.setText(sb.toString());
//            mailSender.send(message);
//            model.addAttribute("error", "密码已发到您的邮箱,请查收！");
        }
        return "forget";

    }

    @GetMapping("/user/userManage")
    public String userManageGet(Model model) {
        User user = (User) httpSession.getAttribute("user");
        User user1 = userMapper.selectByNameAndPwd(user);
        model.addAttribute("user", user1);
        return "user/userManage";
    }

    @PostMapping("/user/userManage")
    public String userManagePost(Model model, User user, HttpSession httpSession) {
        Date date = new Date();
        user.setUpdateDate(date);
        int i = userMapper.update(user);
        httpSession.setAttribute("user",user);
        return "redirect:userManage";
    }

}
