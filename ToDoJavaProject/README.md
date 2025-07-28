# ToDo Java Project

Merhabalar
Sınıf arkadaşlarımla geliştirdiğimiz 1.sınıf oop dersinin projesi ToDo project. Amaç Java dilini etkin kullanarak bir proje geliştirmekti. Kullanıcılar görevlerini oluşturabilir, notlarını kaydedebilir ve sohbetler yönetebilir. Yani basitçe bir not alma ve görev ekleme uygulamasıdır.

## 🚀 Özellikler

- **👤 Kullanıcı Sistemi**: Kayıt, giriş, profil yönetimi
- **📝 Not & Görev**: Oluşturma, düzenleme, silme (boş notlar da kaydedilebilir)
- **💬 Sohbetler**: Bireysel/grup sohbetleri, her sohbet için ayrı not/görev alanları
- **🎨 Modern UI**: Bootstrap 5, responsive tasarım, gradient arka plan
- **🔒 Güvenlik**: Spring Security, BCrypt, form validasyonu
- **⚡ Otomatik**: 2.5 saniye uyarı mesajları, otomatik kapanma

## 🛠️ Teknolojiler

- **Backend**: Spring Boot 3.4.4, Java 17, Maven
- **Frontend**: Thymeleaf, Bootstrap 5, Vanilla JS
- **Database**: MySQL 8.0
- **Security**: Spring Security, BCrypt

## ⚡ Hızlı Başlangıç

### 1. Klonlayın
```bash
git clone https://github.com/kullaniciadi/ToDoJavaProject.git
cd ToDoJavaProject
```

### 2. Veritabanını Kurun
```sql
CREATE DATABASE todolist_db;
```

### 3. Konfigürasyon
`src/main/resources/application.properties` dosyasını düzenleyin:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/todolist_db
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 4. Çalıştırın
```bash
mvn spring-boot:run
```

### 5. Test Kullanıcısı
- **Email:** user1@example.com
- **Şifre:** pass1

Uygulama: `http://localhost:8080`

## 📁 Proje Yapısı

```
src/main/
├── java/com/ornek/todolist/
│   ├── config/          # Güvenlik
│   ├── controller/      # MVC
│   ├── model/           # Entity'ler
│   ├── repo/            # Repository
│   ├── service/         # İş mantığı
│   └── DatabaseSeeder.java
└── resources/
    ├── static/          # CSS, JS
    ├── templates/       # Thymeleaf
    └── application.properties
```

## 🔐 Güvenlik

- BCrypt şifre hash'leme
- Frontend + Backend validasyon
- Session yönetimi
- XSS/CSRF koruması

## 📸 Ekran Görüntüleri

### Ana Sayfa
![Ana Sayfa](screenshots/main-page.png)

### Giriş Sayfası
![Giriş](screenshots/login.png)

### Profil Sayfası
![Profil](screenshots/profile.png)

## 🔮 Gelecek Planları

- [ ] Mobil uygulama
- [ ] Gerçek zamanlı sohbet
- [ ] Dosya yükleme
- [ ] Email bildirimleri

## 📝 Lisans

MIT License

## 📞 İletişim

bilgehanakbas0@gmail.com
https://www.linkedin.com/in/bilgehan-akbas/

### Takım Arkadaşlarım

**Mustafa Karadeniz** - official.mustafakaradeniz@gmail.com
**Efe Arda Dakes** - efe.dakes@gmail.com  
**Yavuzhan Kılıç** - yavuzhankilic@icloud.com
**Akif Korkmaz** - [LinkedIn](https://linkedin.com/in/akif-korkmaz)
