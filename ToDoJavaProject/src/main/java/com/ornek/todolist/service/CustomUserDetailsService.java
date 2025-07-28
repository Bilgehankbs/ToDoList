package com.ornek.todolist.service;

import com.ornek.todolist.model.User;
import com.ornek.todolist.repo.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepo;

    public CustomUserDetailsService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    /**
     * Spring Security, login sırasında bu metodu çağırır.
     * DB’de kullanıcı bulunamazsa UsernameNotFoundException fırlatılır.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1) Kullanıcıyı DB’den çek
        User user = userRepo.findByEmail(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Kullanıcı bulunamadı: " + username)
                );

        // 2) GrantedAuthority listesi (şimdilik tek ROLE_USER)
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");

        // 3) Spring’in kendi UserDetails nesnesi
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),  // ← bu doğru
                user.getPassword(),
                Collections.singletonList(authority)
        );
    }
    }
