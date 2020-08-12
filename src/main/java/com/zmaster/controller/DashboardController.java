package com.zmaster.controller;


import com.sun.org.glassfish.external.statistics.Stats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * 仪表板页面
 *
 * @param model
 * @return
 */

/**
 */
@Controller
public class DashboardController {




    @RequestMapping("/dashboard")
    public String dashboard(Model model) {


        return "dashboard";
    }



}