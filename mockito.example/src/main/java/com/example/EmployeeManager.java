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
		for (Employee employee : employees) {
			bankService.pay(employee.getId(), employee.getSalary());
			employee.setPaid(true);
		}
		return employees.size();
	}

}
