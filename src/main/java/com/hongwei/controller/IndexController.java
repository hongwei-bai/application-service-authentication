package com.hongwei.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {
    @RequestMapping(path = {"/index.do", "/"})
    @ResponseBody
    public String index() {
        return "Mike Hello My SpringBoot!";
    }
}
