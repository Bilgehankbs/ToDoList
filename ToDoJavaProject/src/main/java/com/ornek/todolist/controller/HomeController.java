package com.ornek.todolist.controller;

import com.ornek.todolist.model.Chat;
import com.ornek.todolist.model.ChatType;
import com.ornek.todolist.model.Task;
import com.ornek.todolist.model.User;
import com.ornek.todolist.model.UserStatus;
import com.ornek.todolist.repo.TaskRepository;
import com.ornek.todolist.service.ChatService;
import com.ornek.todolist.service.UserService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
public class HomeController {

    private final ChatService chatService;
    private final UserService userService;
    private final TaskRepository taskRepository;

    public HomeController(ChatService chatService, UserService userService, TaskRepository taskRepository) {
        this.chatService = chatService;
        this.userService = userService;
        this.taskRepository = taskRepository;
    }

    /**
     * Ana sayfa: Kullanıcının sohbetlerini ve görevlerini listeler
     * GET / veya /chat isteklerini karşılar
     */
    @GetMapping({"/", "/chat"})
    public String chatIndex(Model model, Principal principal, 
                           @RequestParam(required = false) String error,
                           @RequestParam(required = false) String success) {
        User me = userService.findByEmail(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("Kullanıcı bulunamadı: " + principal.getName()));

        List<Chat> chats = chatService.getChatsForUser(me);
        List<Task> tasks = taskRepository.findAll();
        
        model.addAttribute("chats", chats);
        model.addAttribute("tasks", tasks);
        model.addAttribute("user", me);
        model.addAttribute("myName", me.getName());
        model.addAttribute("myId", me.getId());
        
        // Hata ve başarı mesajlarını ekle
        if (error != null) {
            model.addAttribute("error", error);
        }
        if (success != null) {
            model.addAttribute("success", success);
        }
        
        return "index";
    }

    /**
     * Belirli bir sohbeti gösterir ve o sohbete ait görevleri listeler
     * GET /chat/{chatId} isteklerini karşılar
     */
    @GetMapping("/chat/{chatId}")
    public String showChat(@PathVariable Long chatId, Model model, Principal principal,
                          @RequestParam(required = false) String error,
                          @RequestParam(required = false) String success) {
        User me = userService.findByEmail(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("Kullanıcı bulunamadı: " + principal.getName()));

        Chat currentChat = chatService.getChatById(chatId);
        if (currentChat == null) {
            return "redirect:/";
        }

        List<Chat> chats = chatService.getChatsForUser(me);
        List<Task> tasks = taskRepository.findByChatId(chatId);
        
        model.addAttribute("chat", currentChat);
        model.addAttribute("chats", chats);
        model.addAttribute("tasks", tasks);
        model.addAttribute("currentChat", currentChat);
        model.addAttribute("user", me);
        model.addAttribute("myName", me.getName());
        model.addAttribute("myId", me.getId());
        
        if (error != null) {
            model.addAttribute("error", error);
        }
        if (success != null) {
            model.addAttribute("success", success);
        }
        
        return "index";
    }

    /**
     * Sohbetin not paragrafını günceller
     * POST /chat/{chatId}/paragraph isteklerini karşılar
     */
    @PostMapping("/chat/{chatId}/paragraph")
    public String saveParagraph(@PathVariable Long chatId,
                                @RequestParam("paragraph") String paragraph,
                                Principal principal) {
        try {
            // Girdi doğrulama
            if (paragraph == null) {
                paragraph = "";
            }
            
            chatService.updateParagraph(chatId, paragraph.trim());
            return "redirect:/chat/" + chatId + "?success=NoteSaved";
        } catch (Exception e) {
            return "redirect:/chat/" + chatId + "?error=NoteSaveError";
        }
    }

