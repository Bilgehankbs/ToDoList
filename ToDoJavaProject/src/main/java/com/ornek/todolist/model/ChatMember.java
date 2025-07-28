package com.ornek.todolist.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "chat_members")
public class ChatMember {



    @Embeddable
    public static class Key implements Serializable {
        @Column(name = "chat_id")
        private Long chatId;

        @Column(name = "user_id")
        private Long userId;

        // equals / hashCode
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Key)) return false;
            Key key = (Key) o;
            return Objects.equals(chatId, key.chatId) &&
                    Objects.equals(userId, key.userId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(chatId, userId);
        }
    }

    @EmbeddedId
    private Key id = new Key();

    @ManyToOne
    @MapsId("chatId")
    @JoinColumn(name = "chat_id")
    private Chat chat;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private ChatMemberRole role;

    public ChatMember() {}


    public ChatMember(Chat chat, User user, ChatMemberRole role) {
        this.chat = chat;
        this.user = user;
        this.role = role;
    }

    // getters / setters

    public Key getId() { return id; }
    public void setId(Key id) { this.id = id; }

    public Chat getChat() { return chat; }
    public void setChat(Chat chat) { this.chat = chat; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public ChatMemberRole getRole() { return role; }
    public void setRole(ChatMemberRole role) { this.role = role; }
}
