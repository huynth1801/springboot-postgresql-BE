package started.local.startedjava.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import started.local.startedjava.dto.request.AuthenticationRequest;
import started.local.startedjava.dto.request.UserCreationRequest;
import started.local.startedjava.dto.response.UserResponse;
import started.local.startedjava.entity.ERole;
import started.local.startedjava.entity.User;
import started.local.startedjava.exception.AppException;
import started.local.startedjava.exception.ErrorCode;
import started.local.startedjava.mapper.UserMapper;
import started.local.startedjava.repository.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    // Đăng ký người dùng
    public UserResponse registerUser(UserCreationRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already in use");
        }
        if(userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        Set<String> roles = new HashSet<>();
        roles.add(ERole.ROLE_USER.name());

        user.setRoles(roles);

        userRepository.save(user);
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(user.getRoles())
                .build();
    }

    // Get all users
    public List<UserResponse> getUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getRoles()
                ))
                .toList();
    }

    public UserResponse getUser(String id) {
        return userMapper.toUserResponse(userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }

    // Authenticate
    public boolean authenticate(AuthenticationRequest request) {
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_EXISTED));

        return passwordEncoder.matches(request.getPassword(), user.getPassword());
    }
}
