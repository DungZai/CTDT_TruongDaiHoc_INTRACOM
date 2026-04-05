package vn.intracom.chuongtrinhdaotao.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class OtpService {

    private final JavaMailSender mailSender;

    // Lưu OTP tạm thời: email → {otp, expireTime}
    // Dùng ConcurrentHashMap để thread-safe
    private final Map<String, OtpEntry> otpStore = new ConcurrentHashMap<>();

    // OTP hết hạn sau 5 phút
    private static final long OTP_EXPIRE_MS = 5 * 60 * 1000;

    // ── Tạo và gửi OTP ───────────────────────────────────────────────────────
    public void sendOtp(String email) {
        String otp = generateOtp();
        long expireAt = System.currentTimeMillis() + OTP_EXPIRE_MS;

        // Lưu vào bộ nhớ
        otpStore.put(email, new OtpEntry(otp, expireAt));

        // Gửi email
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("[Intracom University] Mã xác thực OTP");
            message.setText(
                "Xin chào!\n\n" +
                "Mã OTP đăng ký tài khoản của bạn là:\n\n" +
                "    " + otp + "\n\n" +
                "Mã có hiệu lực trong 5 phút. Vui lòng không chia sẻ mã này với ai.\n\n" +
                "Trân trọng,\nIntracom University"
            );
            mailSender.send(message);
            log.info("Đã gửi OTP tới email: {}", email);
        } catch (Exception e) {
            // In ra toàn bộ lỗi gốc để debug
            log.error("Lỗi gửi email OTP tới {}: {}", email, e.getMessage(), e);
            throw new RuntimeException("Không thể gửi email OTP: " + e.getMessage());
        }
    }

    // ── Xác thực OTP ─────────────────────────────────────────────────────────
    public boolean verifyOtp(String email, String otp) {
        OtpEntry entry = otpStore.get(email);
        if (entry == null) return false;
        if (System.currentTimeMillis() > entry.expireAt()) {
            otpStore.remove(email); // Xóa OTP hết hạn
            return false;
        }
        return entry.otp().equals(otp);
    }

    // ── Xóa OTP sau khi đăng ký thành công ───────────────────────────────────
    public void clearOtp(String email) {
        otpStore.remove(email);
    }

    // ── Tạo mã OTP 6 số ──────────────────────────────────────────────────────
    private String generateOtp() {
        return String.format("%06d", new Random().nextInt(999999));
    }

    // ── Record lưu OTP ────────────────────────────────────────────────────────
    private record OtpEntry(String otp, long expireAt) {}
}