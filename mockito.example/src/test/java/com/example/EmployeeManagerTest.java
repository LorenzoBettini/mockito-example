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

	@Before
	public void setup() {
		employeeRepository = mock(EmployeeRepository.class);
		employeeManager = new EmployeeManager(employeeRepository);
	}

	@Test
	public void testPayEmployeesWhenNoEmployeesArePresent() {
		int numberOfPayments = employeeManager.payEmployees();
		assertThat(numberOfPayments).isEqualTo(0);
	}

	@Test
	public void testPayEmployeesWhenOneEmployeeIsPresent() {
		List<Employee> employees = new ArrayList<>();
		employees.add(new Employee("1", 1000));
		when(employeeRepository.findAll()).thenReturn(employees);
		assertThat(employeeManager.payEmployees()).isEqualTo(1);
	}
}