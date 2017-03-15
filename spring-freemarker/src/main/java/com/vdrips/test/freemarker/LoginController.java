package com.vdrips.test.freemarker;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by baixf on 2017/3/15.
 */
@Controller
public class LoginController {

    @RequestMapping("/login_ftl")
    public ModelAndView login(){
        ModelAndView modelAndView = new ModelAndView("login");
        modelAndView.addObject("name","baixf");
        modelAndView.addObject("sex",1);
        return modelAndView;
    }

    @RequestMapping("/login_json")
    @ResponseBody
    public ModelAndView loginJson(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("name","baixf");
        modelAndView.addObject("sex",1);
        return modelAndView;
    }
}
