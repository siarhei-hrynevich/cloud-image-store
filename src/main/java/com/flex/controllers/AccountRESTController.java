package com.flex.controllers;

import com.flex.config.jwt.JwtProvider;
import com.flex.exeptions.UserAlreadyExistException;
import com.flex.models.UserModel;
import com.flex.services.implementation.UserService;
import com.flex.viewModels.LoginViewModel;
import com.flex.viewModels.RegisterViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountException;

@RestController
@RequestMapping("/api/account")
public class AccountRESTController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtProvider jwtProvider;

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("userForm") RegisterViewModel userForm) {
        if (!userForm.getPassword().equals(userForm.getConfirmPassword())) {
            return "password not equal to confirmPassword";
        }
        try {
            userService.registerNewUser(userForm);
            return "success";
        } catch (UserAlreadyExistException ex) {
            return ex.getMessage();
        }
    }

    @PostMapping("/login")
    public String auth(@ModelAttribute("loginForm") LoginViewModel request) {
        try {
            UserModel user = userService.findByLoginAndPassword(request.getLogin(), request.getPassword());
            return jwtProvider.generateToken(user.getLogin());
        } catch (AccountException e) {
            return e.getMessage();
        }
    }
}
