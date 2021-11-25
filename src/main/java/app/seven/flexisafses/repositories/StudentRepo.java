package app.seven.flexisafses.repositories;

import app.seven.flexisafses.models.pojo.AppUser;
import app.seven.flexisafses.models.pojo.Gender;
import app.seven.flexisafses.models.pojo.Student;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Repository
public interface StudentRepo extends MongoRepository<Student, String> {
    @Query("{'$expr': {'$and': [{'$eq': [{'$dayOfMonth': '$dateOfBirth'}, ?1]}, {'$eq': [{'$month': '$dateOfBirth'}, ?0]}]}}")
//    @Query("{'$expr': {'$eq': [{'$month': '$dateOfBirth'}, 03]}}")
//    @Query("{'$expr': {'$eq': [{'$dayOfMonth': '$dateOfBirth'}, 24]}}")
    List<Student> findByBirthday(int month, int day);

    List<Student> findAllByDateOfBirthMonth(Month dateOfBirth_monthValue);
    List<Student> findAllByDateOfBirthDayOfMonth(int dateOfBirth_monthValue);
}
