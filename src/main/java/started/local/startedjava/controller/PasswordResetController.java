package started.local.startedjava.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import started.local.startedjava.dto.request.EmailRequest;
import started.local.startedjava.dto.request.ResetPasswordRequest;
import started.local.startedjava.dto.response.ResetPasswordSuccessResponse;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")public class PasswordResetController {
    private final ResetPasswordService resetPasswordService;

    @PostMapping("/password-reset/request")
    public ResponseEntity<Map<String, String>> requestPasswordReset(@RequestBody EmailRequest request) {
        String link = resetPasswordService.requestResetPassword(request.getEmail());
        return ResponseEntity.ok(Map.of("resetLink", link));
    }


    @PostMapping("/password-reset/confirm")
    public ResponseEntity<ResetPasswordSuccessResponse> confirmPasswordReset(@RequestBody ResetPasswordRequest request) {
        resetPasswordService.resetPassword(request.getToken(), request.getNewPassword());
        return ResponseEntity.ok(new ResetPasswordSuccessResponse("Đặt lại mật khẩu thành công. Bạn có thể đăng nhập ngay bây giờ."));
    }
}