    /**
     * Yeni sohbet oluşturur
     * POST /chats isteklerini karşılar
     */
    @PostMapping("/chats")
    public String createChat(@RequestParam String name, @RequestParam ChatType type, Principal principal) {
        try {
            User currentUser = userService.findByEmail(principal.getName())
                    .orElseThrow(() -> new UsernameNotFoundException("Kullanıcı bulunamadı"));

            // Girdi doğrulama
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Sohbet adı boş olamaz");
            }

            Chat chat = chatService.createChat(type, name.trim(), currentUser);
            return "redirect:/chat/" + chat.getId() + "?success=ChatCreated";
        } catch (Exception e) {
            return "redirect:/?error=ChatCreateError";
        }
    }

    /**
     * Yeni görev ekler
     * POST /task/add isteklerini karşılar
     * chatId belirtilmezse varsayılan sohbete ekler
     */
    @PostMapping("/task/add")
    public String addTask(@RequestParam String title, 
                         @RequestParam String content,
                         @RequestParam(required = false) Long chatId,
                         Principal principal) {
        try {
            User currentUser = userService.findByEmail(principal.getName())
                    .orElseThrow(() -> new UsernameNotFoundException("Kullanıcı bulunamadı"));

            // Hedef sohbeti belirle
            Chat targetChat;
            if (chatId != null) {
                // Belirli bir sohbete görev ekle
                targetChat = chatService.getChatById(chatId);
                if (targetChat == null) {
                    return "redirect:/";
                }
            } else {
                // Varsayılan sohbete ekle (yoksa yeni oluştur)
                List<Chat> userChats = chatService.getChatsForUser(currentUser);
                targetChat = userChats.isEmpty() ? 
                        chatService.createChat(ChatType.INDIVIDUAL, "Kişisel", currentUser) : 
                        userChats.get(0);
            }

            // Girdi doğrulama
            if (title == null || title.trim().isEmpty()) {
                throw new IllegalArgumentException("Task title cannot be empty");
            }
            
            if (content == null || content.trim().isEmpty()) {
                throw new IllegalArgumentException("Task content cannot be empty");
            }

            // Görevi oluştur ve kaydet
            Task task = new Task(targetChat, title.trim(), content.trim());
            taskRepository.save(task);
            
            if (chatId != null) {
                return "redirect:/chat/" + chatId + "?success=TaskAdded";
            } else {
                return "redirect:/?success=TaskAdded";
            }
        } catch (Exception e) {
            // Log error silently
            
            if (chatId != null) {
                return "redirect:/chat/" + chatId + "?error=TaskAddError";
            } else {
                return "redirect:/?error=TaskAddError";
            }
        }
    }

    /**
     * Görev düzenleme sayfasını gösterir
     * GET /task/edit/{taskId} isteklerini karşılar
     */
    @GetMapping("/task/edit/{taskId}")
    public String showEditTask(@PathVariable Long taskId,
                              @RequestParam(required = false) Long chatId,
                              Model model, Principal principal) {
        User me = userService.findByEmail(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("Kullanıcı bulunamadı"));

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Görev bulunamadı"));

        model.addAttribute("task", task);
        model.addAttribute("chatId", chatId);
        model.addAttribute("myName", me.getName());
        return "task/edit";
    }

    /**
     * Görev düzenleme işlemini gerçekleştirir
     * POST /task/edit/{taskId} isteklerini karşılar
     */
    @PostMapping("/task/edit/{taskId}")
    public String editTask(@PathVariable Long taskId,
                          @RequestParam String title,
                          @RequestParam String content,
                          @RequestParam(required = false) Long chatId,
                          Principal principal) {
        try {
            User currentUser = userService.findByEmail(principal.getName())
                    .orElseThrow(() -> new UsernameNotFoundException("Kullanıcı bulunamadı"));

            Task task = taskRepository.findById(taskId)
                    .orElseThrow(() -> new IllegalArgumentException("Görev bulunamadı"));

            // Girdi doğrulama
            if (title == null || title.trim().isEmpty()) {
                throw new IllegalArgumentException("Task title cannot be empty");
            }
            
            if (content == null || content.trim().isEmpty()) {
                throw new IllegalArgumentException("Task content cannot be empty");
            }

            // Görevi güncelle
            task.setTitle(title.trim());
            task.setContent(content.trim());
            taskRepository.save(task);

            if (chatId != null) {
                return "redirect:/chat/" + chatId + "?success=TaskUpdated";
            } else {
                return "redirect:/?success=TaskUpdated";
            }
        } catch (Exception e) {
            if (chatId != null) {
                return "redirect:/chat/" + chatId + "?error=TaskUpdateError";
            } else {
                return "redirect:/?error=TaskUpdateError";
            }
        }
    }

    /**
     * Görevi siler
     * GET /task/delete/{taskId} isteklerini karşılar
     */
    @GetMapping("/task/delete/{taskId}")
    public String deleteTask(@PathVariable Long taskId,
                           @RequestParam(required = false) Long chatId) {
        taskRepository.deleteById(taskId);
        
        if (chatId != null) {
            return "redirect:/chat/" + chatId + "?success=TaskDeleted";
        } else {
            return "redirect:/?success=TaskDeleted";
        }
    }

    /**
     * Sohbet düzenleme sayfasını gösterir
     * GET /chat/edit/{chatId} isteklerini karşılar
     */
    @GetMapping("/chat/edit/{chatId}")
    public String showEditChat(@PathVariable Long chatId, Model model, Principal principal) {
        User me = userService.findByEmail(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("Kullanıcı bulunamadı"));

        Chat chat = chatService.getChatById(chatId);
        model.addAttribute("chat", chat);
        model.addAttribute("myName", me.getName());
        return "chat/edit";
    }

    /**
     * Sohbet düzenleme işlemini gerçekleştirir
     * POST /chat/edit/{chatId} isteklerini karşılar
     */
    @PostMapping("/chat/edit/{chatId}")
    public String editChat(@PathVariable Long chatId,
                          @RequestParam String name,
                          @RequestParam ChatType type,
                          Principal principal) {
        try {
            User currentUser = userService.findByEmail(principal.getName())
                    .orElseThrow(() -> new UsernameNotFoundException("Kullanıcı bulunamadı"));

            Chat chat = chatService.getChatById(chatId);

            // Girdi doğrulama
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Sohbet adı boş olamaz");
            }

            // Sohbeti güncelle
            chat.setName(name.trim());
            chat.setType(type);
            chatService.updateChat(chat);

            return "redirect:/chat/" + chatId + "?success=ChatUpdated";
        } catch (Exception e) {
            return "redirect:/chat/" + chatId + "?error=ChatUpdateError";
        }
    }

    /**
     * Sohbeti siler
     * GET /chat/delete/{chatId} isteklerini karşılar
     */
    @GetMapping("/chat/delete/{chatId}")
    public String deleteChat(@PathVariable Long chatId, Principal principal) {
        User currentUser = userService.findByEmail(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("Kullanıcı bulunamadı"));

        chatService.deleteChat(chatId, currentUser);
        
        return "redirect:/?success=ChatDeleted";
    }

    /**
     * Profil sayfasını gösterir
     * GET /profile isteklerini karşılar
     */
    @GetMapping("/profile")
    public String showProfile(Model model, Principal principal) {
        User me = userService.findByEmail(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("Kullanıcı bulunamadı"));

        model.addAttribute("user", me);
        model.addAttribute("myName", me.getName());
        return "profile/index";
    }

    /**
     * Profil düzenleme sayfasını gösterir
     * GET /profile/edit isteklerini karşılar
     */
    @GetMapping("/profile/edit")
    public String showEditProfile(Model model, Principal principal,
                                 @RequestParam(required = false) String error,
                                 @RequestParam(required = false) String success,
                                 @RequestParam(required = false) String message) {
        User me = userService.findByEmail(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("Kullanıcı bulunamadı"));

        model.addAttribute("user", me);
        model.addAttribute("myName", me.getName());
        
        if (error != null) {
            model.addAttribute("error", error);
        }
        if (success != null) {
            model.addAttribute("success", success);
        }
        if (message != null) {
            model.addAttribute("message", message);
        }
        
        return "profile/edit";
    }

    /**
     * Kullanıcı durumunu günceller
     * POST /profile/status isteklerini karşılar
     */
    @PostMapping("/profile/status")
    public String updateStatus(@RequestParam UserStatus status, Principal principal) {
        try {
            User currentUser = userService.findByEmail(principal.getName())
                    .orElseThrow(() -> new UsernameNotFoundException("Kullanıcı bulunamadı"));

            currentUser.setStatus(status);
            userService.updateUser(currentUser);

            return "redirect:/profile?success=StatusUpdated";
        } catch (Exception e) {
            return "redirect:/profile?error=StatusUpdateError";
        }
    }

    /**
     * Profil güncelleme işlemini gerçekleştirir
     * POST /profile/edit isteklerini karşılar
     */
    @PostMapping("/profile/edit")
    public String editProfile(@RequestParam String name,
                             @RequestParam String email,
                             @RequestParam(required = false) String newPassword,
                             @RequestParam(required = false) String confirmPassword,
                             Principal principal) {
        // Null kontrolü
        if (name == null) name = "";
        if (email == null) email = "";
        if (newPassword == null) newPassword = "";
        if (confirmPassword == null) confirmPassword = "";
        try {
            User currentUser = userService.findByEmail(principal.getName())
                    .orElseThrow(() -> new UsernameNotFoundException("Kullanıcı bulunamadı"));

            // Girdi doğrulama
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("İsim boş olamaz");
            }
            
            // Email değişikliğini engelle - güvenlik nedeniyle
            if (!email.equals(currentUser.getEmail())) {
                throw new IllegalArgumentException("E-posta adresi güvenlik nedeniyle değiştirilemez");
            }

            // Şifre değişikliği kontrolü
            if (newPassword != null && !newPassword.trim().isEmpty()) {
                if (confirmPassword == null || !newPassword.equals(confirmPassword)) {
                    throw new IllegalArgumentException("Şifreler eşleşmiyor");
                }
                if (newPassword.trim().length() < 4) {
                    throw new IllegalArgumentException("Şifre en az 4 karakter olmalıdır");
                }
            } else if (confirmPassword != null && !confirmPassword.trim().isEmpty()) {
                // Sadece confirm password dolu ise hata ver
                throw new IllegalArgumentException("Yeni şifre alanı boş olamaz");
            }

            // Profili güncelle
            currentUser.setName(name.trim());
            currentUser.setEmail(email.trim());
            
            // Şifre değişikliği
            if (newPassword != null && !newPassword.trim().isEmpty()) {
                currentUser.setPassword(userService.encodePassword(newPassword.trim()));
            }
            
            userService.updateUser(currentUser);

            return "redirect:/profile?success=ProfileUpdated";
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            // URL'de özel karakterler sorun yaratmasın diye kod kullanıyoruz
            if (errorMessage != null && errorMessage.contains("Şifreler eşleşmiyor")) {
                return "redirect:/profile/edit?error=PasswordMismatch";
            } else if (errorMessage != null && errorMessage.contains("en az 6 karakter")) {
                return "redirect:/profile/edit?error=PasswordTooShort";
            } else if (errorMessage != null && errorMessage.contains("Yeni şifre alanı boş")) {
                return "redirect:/profile/edit?error=PasswordEmpty";
            } else if (errorMessage != null && errorMessage.contains("İsim boş")) {
                return "redirect:/profile/edit?error=NameEmpty";
            } else if (errorMessage != null && errorMessage.contains("E-posta adresi güvenlik")) {
                return "redirect:/profile/edit?error=EmailChangeNotAllowed";
            } else {
                return "redirect:/profile/edit?error=GeneralError";
            }
        }
    }
}
