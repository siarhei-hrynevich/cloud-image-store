package com.flex.controllers;

import com.flex.config.jwt.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class HomeController extends BaseController {

    @Autowired
    public HomeController(JwtProvider provider) {
        super(provider);
    }

    @GetMapping("/")
    public String client(HttpServletRequest request, Model model) {
        model.addAttribute("content", "/about");
        setAuthenticated(request, model);
        return "client";
    }


    @GetMapping("/help")
    public String help() {
        return "home/help";
    }

    @GetMapping("/about")
    public String about(HttpServletRequest request, Model model) {
        return getViewByHeader(request, model, "home/index");
    }

    @GetMapping("/search")
    public String search(HttpServletRequest request, Model model) {
        return getViewByHeader(request, model, "images/search");
    }

    @GetMapping("/uploading")
    public String uploading(HttpServletRequest request, Model model) {
        return getViewByHeader(request, model, "images/uploading");
    }

    @GetMapping("/login")
    public String login(HttpServletRequest request, Model model) {
        return getViewByHeader(request, model, "account/loginPage");
    }

    @GetMapping("/register")
    public String register(HttpServletRequest request, Model model) {
        return getViewByHeader(request, model, "account/register");
    }
}
