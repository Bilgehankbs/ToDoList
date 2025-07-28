package com.ornek.todolist.service;

import com.ornek.todolist.model.Chat;
import com.ornek.todolist.model.ChatMember;
import com.ornek.todolist.model.ChatMemberRole;
import com.ornek.todolist.model.ChatType;
import com.ornek.todolist.model.User;
import com.ornek.todolist.repo.ChatMemberRepository;
import com.ornek.todolist.repo.ChatRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Sohbet işlemleri servisi
 * Sohbet oluşturma, güncelleme, silme ve üye yönetimi
 */
@Service
@Transactional
public class ChatService {

    private final ChatRepository chatRepository;
    private final ChatMemberRepository chatMemberRepository;

    public ChatService(ChatRepository chatRepository,
                       ChatMemberRepository chatMemberRepository) {
        this.chatRepository = chatRepository;
        this.chatMemberRepository = chatMemberRepository;
    }

    /**
     * Sohbetin not paragrafını günceller
     */
    public void updateParagraph(Long chatId, String paragraph) {
        if (chatId == null) {
            throw new IllegalArgumentException("Sohbet ID'si belirtilmelidir");
        }
        if (paragraph == null) {
            paragraph = "";
        }
        
        Chat chat = getChatById(chatId);
        chat.setParagraph(paragraph.trim());
        chatRepository.save(chat);
    }

    /**
     * Sohbet bilgilerini günceller
     */
    public void updateChat(Chat chat) {
        chatRepository.save(chat);
    }

    /** 
     * Kullanıcının üyesi olduğu tüm sohbetleri getirir
     */
    public List<Chat> getChatsForUser(User user) {
        return chatRepository.findByMembersUser(user);
    }

    /**
     * ID'ye göre sohbet getirir
     * Sohbet bulunamazsa IllegalArgumentException fırlatır
     */
    public Chat getChatById(Long chatId) {
        return chatRepository.findById(chatId)
                .orElseThrow(() -> new IllegalArgumentException("Geçersiz sohbet ID: " + chatId));
    }


    /** 
     * Yeni sohbet oluşturur ve yaratıcısını ADMIN rolüyle ekler
     */
    public Chat createChat(ChatType type, String name, User creator) {
        // Girdi doğrulama
        if (type == null) {
            throw new IllegalArgumentException("Sohbet tipi belirtilmelidir");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Sohbet adı boş olamaz");
        }
        if (creator == null) {
            throw new IllegalArgumentException("Sohbet oluşturucu belirtilmelidir");
        }

        Chat chat = new Chat();
        chat.setType(type);
        chat.setName(name.trim());
        chat.setCreatedBy(creator);
        // JpaRepository sayesinde save metodu var
        chat = chatRepository.save(chat);

        ChatMember member = new ChatMember();
        member.setChat(chat);
        member.setUser(creator);
        member.setRole(ChatMemberRole.ADMIN);
        chatMemberRepository.save(member);

        return chat;
    }

    /**
     * Chat'i siler (sadece ADMIN rolündeki kullanıcılar silebilir)
     */
    public void deleteChat(Long chatId, User user) {
        Chat chat = getChatById(chatId);
        
        // Kullanıcının bu chat'te ADMIN rolünde olup olmadığını kontrol et
        ChatMember member = chatMemberRepository.findByChatAndUser(chat, user)
                .orElseThrow(() -> new IllegalArgumentException("Bu sohbete erişiminiz yok"));

        if (member.getRole() != ChatMemberRole.ADMIN) {
            throw new IllegalArgumentException("Sadece sohbet yöneticileri sohbeti silebilir");
        }

        // Önce chat'e ait tüm üyeleri sil
        chatMemberRepository.deleteByChat(chat);
        
        // Sonra chat'i sil
        chatRepository.delete(chat);
    }
}
