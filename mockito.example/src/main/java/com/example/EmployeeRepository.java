package com.example;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository {

	List<Employee> findAll();

	Optional<Employee> findById(String id);
}
