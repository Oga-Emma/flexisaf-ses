package app.seven.flexisafses.services.department;

import app.seven.flexisafses.models.exception.NotFoundException;
import app.seven.flexisafses.models.pojo.Department;
import app.seven.flexisafses.repositories.DepartmentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentServicePostgresImpl implements DepartmentService {
    private final DepartmentRepo departmentRepo;

    DepartmentServicePostgresImpl(@Autowired DepartmentRepo departmentRepo){
        this.departmentRepo = departmentRepo;
    }

    @Override
    public Optional<Department> getDepartmentByName(String name) throws NotFoundException {
        return departmentRepo.findByName(name);
    }

    @Override
    public Department getDepartmentById(String id) throws NotFoundException {
        Optional<Department> result = departmentRepo.findById(id);
        if(result.isEmpty()){
            throw new NotFoundException("No department with id " + id + " found");
        }
        return result.get();
    }

    @Override
    public Department createDepartment(Department request) {
        return departmentRepo.save(request);
    }

    @Override
    public Department updateDepartment(Department request) {
        return departmentRepo.save(request);
    }

    @Override
    public void deleteDepartment(Department request) {
        departmentRepo.delete(request);
    }

    @Override
    public List<Department> fetchDepartments() {
        return departmentRepo.findAll();
    }
}
