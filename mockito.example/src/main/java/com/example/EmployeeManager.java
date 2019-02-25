package com.example;

import java.util.List;

public class EmployeeManager {

	private EmployeeRepository employeeRepository;
	private BankService bankService;

	public EmployeeManager(EmployeeRepository employeeRepository, BankService bankService) {
		this.employeeRepository = employeeRepository;
		this.bankService = bankService;
	}

	public int payEmployees() {
		List<Employee> employees = employeeRepository.findAll();
		if (!employees.isEmpty()) {
			Employee employee = employees.get(0);
			bankService.pay(employee.getId(), employee.getSalary());
		}
		return employees.size();
	}

}
