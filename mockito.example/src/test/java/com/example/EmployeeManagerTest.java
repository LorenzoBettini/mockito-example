package com.example;

import static org.assertj.core.api.Assertions.*;

import org.junit.Test;

public class EmployeeManagerTest {

	@Test
	public void testPayEmployeesWhenNoEmployeesArePresent() {
		EmployeeManager employeeManager = new EmployeeManager();
		int numberOfPayments = employeeManager.payEmployees();
		assertThat(numberOfPayments).isEqualTo(0);
	}

}