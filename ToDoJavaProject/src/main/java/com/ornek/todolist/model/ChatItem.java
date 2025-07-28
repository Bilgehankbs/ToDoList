package com.ornek.todolist.model;

import jakarta.persistence.*;

/**
 * Sohbet öğelerinin temel sınıfı
 * Task ve diğer sohbet öğeleri bu sınıftan türetilir
 * JOINED inheritance stratejisi kullanır
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "chat_items")
public abstract class ChatItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Öğe başlığı
    @Column(nullable = false)
    private String title;

    // Hangi sohbete ait olduğu
    @ManyToOne
    @JoinColumn(name = "chat_id", nullable = false)
    private Chat chat;

    @Column(name = "pinned", nullable = false)
    private Boolean pinned = false;


    // getters / setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Chat getChat() { return chat; }
    public void setChat(Chat chat) { this.chat = chat; }

    public Boolean getPinned() { return pinned; }
    public void setPinned(Boolean pinned) { this.pinned = pinned; }

}
