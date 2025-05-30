package started.local.startedjava.controller.authentication;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;
import started.local.startedjava.config.security.JwtUtils;
import started.local.startedjava.constant.AppConstant;
import started.local.startedjava.dto.request.authentication.RegistrationRequest;
import started.local.startedjava.dto.request.authentication.UserRequest;
import started.local.startedjava.dto.response.authentication.RegistrationResponse;
import started.local.startedjava.mapper.authentication.UserMapper;
import started.local.startedjava.repository.authentication.UserRepository;
import started.local.startedjava.service.authentication.VerificationService;


@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@CrossOrigin(AppConstant.FRONTEND_HOST)
@Slf4j
public class AuthController {
    private AuthenticationManager authenticationManager;
    private VerificationService verificationService;
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
        verificationService.confirmVerification(registration);
        return ResponseEntity.status(HttpStatus.OK).body(new ObjectNode(JsonNodeFactory.instance));
    }
}
