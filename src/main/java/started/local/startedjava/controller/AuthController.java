package started.local.startedjava.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import started.local.startedjava.dto.request.ApiResponse;
import started.local.startedjava.dto.request.AuthenticationRequest;
import started.local.startedjava.dto.request.UserCreationRequest;
import started.local.startedjava.dto.response.AuthenticationResponse;
import started.local.startedjava.dto.response.UserResponse;
import started.local.startedjava.service.AuthService;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    // API đăng ký
    @PostMapping("/register")
    public ApiResponse<UserResponse> register(@Valid @RequestBody UserCreationRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(authService.registerUser(request))
                .build();
    }

    // Login
    @PostMapping("/log-in")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        boolean result = authService.authenticate(request);
        return ApiResponse.<AuthenticationResponse>builder()
                .result(AuthenticationResponse.builder()
                        .authenticated(result)
                        .build()).build();
    }

}

