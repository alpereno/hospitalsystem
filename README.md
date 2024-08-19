# hospitalsystem
Assignment

Database Tasarımı:
* Birisi Postgresql(Patients) diğeri Mysql (Notification) olmak üzere iki ayrı RDBMS kullanılmıştır.
  - Postgresql de patients, email_addresses, phone_numbers olmak üzere 3 tablo vardır. Birden çok telefon ve mail eklenebilmesi için bu yöntem tercih edilmiştir. Cinsiyet M-F olarak saklanır.(char)
  - Patient tablosunda ad soyad gibi bilgilerin yanında kullanıcıları tamamen silmek yerine is_active boolean değişkeni de saklanır. Ek olarak version_number, created_time, last_update_time ile kişinin geçmiş bilgileri de saklanır.
  - email ve sms bildirimlerini almak isteyip istememesine göre iki boolean değişken bulunur. 
  - email_address ve phone_number id numaraları ile patient'e bağlıdır. Bu iletişim tercihlerinin tiplerini de Personal-Work-Mobile  ek bilgi olarak saklar.
  * Mysql de notifications, notification_criteria, notification_messages ve target_patients olmak üzere 4 tablo vardır.
  - notification_criteria içinde bildirim için yaş aralığı ve cinsiyet bilgisini, notification_message ise bildirim mesajı ve başlığını barındırır. İkisi de id ile notifications'a bağlıdır.
  - target_patiens tablosu ise bildirim için uygun olan hastaların id numarasını, email, telefon numaralarını ve bildirim idsini saklar.  

Services:
*   Spring Boot ile iki ayrı servis yazılmıştır. Birisi Patient ile ilgili işlemleri yaparken diğeri Notification ile ilgili işlemleri gerçekleştirir.
*   Bu iki servis birbirleriyle Rabbitmq queue mechanism ile iletişim kurarlar.
    - Patient servisi CRUD işlemlerini gerçekleştirir, entityleri database de olduğu gibi relationaldır.
    - İşlemler direkt olarak entityden değil, Response, Request pattern uygulanmasından dolayı bu objelerden gerçekleşir.
    - Hata kontrolü için kendim ExceptionHandler classı, hata türleri oluşturdum.
    - Normal CRUD işlemlerinin yanında isme, soyisme, belirli yaş aralığına, yaş aralığı ve cinsiyet vererek arama yapabilme, repositoryde buna uygun metodlar tanımlanmıştır.
    - Silme işlemi yerine, Patient'in aktifliğini false yapma özelliği mevcuttur. Yine update işleminde eski kayıt deaktif edilerek yeni kaydın versiyon numarası artırılır.
    - Swagger ve Rabbitmq configurasyonları yapılmıştır.
* İkinci servis olan Notification servisi;
  - Yine notification için normal CRUD işlemlerine ek olarak target patientsleri getirme işlemlerini gerçekleştir. Entityler yine relationaldır.
  - Hata kontrolü için kendim ExceptionHandler class'ı, hata türleri oluşturdum.
  - Ek: Yeni bir notification eklenmesiyle beraber buna uygun olan bütün patientleri rabbitmq queue mekanizması kullanarak ile elde eder.
  - Bir notification silindiğinde target_patient tablosundan ilgili hastalar çıkarılır.
  - Bir hastanın bildirim alabilmesi için sadece bildirim kriterlere uygun olmasıyla değil, hasta; sms veya email bildirimi almak istiyor mu diye kontrol edilir, ona göre tabloya eklenir.
  - İstenildiği gibi yeni bir hasta kayıt edildiğinde veya güncellendiğinde uygun notification varsa yine target_table'a eklenir.
  - Swagger ve Rabbitmq configurasyonları yapılmıştır.

Front:
 * React kullanılarak iki sayfa oluşturuldu.
   - Birinci sayfa patientleri gösterip, ekleme silme, güncelleme, bulma ve listeleme işlemlerini yerine getirir.
   - İkinci sayfa yeni bir bildirim ekleme, silme, listemele işlemleriyle beraber bildirim alacak kişilerin tablosunu da barındırır.

Postman Collection ve veritabanı backupları projeye dahil edilmiştir. 

Saygılarımla,
Alperen Önal.
