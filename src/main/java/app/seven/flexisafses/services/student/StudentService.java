package app.seven.flexisafses.services.student;

import app.seven.flexisafses.models.exception.BadRequestException;
import app.seven.flexisafses.models.exception.NotFoundException;
import app.seven.flexisafses.models.pojo.AppUser;
import app.seven.flexisafses.models.pojo.Department;
import app.seven.flexisafses.models.pojo.Gender;
import app.seven.flexisafses.models.pojo.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface StudentService {
    Student createStudent(Student student);
    Student updateStudent(Student student);
    Page<Student> queryStudent(String firstName, String lastName, String otherName, String fullName, Gender gender,
                               Department department, AppUser createdBy, List<LocalDate> createdAtRange, Pageable pageable) throws BadRequestException;
    void delete(Student student);

    Long count();

    Student getStudent(String studentId) throws NotFoundException;

    List<Student> getDueBirthdays(LocalDate localDate);
}
