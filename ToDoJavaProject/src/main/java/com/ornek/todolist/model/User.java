package com.ornek.todolist.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // created_at sütunu (insert sırasında set, update sırasında değişmez)
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // email
    @Column(nullable = false, unique = true)
    private String email;

    // name
    @Column(nullable = false)
    private String name;

    // password
    @Column(nullable = false)
    private String password;

    // Kullanıcı durumu (Çevrimiçi, Uzakta, Rahatsız Etmeyin)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status = UserStatus.ONLINE;

    // updated_at sütunu (her update öncesi set edilecek)
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    // JPA lifecycle callback’larıyla timestamp’leri otomatik atıyoruz
    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // --- getters & setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    // createdAt’ın set’i genelde kullanılmaz, sadece JPA kendisi set etsin

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public UserStatus getStatus() { return status; }
    public void setStatus(UserStatus status) { this.status = status; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    // updatedAt da sadece JPA tarafından güncellensin


}
