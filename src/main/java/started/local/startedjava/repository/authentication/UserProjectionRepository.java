package started.local.startedjava.repository.authentication;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import started.local.startedjava.dto.request.statistic.StatisticResource;
import started.local.startedjava.entity.authentication.User;

import java.time.Instant;
import java.util.List;

@Repository
@AllArgsConstructor
public class UserProjectionRepository {
    private EntityManager em;

    public List<StatisticResource> getUserCountByCreateDate() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<StatisticResource> query = cb.createQuery(StatisticResource.class);

        Root<User> user = query.from(User.class);
        query.select(cb.construct(StatisticResource.class, user.get("createdAt").as(Instant.class), cb.count(user.get("id"))));
        query.groupBy(user.get("createdAt").as(Instant.class));
        query.orderBy(cb.asc(user.get("createdAt")));

        return em.createQuery(query).getResultList();
    }
}
