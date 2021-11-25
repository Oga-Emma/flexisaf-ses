package app.seven.flexisafses.repositories;

import app.seven.flexisafses.models.pojo.AppUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface UserRepo extends MongoRepository<AppUser, String> {
    Optional<AppUser> findByUsername(String username);
}
