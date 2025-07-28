package com.ornek.todolist;

import com.ornek.todolist.model.*;
import com.ornek.todolist.repo.*;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Configuration
public class DatabaseSeeder {

    @Bean
    @Transactional
    public ApplicationRunner seedData(UserRepository userRepo,
                                      ChatRepository chatRepo,
                                      ChatMemberRepository memberRepo,
                                      TaskRepository taskRepo,

                                      PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepo.count() > 0) {
                System.out.println("⚠️⚠️⚠️Veritabanı zaten dolu, seed işlemi atlandı. Program Çalışıyor!");
                return;
            }

            taskRepo.deleteAllInBatch();
            memberRepo.deleteAllInBatch();
            chatRepo.deleteAllInBatch();
            userRepo.deleteAllInBatch();

            // 10 kullanıcıyı tek listede hazırla
            List<User> users = IntStream.rangeClosed(1, 10)
                    .mapToObj(i -> {
                        User u = new User();
                        u.setName("user" + i);
                        u.setEmail("user" + i + "@example.com");
                        u.setPassword(passwordEncoder.encode("pass" + i));
                        u.setStatus(UserStatus.ONLINE);
                        return u;
                    })
                    .collect(Collectors.toList());
            userRepo.saveAll(users);

            User alice = users.get(0);
            User bob   = users.get(1);

            // Bireysel sohbet (Alice ↔ Bob)
            Chat indChat = new Chat();
            indChat.setName("Alice & Bob Sohbeti");
            indChat.setType(ChatType.INDIVIDUAL);
            indChat.setParagraph("Alice ile Bob arasındaki bireysel not sohbeti.");
            indChat.setCreatedBy(alice);
            indChat = chatRepo.save(indChat);

            memberRepo.saveAll(List.of(
                    new ChatMember(indChat, alice, ChatMemberRole.ADMIN),
                    new ChatMember(indChat, bob,   ChatMemberRole.MEMBER)
            ));

            System.out.println("✅✅✅ Örnek veriler başarıyla yüklendi.");
        };
    }
}