package com.example;

import static org.assertj.core.api.Assertions.*;

import org.junit.Before;
import org.junit.Test;

public class EmployeeManagerTest {

	private EmployeeManager employeeManager;

	@Before
	public void setup() {
		employeeManager = new EmployeeManager();
	}

	@Test
	public void testPayEmployeesWhenNoEmployeesArePresent() {
		int numberOfPayments = employeeManager.payEmployees();
		assertThat(numberOfPayments).isEqualTo(0);
	}

}