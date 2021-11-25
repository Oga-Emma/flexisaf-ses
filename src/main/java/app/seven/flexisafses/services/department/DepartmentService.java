package app.seven.flexisafses.services.department;

import app.seven.flexisafses.models.exception.NotFoundException;
import app.seven.flexisafses.models.pojo.Department;

import java.util.List;
import java.util.Optional;

public interface DepartmentService {
    Optional<Department> getDepartmentByName(String name) throws NotFoundException;
    Department getDepartmentById(String id) throws NotFoundException;
    Department createDepartment(Department request);
    Department updateDepartment(Department request);
    void deleteDepartment(Department request);

    List<Department> fetchDepartments();
}
