# ToDo Java Project
Sınıf arkadaşlarımla geliştirdiğimiz 1.sınıf oop dersinin projesi ToDo project. Amaç Java dilini etkin kullanarak bir proje geliştirmekti. Kullanıcılar görevlerini oluşturabilir, notlarını kaydedebilir ve sohbetler yönetebilir. Yani basitçe bir not alma ve görev ekleme uygulamasıdır. Pictures klasöründe görsel olarak son hali yer almaktadır.

## 🚀 Özellikler

- **👤 Kullanıcı Sistemi**: Kayıt, giriş, profil yönetimi
- **📝 Not & Görev**: Oluşturma, düzenleme, silme (boş notlar da kaydedilebilir)
- **💬 Sohbetler**: Bireysel/grup sohbetleri, her sohbet için ayrı not/görev alanları
- **🎨 Modern UI**: Bootstrap 5, responsive tasarım, gradient arka plan
- **🔒 Güvenlik**: Spring Security, BCrypt, form validasyonu
- **⚡ Otomatik**: 2.5 saniye uyarı mesajları, otomatik kapanma

## 🛠️ Teknolojiler

- **Backend**: Java, Spring Boot
- **Frontend**: HTML, CSS, JS
- **Database**: MySQL 
- **Security**: Spring Security, BCrypt

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

## 🔮 Gelecek Planları

-  Mobil uygulama
-  Gerçek zamanlı sohbet
-  Dosya yükleme
-  Mail bildirimleri

## 📞 İletişim

- bilgehanakbas0@gmail.com
- https://www.linkedin.com/in/bilgehan-akbas/

### Takım Arkadaşlarım

- **Mustafa Karadeniz** - official.mustafakaradeniz@gmail.com
- **Yavuzhan Kılıç** - yavuzhankilic@icloud.com
- **Efe Arda Dakes** - efe.dakes@gmail.com  
- **Akif Korkmaz** - akifkorkmaz11@hotmail.com
