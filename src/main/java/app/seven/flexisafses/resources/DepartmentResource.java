package app.seven.flexisafses.resources;

import app.seven.flexisafses.models.dto.DepartmentRequestDto;
import app.seven.flexisafses.models.exception.BadRequestException;
import app.seven.flexisafses.models.exception.NotFoundException;
import app.seven.flexisafses.models.pojo.Department;
import app.seven.flexisafses.services.department.DepartmentService;
import app.seven.flexisafses.util.response.SuccessResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequestMapping("/departments")
@RestController
public class DepartmentResource {

    private final DepartmentService departmentService;

    public DepartmentResource(@Autowired DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping
    public ResponseEntity<SuccessResponse<List<Department>>> fetchDepartment(){
        return ResponseEntity.ok(new SuccessResponse<>("Departments", departmentService.fetchDepartments()));
    }

    @PostMapping
    public ResponseEntity<SuccessResponse<Department>> createDepartment(@RequestBody DepartmentRequestDto request) throws BadRequestException, NotFoundException {
        Optional<Department> dept = departmentService.getDepartmentByName(request.getName());

        if(dept.isPresent()){
            throw new BadRequestException("Department with name " + request.getName() + " already exist");
        }

        Department department = Department
                .builder()
                .name(request.getName())
                .build();

        return new ResponseEntity<SuccessResponse<Department>>(new SuccessResponse<>("Departments", departmentService.createDepartment(department)), HttpStatus.CREATED);
    }

    @PatchMapping("/{departmentId}")
    public ResponseEntity<SuccessResponse<Department>> updateDepartment(@RequestBody DepartmentRequestDto request, @PathVariable String departmentId) throws BadRequestException, NotFoundException {

        Department dept = departmentService.getDepartmentById(departmentId);

        if(dept == null){
            throw new NotFoundException("Department with id " + departmentId + " not found");
        }
        dept.setName(request.getName());

        return ResponseEntity.ok(new SuccessResponse<>("Departments", departmentService.updateDepartment(dept)));
    }
}
