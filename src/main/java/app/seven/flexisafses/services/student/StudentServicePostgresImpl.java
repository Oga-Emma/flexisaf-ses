package app.seven.flexisafses.services.student;

import app.seven.flexisafses.models.exception.BadRequestException;
import app.seven.flexisafses.models.exception.NotFoundException;
import app.seven.flexisafses.models.pojo.AppUser;
import app.seven.flexisafses.models.pojo.Department;
import app.seven.flexisafses.models.pojo.Gender;
import app.seven.flexisafses.models.pojo.Student;
import app.seven.flexisafses.repositories.StudentRepo;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class StudentServicePostgresImpl implements StudentService{
    private final StudentRepo studentRepo;
    private final MongoTemplate mongoTemplate;

    public StudentServicePostgresImpl(@Autowired StudentRepo studentRepo, @Autowired MongoTemplate mongoTemplate){
        this.studentRepo = studentRepo;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Student createStudent(Student student) {
        return studentRepo.save(student);
    }

    @Override
    public Student updateStudent(Student student) {
        return studentRepo.save(student);
    }

    @Override
    public Page<Student> queryStudent(String firstName, String lastName, String otherName, String fullName, Gender gender,
                                      Department department, AppUser createdBy, List<LocalDate> createdAtRange, Pageable pageable) throws BadRequestException {

        Query query = new Query().with(pageable);

        if(fullName != null){
            String[] split = fullName.split(" ");
            if(split.length != 3){
                throw new BadRequestException("Full name must be in the correct format (i.e firstname lastname othername");
            }

            firstName = split[0];
            lastName = split[1];
            otherName = split[2];
        }

        if(firstName != null){
            query.addCriteria(Criteria.where("firstName").regex("(?i).*" + firstName + ".*"));
        }

        if(lastName != null){
            query.addCriteria(Criteria.where("lastName").regex("(?i).*" + lastName + ".*"));
        }

        if(otherName != null){
            query.addCriteria(Criteria.where("otherName").regex("(?i).*" + otherName + ".*"));
        }

        if(department != null){
            query.addCriteria(Criteria.where("department").is(department));
        }

        if(createdBy != null){
            query.addCriteria(Criteria.where("createdBy").is(createdBy));
        }

        if(createdAtRange != null){
            if(createdAtRange.size() != 2){
                throw new BadRequestException("CreatedAtRange must be an array of two dates");
            }

            query.addCriteria(Criteria.where("createdAt").gte(createdAtRange.get(0)).lt(createdAtRange.get(1)));
        }

//
//        log.debug(query.toString());
//        log.debug(pageable.toString());

        List<Student> result = mongoTemplate.find(query, Student.class);
        long count = mongoTemplate.count(query, Student.class);

        return new PageImpl<Student>(result , pageable, count);
    }

    @Override
    public void delete(Student student) {
        studentRepo.delete(student);
    }

    @Override
    public Long count() {
        return studentRepo.count();
    }

    @Override
    public Student getStudent(String studentId) throws NotFoundException {
        Optional<Student> result =  studentRepo.findById(studentId);
        if(result.isEmpty()){
            throw new NotFoundException("No student with id " + studentId + " found");
        }

        return result.get();
    }
}
