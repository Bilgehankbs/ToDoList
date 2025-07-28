// Ortak JavaScript fonksiyonları
document.addEventListener('DOMContentLoaded', function() {
    // Alert mesajlarını 2.5 saniye sonra otomatik kapat
    const alerts = document.querySelectorAll('.alert');
    alerts.forEach(function(alert) {
        setTimeout(function() {
            alert.classList.add('fade-out');
            setTimeout(function() {
                alert.remove();
            }, 300);
        }, 2500);
    });
}); 