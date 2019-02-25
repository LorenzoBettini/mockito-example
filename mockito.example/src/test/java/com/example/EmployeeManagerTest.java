package com.example;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

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
		assertThat(employeeManager.payEmployees()).isEqualTo(1);
	}
}