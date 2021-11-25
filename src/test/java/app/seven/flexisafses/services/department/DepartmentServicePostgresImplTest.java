package app.seven.flexisafses.services.department;

import app.seven.flexisafses.models.exception.NotFoundException;
import app.seven.flexisafses.models.pojo.Department;
import app.seven.flexisafses.repositories.DepartmentRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class DepartmentServicePostgresImplTest {
    @Mock
    private DepartmentRepo departmentRepo;

    private DepartmentService departmentService;
//    AutoCloseable autoCloseable;

    Department testDepartment;
    @BeforeEach
    public void setUp() {
        testDepartment = Department.builder()
                .id(UUID.randomUUID().toString())
                .name("Computer Science")
                .build();
        departmentService = new DepartmentServicePostgresImpl(departmentRepo);
    }

    @Test
    void getDepartmentByName() throws NotFoundException {
        String name = "Computer Science";
        // When
        departmentService.getDepartmentByName(name);

        //then
        verify(departmentRepo).findByName(name);
    }

    @Test
    void getDepartmentById() throws NotFoundException {
        String id = UUID.randomUUID().toString();

        given(departmentRepo.findById(id)).willReturn(Optional.of(testDepartment));

        // When
        departmentService.getDepartmentById(id);

        //then
        verify(departmentRepo).findById(id);
    }

    @Test
    void createDepartment() {
        given(departmentRepo.save(testDepartment)).willReturn(testDepartment);

        // When
        departmentService.createDepartment(testDepartment);

        //then
        verify(departmentRepo).save(testDepartment);
    }

    @Test
    void updateDepartment() {
        given(departmentRepo.save(testDepartment)).willReturn(testDepartment);

        // When
        departmentService.updateDepartment(testDepartment);

        //then
        verify(departmentRepo).save(testDepartment);
    }

    @Test
    void deleteDepartment() {given(departmentRepo.save(testDepartment)).willReturn(testDepartment);

        // When
        departmentService.updateDepartment(testDepartment);

        //then
        verify(departmentRepo).save(testDepartment);
    }

    @Test
    void fetchDepartments() {
        given(departmentRepo.findAll()).willReturn(List.of(testDepartment));

        // When
        List<Department> departments = departmentService.fetchDepartments();

        //then
        assertThat(departments).contains(testDepartment);
        verify(departmentRepo).findAll();
    }
}
