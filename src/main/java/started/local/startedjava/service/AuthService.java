package started.local.startedjava.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import started.local.startedjava.dto.request.*;
import started.local.startedjava.dto.response.AuthenticationResponse;
import started.local.startedjava.dto.response.IntrospectResponse;
import started.local.startedjava.dto.response.UserResponse;
import started.local.startedjava.entity.ERole;
import started.local.startedjava.entity.Role;
import started.local.startedjava.entity.User;
import started.local.startedjava.exception.AppException;
import started.local.startedjava.exception.ErrorCode;
import started.local.startedjava.mapper.UserMapper;
import started.local.startedjava.repository.InvalidatedTokenRepository;
import started.local.startedjava.repository.RoleRepository;
import started.local.startedjava.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.swing.*;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final InvalidatedTokenRepository invalidatedTokenRepository;

    @Value("${jwt.signer.key}")
    private String signerKey;

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
        roles.add(ERole.USER.name());

//        user.setRoles(roles);

        userRepository.save(user);
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
//                .roles(user.getRoles())
                .build();
    }

    // Get all users
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<UserResponse> getUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toUserResponse).toList();
    }

    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse getUser(String id) {
        return userMapper.toUserResponse(userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }

    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        userMapper.updateUser(user, request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        var roles =  roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));

        return userMapper.toUserResponse(userRepository.save(user));
    }

    // Refresh token
//    public AuthenticationResponse refreshToken(RefreshRequest request)
//    throws ParseException, JOSEException {
//        var signedJWT = JWTParser.parse(request.getToken());
//    }


    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findByUsername(name).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED)
        );
        return userMapper.toUserResponse(user);
    }

    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }

    public IntrospectResponse introspect(IntrospectRequest request)
        throws JOSEException, ParseException {
        var token = request.getToken();

        JWSVerifier verifier  = new MACVerifier(signerKey.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);

        return IntrospectResponse.builder()
                .valid(verified && expiryTime.after(new Date()))
                .build();
    }

    // Authenticate
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_EXISTED));

        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if(!authenticated)
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        var token = generateToken(user);
        log.info(token);

        AuthenticationResponse response = AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
        log.info("Authentication response: {}", response);
        return response;

    }

    private String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("huynoob.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                ))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(signerKey.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    private SignedJWT verifyToken(String token) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(signerKey.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);

        if (!(verified && expiryTime.after(new Date())))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        if (invalidatedTokenRepository
                .existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        return signedJWT;
    }

    private String buildScope(User user){
        StringJoiner stringJoiner = new StringJoiner(" ");
        if(!CollectionUtils.isEmpty(user.getRoles())) {
            log.info(user.getRoles().toString());
            user.getRoles().forEach(role -> {
                log.info("get role names {}", role.getName());
                stringJoiner.add(role.getName());
                log.info("roles permission {}", role.getPermissions());
                if(!CollectionUtils.isEmpty(role.getPermissions())) {
                    role.getPermissions().forEach(permission -> {stringJoiner.add(permission.getName());});

                }
            });
        }
        return stringJoiner.toString();
    }

    public boolean validateTokenSignature(SignedJWT signedJWT) throws JOSEException {
        JWSVerifier verifier = new MACVerifier(signerKey);
        return signedJWT.verify(verifier);
    }

    // Check token isExpired
    public boolean isTokenExpired(JWTClaimsSet claimsSet) {
        Date expiration = claimsSet.getExpirationTime();
        return expiration != null && expiration.after(new Date());
    }
}
