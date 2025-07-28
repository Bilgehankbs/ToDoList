package com.ornek.todolist.controller;

import com.ornek.todolist.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;


@Controller
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserService userService,
                          PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    // registration form backing bean'i her request'te model'e koyar
    @ModelAttribute("userForm")
    public RegistrationForm registrationForm() {
        return new RegistrationForm();
    }

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String showRegisterForm() {
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(
            @ModelAttribute("userForm") @Valid RegistrationForm form,
            BindingResult bindingResult) {

        // JSR-303 validation hataları varsa geri dön
        if (bindingResult.hasErrors()) {
            return "auth/register";
        }

        // şifre eşleşmesini null-safe kontrol et
        if (form.getPassword() == null || form.getPassword().trim().isEmpty()) {
            bindingResult.rejectValue(
                    "password",
                    "error.password",
                    "Parola boş olamaz"
            );
            return "auth/register";
        }
        
        if (form.getPasswordConfirm() == null || form.getPasswordConfirm().trim().isEmpty()) {
            bindingResult.rejectValue(
                    "passwordConfirm",
                    "error.passwordConfirm",
                    "Parola tekrarı boş olamaz"
            );
            return "auth/register";
        }
        
        if (!form.getPassword().equals(form.getPasswordConfirm())) {
            bindingResult.rejectValue(
                    "passwordConfirm",
                    "error.passwordConfirm",
                    "Parolalar eşleşmiyor"
            );
            return "auth/register";
        }

        try {
            // kullanıcıyı kaydetme işlemi
            String encoded = passwordEncoder.encode(form.getPassword());
            userService.register(form.getName(), form.getEmail(), encoded);
            return "redirect:/login?registered";
        } catch (IllegalStateException e) {
            // E-posta zaten kayıtlı hatası için özel mesaj
            if (e.getMessage().contains("Bu email adresi zaten kayıtlı")) {
                bindingResult.rejectValue(
                        "email",
                        "error.email",
                        "Bu e-posta adresi zaten kayıtlı!"
                );
            } else {
                bindingResult.rejectValue(
                        "username",
                        "error.username",
                        e.getMessage()
                );
            }
            return "auth/register";
        }
    }
}