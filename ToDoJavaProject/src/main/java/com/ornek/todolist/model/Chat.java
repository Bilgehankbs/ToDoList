package com.ornek.todolist.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Sohbet entity'si
 * Kullanıcılar arasında not paylaşımı ve görev yönetimi için
 */
@Entity
@Table(name = "note_chats")
public class Chat {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Sohbet adı
    @Column(nullable = false)
    private String name;

    // Sohbet tipi (Bireysel/Grup)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChatType type;

    // Not paragrafı (uzun metin)
    @Lob
    @Column(columnDefinition="TEXT")
    private String paragraph;

    // Sohbeti oluşturan kullanıcı
    @ManyToOne
    @JoinColumn(name = "created_by_id", nullable = false)
    private User createdBy;

    // Sohbet üyeleri ve rolleri
    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatMember> members = new ArrayList<>();

    // İçindeki task ve to-do list’ler
    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatItem> items = new ArrayList<>();

    // getters / setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public ChatType getType() { return type; }
    public void setType(ChatType type) { this.type = type; }

    public String getParagraph() { return paragraph; }
    public void setParagraph(String paragraph) { this.paragraph = paragraph; }

    public User getCreatedBy() { return createdBy; }
    public void setCreatedBy(User createdBy) { this.createdBy = createdBy; }

    public List<ChatMember> getMembers() { return members; }
    public void setMembers(List<ChatMember> members) { this.members = members; }

    public List<ChatItem> getItems() { return items; }
    public void setItems(List<ChatItem> items) { this.items = items; }
}
