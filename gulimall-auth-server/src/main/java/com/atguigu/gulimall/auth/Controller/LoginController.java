package com.atguigu.gulimall.auth.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author weepppp 2022/7/18 13:35
 **/
@Controller
public class LoginController {

    @GetMapping("/login.html")
    public String loginPage(){
        return "login";
    }

    @GetMapping("/reg.html")
    public String toRegister(){
        return "reg";
    }
}
