package com.example;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
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
		when(employeeRepository.findAll())
			.thenReturn(emptyList());
		assertThat(employeeManager.payEmployees())
			.isEqualTo(0);
	}

	@Test
	public void testPayEmployeesWhenOneEmployeeIsPresent() {
		when(employeeRepository.findAll())
			.thenReturn(asList(new Employee("1", 1000)));
		assertThat(employeeManager.payEmployees()).isEqualTo(1);
	}
}