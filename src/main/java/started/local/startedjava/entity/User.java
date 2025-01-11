package started.local.startedjava.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Document("users")
public class User {
    @Id
    String id;

    @NotBlank
    @Size(max = 20)
    String username;

    @NotBlank
    @Size(max = 50)
    @Email
    String email;

    @NotBlank
    @Size(max = 120)
    String password;

    @DBRef
    List<String> roles; // Người dùng có thể có nhiều vai trò
}
