package com.ornek.todolist.repo;

import com.ornek.todolist.model.Chat;
import com.ornek.todolist.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    // Kullanıcının üyesi olduğu sohbetleri getirir
    List<Chat> findByMembersUser(User user);

}
