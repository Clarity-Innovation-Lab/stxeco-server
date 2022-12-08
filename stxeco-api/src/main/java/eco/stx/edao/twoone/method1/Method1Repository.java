package eco.stx.edao.twoone.method1;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Method1Repository extends MongoRepository<Method1Vote, String> {


}
