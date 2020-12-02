package com.flex.controllers;

import com.flex.config.jwt.JwtProvider;
import com.flex.exeptions.UserAlreadyExistException;
import com.flex.models.ExtendedUserDetails;
import com.flex.models.UserModel;
import com.flex.services.implementation.UserService;
import com.flex.viewModels.LoginViewModel;
import com.flex.viewModels.RegisterViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.security.auth.login.AccountException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class AccountController extends BaseController {
    @Autowired
    private UserService userService;

    public AccountController(JwtProvider provider) {
        super(provider);
    }

    @PostMapping("/auth")
    public String auth(LoginViewModel loginViewModel, String redirectedUrl, HttpServletResponse response) {
        try {
            UserModel user = userService.findByLoginAndPassword(loginViewModel.getLogin(), loginViewModel.getPassword());
            response.addCookie(createAuthCookie(user.getLogin()));
            if (redirectedUrl == null)
                redirectedUrl = "/";
            return String.format("redirect:%s", redirectedUrl);
        } catch (AccountException e) {
            return "redirect:login";
        }
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("userForm") RegisterViewModel form) {
        if (!form.getPassword().equals(form.getConfirmPassword())) {
            return "redirect:register";
        }
        try {
            userService.registerNewUser(form);
            return "redirect:login";
        } catch (Exception ex) {
            return "redirect:register";
        }
    }

    @GetMapping("/account")
    public String account(HttpServletRequest request, Model model) {
        model.addAttribute("showDeleteButton", true);
        model.addAttribute("user", SecurityContextHolder.getContext().getAuthentication().getDetails());
        return getViewByHeader(request, model, "account/account");
    }

    @GetMapping("/account/{id}")
    public String accountOfUser(HttpServletRequest request, Model model, @PathVariable Long id) {
        ExtendedUserDetails details = (ExtendedUserDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();
        if(details.getAuthorities().contains("ROLE_ADMIN") || details.getId().equals(id))
            model.addAttribute("showDeleteButton", true);
        model.addAttribute("user", details);
        return getViewByHeader(request, model, "account/account");
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
