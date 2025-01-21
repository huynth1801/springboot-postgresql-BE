package started.local.startedjava.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import started.local.startedjava.entity.InvalidatedToken;

public interface InvalidatedTokenRepository extends MongoRepository<InvalidatedToken, String> {
}
