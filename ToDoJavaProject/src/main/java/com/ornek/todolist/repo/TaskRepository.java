package com.ornek.todolist.repo;

import com.ornek.todolist.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Görev veritabanı işlemleri
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    /**
     * Belirli bir sohbete ait görevleri getirir
     */
    List<Task> findByChatId(Long chatId);
}
