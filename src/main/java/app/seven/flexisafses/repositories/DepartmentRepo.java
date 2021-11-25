package app.seven.flexisafses.repositories;

import app.seven.flexisafses.models.pojo.Department;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentRepo extends MongoRepository<Department, String> {
    Optional<Department> findByName(String name);
}
