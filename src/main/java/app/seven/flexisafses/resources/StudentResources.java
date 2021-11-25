package app.seven.flexisafses.resources;

import app.seven.flexisafses.models.dto.StudentRequestDto;
import app.seven.flexisafses.models.exception.BadRequestException;
import app.seven.flexisafses.models.exception.NotFoundException;
import app.seven.flexisafses.models.pojo.AppUser;
import app.seven.flexisafses.models.pojo.Department;
import app.seven.flexisafses.models.pojo.Gender;
import app.seven.flexisafses.models.pojo.Student;
import app.seven.flexisafses.services.auth_user.AuthUserService;
import app.seven.flexisafses.services.department.DepartmentService;
import app.seven.flexisafses.services.student.StudentService;
import app.seven.flexisafses.util.response.SuccessResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequestMapping("/students")
@RestController
public class StudentResources {
    private final AuthUserService authUserService;
    private final DepartmentService departmentService;
    private final StudentService studentService;

    public StudentResources(@Autowired AuthUserService authUserService, @Autowired DepartmentService departmentService,
                            @Autowired  StudentService studentService) {
        this.authUserService = authUserService;
        this.departmentService = departmentService;
        this.studentService = studentService;
    }

    @GetMapping
    public ResponseEntity<SuccessResponse> fetchStudents(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int perPage,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String otherName, String fullName,
            @RequestParam(required = false) Gender gender,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String createdBy,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate
            ) throws NotFoundException, BadRequestException {



        Department dept = null;
        if(department != null){
            Optional<Department> result = departmentService.getDepartmentByName(department);

            if(result.isEmpty()){
                throw new NotFoundException("No department with name " + department + " found");
            }
            dept = result.get();
        }

        AppUser usr = null;
        if(createdBy != null){
            usr = authUserService.getUser(createdBy);
        }

        Pageable pageable = PageRequest.of(page - 1, perPage);


        return new ResponseEntity<>(new SuccessResponse("Students found",

                studentService.queryStudent(
                        firstName,
                        lastName,
                        otherName,
                        fullName,
                        gender,
                        dept,
                        usr,
                        Arrays.asList(startDate,
                                endDate),
                        pageable
                )
                ), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<SuccessResponse> createStudent(
            @RequestBody StudentRequestDto request, Principal principal) throws BadRequestException, NotFoundException {


        Optional<Department> departmentResult = departmentService.getDepartmentByName(request.department);

        if(departmentResult.isEmpty()){
            throw new NotFoundException("No department with name " + request.department + " found");
        }

        Long count = studentService.count();
        String matricNumber = "FLEXISAF/" + String.format("%03d", count.intValue() + 1);

        int yearBetween = LocalDate.now().getYear() - request.dateOfBirth.getYear();
        if(yearBetween < 18 || yearBetween > 30){
            throw new BadRequestException("A student can not be less than 18 or more than 30 years");
        }

        Student usr = Student.builder()
                .email(request.email)
                .firstName(request.firstName)
                .lastName(request.lastName)
                .otherName(request.otherName)
                .dateOfBirth(request.dateOfBirth)
                .gender(request.gender)
                .matricNumber(matricNumber)
                .department(departmentResult.get())
                .createdAt(LocalDate.now())
                .createdBy(authUserService.getUser(principal.getName()))
                .build();

        return new ResponseEntity<>(new SuccessResponse("Student created",
                studentService.createStudent(usr)), HttpStatus.CREATED);
    }

    @PatchMapping("/{studentId}")
    public ResponseEntity<SuccessResponse> updateStudent(@RequestBody StudentRequestDto request, Principal principal, @PathVariable String studentId) throws BadRequestException, NotFoundException {


        Long count = studentService.count();
        String matricNumber = "FLEXISAF/" + String.format("%03d", count.intValue() + 1);

        Student student = studentService.getStudent(studentId);

        if(request.email != null) student.setFirstName(request.email);
        if(request.firstName != null) student.setFirstName(request.firstName);
        if(request.lastName != null ) student.setLastName(request.lastName);
        if(request.otherName != null ) student.setOtherName(request.otherName);
        if(request.gender != null ) student.setGender(request.gender);

        if(request.dateOfBirth != null ) {

            int yearBetween = LocalDate.now().getYear() - request.dateOfBirth.getYear();
            if(yearBetween < 18 || yearBetween > 30){
                throw new BadRequestException("A student can not be less than 18 or more than 30 years");
            }

            student.setDateOfBirth(request.dateOfBirth);
        }

        if(request.department != null) {
            Optional<Department> departmentResult = departmentService.getDepartmentByName(request.department);

            if(departmentResult.isEmpty()){
                throw new NotFoundException("No department with name " + request.department + " found");
            }
            student.setDepartment(departmentResult.get());
        }

        return new ResponseEntity<>(new SuccessResponse("Student updated",
                studentService.updateStudent(student)), HttpStatus.OK);
    }

    @DeleteMapping("/{studentId}")
    public ResponseEntity<SuccessResponse> deleteStudent(@PathVariable String studentId) throws NotFoundException {
        studentService.delete(studentService.getStudent(studentId));
        return new ResponseEntity<>(new SuccessResponse("Student deleted", null), HttpStatus.OK);
    }
}
