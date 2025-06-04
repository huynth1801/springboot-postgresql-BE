package started.local.startedjava.controller.authentication;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import started.local.startedjava.config.security.JwtUtils;
import started.local.startedjava.constant.AppConstant;
import started.local.startedjava.dto.request.authentication.LoginRequest;
import started.local.startedjava.dto.request.authentication.RegistrationRequest;
import started.local.startedjava.dto.request.authentication.UserRequest;
import started.local.startedjava.dto.response.authentication.JwtResponse;
import started.local.startedjava.dto.response.authentication.RegistrationResponse;
import started.local.startedjava.mapper.authentication.UserMapper;
import started.local.startedjava.repository.authentication.UserRepository;
import started.local.startedjava.service.authentication.RefreshTokenService;
import started.local.startedjava.service.authentication.VerificationService;

import java.time.Instant;


@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@CrossOrigin(AppConstant.FRONTEND_HOST)
@Slf4j
public class AuthController {
    private AuthenticationManager authenticationManager;
    private VerificationService verificationService;
    private RefreshTokenService refreshTokenService;
    private JwtUtils jwtUtils;
    private UserRepository userRepository;
    private UserMapper userMapper;

    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> register(@RequestBody UserRequest userRequest) {
        Long userId = verificationService.generateTokenVerify(userRequest);
        log.info("user id: " + userId);
        return ResponseEntity.status(HttpStatus.OK).body(new RegistrationResponse(userId));
    }

    @PostMapping("/registration/confirm")
    public ResponseEntity<ObjectNode> confirm(@RequestBody RegistrationRequest registration) {
        System.out.println("userId = " + registration.getUserId());
        System.out.println("token = " + registration.getToken());
        verificationService.confirmVerification(registration);
        return ResponseEntity.status(HttpStatus.OK).body(new ObjectNode(JsonNodeFactory.instance));
    }

    @GetMapping("/registration/{userId}/resend-token")
    public ResponseEntity<ObjectNode> resendToken(@PathVariable Long userId) {
        verificationService.resendVerificationToken(userId);
        return ResponseEntity.status(HttpStatus.OK).body(new ObjectNode(JsonNodeFactory.instance));
    }

    @GetMapping("/forgot-password")
    public ResponseEntity<ObjectNode> forgotPassword(@RequestParam String email) {
        verificationService.forgetPassword(email);
        return ResponseEntity.status(HttpStatus.OK).body(new ObjectNode(JsonNodeFactory.instance));
    }

    // Log in
    @PostMapping("/log-in")
    public ResponseEntity<JwtResponse> authenticate(@RequestBody LoginRequest loginRequest) {
        log.info("username request: " + loginRequest.getUsername());
        log.info("password request: " + loginRequest.getPassword());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        String jwt = jwtUtils.generateJwtToken(authentication);
        String refreshToken = refreshTokenService.createRefreshToken(authentication).getToken();

        return ResponseEntity.ok(new JwtResponse("Login success !", jwt, refreshToken, Instant.now()));
    }
}
