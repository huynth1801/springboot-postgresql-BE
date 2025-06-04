package started.local.startedjava.service.authentication;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import started.local.startedjava.constant.FieldName;
import started.local.startedjava.constant.ResourceName;
import started.local.startedjava.entity.authentication.RefreshToken;
import started.local.startedjava.entity.authentication.User;
import started.local.startedjava.exception.ResourceNotFoundException;
import started.local.startedjava.repository.authentication.RefreshTokenRepository;
import started.local.startedjava.repository.authentication.UserRepository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    @Value("${jwt.expiration.refreshTime}")
    private int jwtRefreshExpirationTime;

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken createRefreshToken(Authentication authentication) {
        String username = authentication.getName();
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUser(userRepository.findByUsername(username)
                        .orElseThrow(() -> new ResourceNotFoundException(ResourceName.USER, FieldName.USERNAME, username)));
        refreshToken.setExpiryDate(Instant.now().plusSeconds(jwtRefreshExpirationTime));
        refreshToken.setToken(UUID.randomUUID().toString());

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken refreshToken) {
        if(refreshToken.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(refreshToken);
        }

        return refreshToken;
    }
}
