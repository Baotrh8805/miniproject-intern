package com.suntraining.employeemanagement.repository;

import com.suntraining.employeemanagement.dto.DepartmentStatistic;
import com.suntraining.employeemanagement.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    List<Employee> findByNameContainingIgnoreCase(String name);

    List<Employee> findByDepartmentId(Long departmentId);

    List<Employee> findByNameContainingIgnoreCaseAndDepartmentId(String name, Long departmentId);

    boolean existsByEmployeeCode(String employeeCode);

    // Module 10: Thống kê số lượng nhân viên và mức lương theo từng phòng ban bằng @Query JPQL
    @Query("SELECT new com.suntraining.employeemanagement.dto.DepartmentStatistic(" +
           "d.id, d.code, d.name, COUNT(e.id), COALESCE(AVG(e.salary), 0.0), COALESCE(SUM(e.salary), 0.0)) " +
           "FROM Department d LEFT JOIN d.employees e " +
           "GROUP BY d.id, d.code, d.name " +
           "ORDER BY COUNT(e.id) DESC")
    List<DepartmentStatistic> getDepartmentStatistics();

    // Module 10: Thống kê tổng số nhân viên trong toàn hệ thống
    @Query("SELECT COUNT(e) FROM Employee e")
    long countTotalEmployees();

    // Module 10: Thống kê mức lương trung bình toàn công ty
    @Query("SELECT COALESCE(AVG(e.salary), 0.0) FROM Employee e")
    Double getAverageSalary();

    // Module 10: Thống kê mức lương cao nhất
    @Query("SELECT COALESCE(MAX(e.salary), 0.0) FROM Employee e")
    Double getMaxSalary();

    // Module 10: Thống kê mức lương thấp nhất
    @Query("SELECT COALESCE(MIN(e.salary), 0.0) FROM Employee e")
    Double getMinSalary();

    // Module 10: Thống kê tổng quỹ lương toàn công ty
    @Query("SELECT COALESCE(SUM(e.salary), 0.0) FROM Employee e")
    Double getTotalPayroll();
}
