package started.local.startedjava.utils;

import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import started.local.startedjava.entity.BaseEntity;
import started.local.startedjava.entity.authentication.User;
import started.local.startedjava.repository.authentication.RoleRepository;
import started.local.startedjava.repository.authentication.UserRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class MapperUtils {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;

    public User mapToUser(Long id) {
        return userRepository.getById(id);
    }

    @Named("hashPassword")
    public String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }

    @AfterMapping
    @Named("attachUser")
    public void attachUser(@MappingTarget User user) {
        user.setRoles(attachSet(user.getRoles(), roleRepository));
    }


    private <E extends BaseEntity> Set<E> attachSet(Set<E> entities, JpaRepository<E, Long> repository) {
        Set<E> detachedSet = Optional.ofNullable(entities).orElseGet(HashSet::new);
        Set<E> attachedSet = new HashSet<>();

        for (E entity : detachedSet) {
            if (entity.getId() != null) {
                repository.findById(entity.getId()).ifPresent(attachedSet::add);
            } else {
                attachedSet.add(entity);
            }
        }

        return attachedSet;
    }

}
