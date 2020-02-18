package com.example;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * Test doubles that are "fakes" must be tested
 * 
 * @author Lorenzo Bettini
 *
 */
public class EmployeeInMemoryRepositoryTest {

	private EmployeeInMemoryRepository employeeRepository;

	private List<Employee> employees;

	@Before
	public void setup() {
		employees = new ArrayList<>();
		employeeRepository = new EmployeeInMemoryRepository(employees);
	}

	@Test
	public void testFindAll() {
		Employee employee1 = new Employee("1", 1000);
		Employee employee2 = new Employee("2", 2000);
		employees.addAll(asList(employee1, employee2));
		assertThat(employeeRepository.findAll())
			.containsExactly(employee1, employee2);
	}

	@Test
	public void testSaveNewEmployee() {
		Employee saved = employeeRepository.save(new Employee("1", 1000));
		assertThat(employees)
			.containsExactly(saved);
	}

	@Test
	public void testSaveExistingEmployee() {
		Employee employee1 = new Employee("1", 1000);
		Employee employee2 = new Employee("2", 2000);
		employees.addAll(asList(employee1, employee2));
		Employee saved = employeeRepository.save(new Employee("2", 3000));
		assertThat(employees)
			.containsExactly(employee1, saved);
	}
}
