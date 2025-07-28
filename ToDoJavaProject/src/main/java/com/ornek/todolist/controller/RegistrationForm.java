package com.ornek.todolist.controller;


import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class RegistrationForm {
    @NotBlank(message = "Kullanıcı adı boş olamaz")
    @Size(min = 3, max = 20, message = "Kullanıcı adı 3-20 karakter arasında olmalıdır")
    private String name;

    @NotBlank(message = "E-posta boş olamaz")
    @Email(message = "Geçerli bir e-posta adresi giriniz")
    private String email;

    @NotBlank(message = "Şifre boş olamaz")
    @Size(min = 4, message = "Şifre en az 4 karakter olmalıdır")
    private String password;

    @NotBlank(message = "Şifre tekrarı boş olamaz")
    private String passwordConfirm;

    /** Şifre ve şifre tekrarı eşleşiyor mu? */
    @AssertTrue(message = "Şifreler eşleşmiyor")
    public boolean isPasswordMatching() {
        if (password == null || passwordConfirm == null) {
            return false;
        }
        return password.equals(passwordConfirm);
    }

    // getter / setter'lar
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getPasswordConfirm() { return passwordConfirm; }
    public void setPasswordConfirm(String passwordConfirm) { this.passwordConfirm = passwordConfirm; }

    /**
     * Manuel validation: boş veya null alanları kontrol eder.
     * Hata varsa IllegalArgumentException fırlatır.
     */
    public void validate() {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Kullanıcı adı boş olamaz");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email boş olamaz");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Parola boş olamaz");
        }
        if (passwordConfirm == null || passwordConfirm.trim().isEmpty()) {
            throw new IllegalArgumentException("Parola tekrarı boş olamaz");
        }
        if (!password.equals(passwordConfirm)) {
            throw new IllegalArgumentException("Parolalar eşleşmiyor");
        }
    }
}