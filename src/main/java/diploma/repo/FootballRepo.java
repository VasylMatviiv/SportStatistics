package diploma.repo;

import diploma.domain.football.FootballMatch;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FootballRepo extends MongoRepository<FootballMatch,String> {
}
