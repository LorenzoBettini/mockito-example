package com.example;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EmployeeManager {

	private static final Logger LOGGER = LogManager.getLogger(EmployeeManager.class);

	private EmployeeRepository employeeRepository;
	private BankService bankService;

	public EmployeeManager(EmployeeRepository employeeRepository, BankService bankService) {
		this.employeeRepository = employeeRepository;
		this.bankService = bankService;
	}

	public int payEmployees() {
		List<Employee> employees = employeeRepository.findAll();
		int payments = 0;
		for (Employee employee : employees) {
			try {
				bankService.pay(employee.getId(), employee.getSalary());
				employee.setPaid(true);
				payments++;
			} catch (RuntimeException e) {
				LOGGER.error("Failed payment of " + employee, e);
				employee.setPaid(false);
			}
		}
		return payments;
	}

}
