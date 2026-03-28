package itis.controller;

import itis.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.concurrent.ThreadLocalRandom;

@Controller
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegistrationController {

    private final UserService userService;

    @GetMapping
    public String getRegistrationPage() {
        return "register"; 
    }

    @PostMapping
    public String registerUser(
            @RequestParam(name = "username") String username,
            @RequestParam(name = "email") String email,
            @RequestParam(name = "password") String password,
            Model model
    ) {
        try {

            userService.registerNewUser(username, password,  email);
            return "redirect:/register/success";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", true);
            return "register";
        }
    }

    @GetMapping("/success")
    public String getRegistrationSuccessPage() {
        return "registration_success";
    }
}
