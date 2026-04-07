package com.enotes.enotes_keeper.controller;

import com.enotes.enotes_keeper.entity.User;
import com.enotes.enotes_keeper.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // REGISTER PAGE
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    // SAVE USER
    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute User user, Model model) {

        // 🔥 CHECK IF EMAIL ALREADY EXISTS
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());

        if (existingUser.isPresent()) {
            model.addAttribute("error", "Email already registered!");
            return "register";
        }

        // 🔐 ENCODE PASSWORD
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);

        model.addAttribute("success", "Registered Successfully!");
        return "login";
    }

    // LOGIN PAGE (IMPORTANT)
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
}