package app.seven.flexisafses.repositories;

import app.seven.flexisafses.models.pojo.AppUser;
import app.seven.flexisafses.models.pojo.Gender;
import app.seven.flexisafses.models.pojo.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepo extends MongoRepository<Student, String> {
}
