package diploma.repo;

import diploma.domain.tennis.TennisMatch;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TennisRepo extends MongoRepository<TennisMatch,String> {
}
