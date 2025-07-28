package com.ornek.todolist.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * Görev entity'si - ChatItem'dan türetilmiş
 * Her görev bir sohbete ait olur ve başlık + içerik içerir
 */
@Entity
@Table(name = "tasks")
public class Task extends ChatItem {
    public Task() {}

    // Görev içeriği (uzun metin)
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    /**
     * Görev oluşturucu - chat, başlık ve içerik ile
     */
    public Task(Chat chat, String title, String content) {
        this.setChat(chat);
        this.setTitle(title);
        this.content = content;
        this.setPinned(false);
    }

    // getters / setters

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
