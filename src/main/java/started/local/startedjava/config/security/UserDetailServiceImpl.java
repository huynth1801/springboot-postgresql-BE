package started.local.startedjava.config.security;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import started.local.startedjava.entity.authentication.User;
import started.local.startedjava.repository.authentication.UserRepository;

@Service
@AllArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private UserRepository userRepository;

    public UserDetailServiceImpl() {
        System.out.println("✅ UserDetailServiceImpl loaded");
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        // Status 1: Activated/Verified
        if(user.getStatus() != 1) {
            throw new RuntimeException("User " + username + " is not active");
        }

        return UserDetailsImpl.build(user);
    }
}
