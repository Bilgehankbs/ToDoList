package com.ornek.todolist.repo;

import com.ornek.todolist.model.ChatMember;
import com.ornek.todolist.model.User;
import com.ornek.todolist.model.Chat;
import com.ornek.todolist.model.ChatMember.Key;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatMemberRepository extends JpaRepository<ChatMember, Key> {
    List<ChatMember> findByUser(User user);
    Optional<ChatMember> findByChatAndUser(Chat chat, User user);
    void deleteByChat(Chat chat);
}
