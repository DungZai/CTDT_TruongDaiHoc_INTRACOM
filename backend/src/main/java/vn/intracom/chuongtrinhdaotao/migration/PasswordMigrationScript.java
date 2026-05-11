package vn.intracom.chuongtrinhdaotao.migration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import vn.intracom.chuongtrinhdaotao.entity.Users;
import vn.intracom.chuongtrinhdaotao.repository.UserRepository;

import java.util.List;

@Slf4j
@Component
@Order(1) // Chạy đầu tiên khi khởi động app
@RequiredArgsConstructor
public class PasswordMigrationScript implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        log.info("=== Bắt đầu kiểm tra và migration mật khẩu ===");
        
        try {
            List<Users> allUsers = userRepository.findAll();
            int migratedCount = 0;
            
            for (Users user : allUsers) {
                String currentPassword = user.getPassword();
                
                // Kiểm tra nếu password chưa được mã hóa BCrypt
                if (!isBCryptEncoded(currentPassword)) {
                    
                    log.warn("⚠️ Phát hiện mật khẩu plain text cho user: {}", user.getUsername());
                    log.debug("   Password cũ: {} (length: {})", currentPassword, currentPassword.length());
                    
                    // Mã hóa mật khẩu hiện tại (giữ nguyên giá trị)
                    String encodedPassword = passwordEncoder.encode(currentPassword);
                    user.setPassword(encodedPassword);
                    userRepository.save(user);
                    
                    migratedCount++;
                    
                    log.info("✅ Đã mã hóa mật khẩu cho user: {}", user.getUsername());
                    log.debug("   Password mới: {}... (length: {})", 
                             encodedPassword.substring(0, 20), encodedPassword.length());
                }
            }
            
            if (migratedCount > 0) {
                log.info("=== Migration hoàn tất: Đã mã hóa {} user ===", migratedCount);
                log.warn("⚠️⚠️⚠️ QUAN TRỌNG: Hãy XÓA hoặc VÔ HIỆU HÓA class PasswordMigrationScript ngay!");
            } else {
                log.info("=== Kiểm tra hoàn tất: Tất cả mật khẩu đã được mã hóa đúng ===");
            }
            
        } catch (Exception e) {
            log.error("❌ Lỗi khi migration mật khẩu", e);
        }
    }
    
    /**
     * Kiểm tra xem password đã được mã hóa BCrypt chưa
     */
    private boolean isBCryptEncoded(String password) {
        // BCrypt format: $2a$10$... hoặc $2b$10$... (60 ký tự)
        return password != null 
                && password.length() == 60 
                && (password.startsWith("$2a$") || password.startsWith("$2b$"));
    }
}