package com.example;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class EmployeeManagerTest {

	private EmployeeManager employeeManager;

	private EmployeeRepository employeeRepository;

	private List<Employee> employees;

	@Before
	public void setup() {
		employees = new ArrayList<>();
		employeeRepository = mock(EmployeeRepository.class);
		when(employeeRepository.findAll()).thenReturn(employees);
		employeeManager = new EmployeeManager(employeeRepository);
	}

	@Test
	public void testPayEmployeesWhenNoEmployeesArePresent() {
		int numberOfPayments = employeeManager.payEmployees();
		assertThat(numberOfPayments).isEqualTo(0);
	}

	@Test
	public void testPayEmployeesWhenOneEmployeeIsPresent() {
		// why do we add something to this list?!
		employees.add(new Employee("1", 1000));
		assertThat(employeeManager.payEmployees()).isEqualTo(1);
	}
}