package com.example;

public class EmployeeManager {

	private EmployeeRepository employeeRepository;
	private BankService bankService;

	public EmployeeManager(EmployeeRepository employeeRepository, BankService bankService) {
		this.employeeRepository = employeeRepository;
		this.bankService = bankService;
	}

	public int payEmployees() {
		return employeeRepository.findAll().size();
	}

}
