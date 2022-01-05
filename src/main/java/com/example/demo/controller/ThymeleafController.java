package com.example.demo.controller;


import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ThymeleafController {

    @GetMapping("/")
    public String index(Model model){
        model.addAttribute("name","Hoang Doan");
        return "index";
    }

    @GetMapping("/list")
    public String getList(Model model){
        String[] name = {
                "Hoang",
                "Long",
                "Hung",
                "Thang",
                "Vu"
        };
        return "signup";
    }
}
