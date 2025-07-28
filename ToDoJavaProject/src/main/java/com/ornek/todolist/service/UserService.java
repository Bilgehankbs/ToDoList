package com.ornek.todolist.service;

import com.ornek.todolist.model.User;
import com.ornek.todolist.repo.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Kullanıcı işlemleri servisi
 * Kullanıcı kayıt, giriş ve profil yönetimi
 */
@Service
@Transactional
public class UserService {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * ID ile kullanıcıyı getirir
     * Bulunamazsa IllegalArgumentException fırlatır
     */
    public User getById(Long id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Kullanıcı bulunamadı: " + id));
    }

    /**
     * Yeni kullanıcı kaydı oluşturur
     * Email benzersiz olmalıdır
     * 
     * @param name Kullanıcı adı
     * @param email Benzersiz email adresi
     * @param password Şifrelenmiş şifre
     */
    public User register(String name, String email, String password) {
        // Girdi doğrulama
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Kullanıcı adı boş olamaz");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email boş olamaz");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Şifre boş olamaz");
        }

        // Email formatı kontrolü
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Geçerli bir email adresi giriniz");
        }

        if (userRepo.findByEmail(email.trim()).isPresent()) {
            throw new IllegalStateException("Bu email adresi zaten kayıtlı: " + email);
        }
        
        User u = new User();
        u.setName(name.trim());
        u.setEmail(email.trim());
        u.setPassword(password);
        return userRepo.save(u);
    }
    /**
     * Email ile kullanıcı arar
     * Bulunamazsa boş Optional döner
     */
    public Optional<User> findByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    /**
     * Şifreyi encode eder
     */
    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    /**
     * Kullanıcıyı günceller
     */
    public User updateUser(User user) {
        return userRepo.save(user);
    }

}
