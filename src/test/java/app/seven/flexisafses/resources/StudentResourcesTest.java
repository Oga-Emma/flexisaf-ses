package app.seven.flexisafses.resources;

import app.seven.flexisafses.models.dto.StudentRequestDto;
import app.seven.flexisafses.models.exception.BadRequestException;
import app.seven.flexisafses.models.pojo.AppUser;
import app.seven.flexisafses.models.pojo.Department;
import app.seven.flexisafses.models.pojo.Gender;
import app.seven.flexisafses.models.pojo.Student;
import app.seven.flexisafses.services.auth_user.AuthUserService;
import app.seven.flexisafses.services.department.DepartmentService;
import app.seven.flexisafses.services.student.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.awt.print.Pageable;
import java.nio.charset.Charset;
import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = "spring.main.lazy-initialization=true")
//@WebMvcTest(DepartmentResource.class)
@AutoConfigureMockMvc()
class StudentResourcesTest {
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Mock
    StudentService studentService;

    @Mock
    DepartmentService departmentService;

    @Mock
    AuthUserService authUserService;

    @Mock
    Principal principal;

    @InjectMocks
    StudentResources studentResources;

    StudentRequestDto studentRequest;
    Student student;
    Department testDepartment;
    AppUser appUser;
    @BeforeEach
    public void setUp() {
        testDepartment = Department.builder()
                .id(UUID.randomUUID().toString())
                .name("Computer Science")
                .build();

        appUser = AppUser.builder()
                .id(UUID.randomUUID().toString())
                .role("ROLE_SUPER_ADMIN")
                .username("admin@flexisa.com")
                .build();

        studentRequest = new StudentRequestDto(
                "seven@mail.com",
                "Seven",
                "Apps",
                "Eight",
                Gender.MALE,
                LocalDate.parse("20/03/1996", DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                "Computer Science"
        );

        student = Student.builder()
                .id(UUID.randomUUID().toString())
                .email(studentRequest.email)
                .firstName(studentRequest.firstName)
                .otherName(studentRequest.lastName)
                .lastName(studentRequest.lastName)
                .dateOfBirth(studentRequest.dateOfBirth)
                .gender(studentRequest.getGender())
                .department(testDepartment)
                .createdBy(appUser)
                .build();

        mockMvc = MockMvcBuilders
                .standaloneSetup(studentResources).build();
    }

    @Test
    void fetchStudents() throws Exception {

        Page<Student> page = new PageImpl<Student>(List.of(student), PageRequest.of(1, 20), 1);
        given(studentService.queryStudent(any(), any(), any(), any(), any(), any(), any(), any(), any()))
                .willReturn(page);

        RequestBuilder request = MockMvcRequestBuilders.get("/students");
        mockMvc.perform(request).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(username = "user1", password = "pwd", roles = "USER")
    void createStudent() throws Exception {

        final Principal principal = mock(Principal.class);
        given(principal.getName()).willReturn("user");

        given(studentService.createStudent(any()))
                .willReturn(student);

        given(departmentService.getDepartmentByName(any())).willReturn(Optional.of(testDepartment));

        given(authUserService.getUser(any())).willReturn(appUser);

        RequestBuilder request = MockMvcRequestBuilders.post("/students")
                .principal(principal)
                .accept(APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentRequest));

        mockMvc.perform(request).andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    void updateStudent() throws Exception {
        given(studentService.getStudent(any()))
                .willReturn(student);

        given(studentService.updateStudent(any()))
                .willReturn(student);

        given(departmentService.getDepartmentByName(any())).willReturn(Optional.of(testDepartment));
        RequestBuilder request = MockMvcRequestBuilders.patch("/students/" + student.getId())
                .accept(APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentRequest));

        mockMvc.perform(request).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    void deleteStudent() throws Exception {
        given(studentService.getStudent(any()))
            .willReturn(student);

        RequestBuilder request = MockMvcRequestBuilders.delete("/students/" + student.getId())
                .accept(APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentRequest));

        mockMvc.perform(request).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }
}
