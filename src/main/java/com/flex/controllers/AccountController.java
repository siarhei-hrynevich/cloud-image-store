package com.flex.controllers;

import com.flex.config.jwt.JwtProvider;
import com.flex.models.UserModel;
import com.flex.services.implementation.UserService;
import com.flex.viewModels.LoginViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.security.auth.login.AccountException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Controller
public class AccountController {
    @Autowired
    JwtProvider provider;
    @Autowired
    private UserService userService;

    @PostMapping("/auth")
    public String auth(LoginViewModel loginViewModel, String redirectedUrl, HttpServletResponse response) {
        try {
            UserModel user = userService.findByLoginAndPassword(loginViewModel.getLogin(), loginViewModel.getPassword());
            response.addCookie(createAuthCookie(user.getLogin()));
            if (redirectedUrl == null)
                redirectedUrl = "/";
            return String.format("redirect:%s", redirectedUrl);
        } catch (AccountException e) {
            return "redirect:/login?error=true";
        }
    }

    @GetMapping("/out")
    public String logout(HttpServletResponse response) {
        response.addCookie(createLogoutCookie());
        return "redirect:/";
    }

    private Cookie createLogoutCookie() {
        Cookie cookie = new Cookie("jwt", "");
        cookie.setMaxAge(0);
        return cookie;
    }

    private Cookie createAuthCookie(String login) {
        Cookie cookie = new Cookie("jwt", provider.generateToken(login));
        cookie.setMaxAge(3600 * 24);
        return cookie;
    }
}
