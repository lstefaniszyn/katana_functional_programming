package com.functional.repositories;

import java.util.List;

import com.functional.models.Employee;

import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository {
    
    public List<Employee> getAll();
    
}
