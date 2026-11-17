package com.suntraining.employeemanagement.repository;

import com.suntraining.employeemanagement.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    List<Employee> findByNameContainingIgnoreCase(String name);

    List<Employee> findByDepartmentId(Long departmentId);

    List<Employee> findByNameContainingIgnoreCaseAndDepartmentId(String name, Long departmentId);

    boolean existsByEmployeeCode(String employeeCode);
}
