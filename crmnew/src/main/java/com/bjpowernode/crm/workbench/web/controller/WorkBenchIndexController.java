package com.bjpowernode.crm.workbench.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 2021/4/25
 */
@Controller
public class WorkBenchIndexController {

    @RequestMapping("/workbench/index.do")
    public String index(){

        return "workbench/index";
    }


}
