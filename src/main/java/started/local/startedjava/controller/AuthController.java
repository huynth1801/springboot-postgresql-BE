package started.local.startedjava.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import started.local.startedjava.dto.request.UserCreationRequest;
import started.local.startedjava.service.AuthService;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    // API đăng ký
    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody UserCreationRequest request) {
        String response = authService.registerUser(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/success")
    public String getMessage() {
        return "Hello success";
    }

}

