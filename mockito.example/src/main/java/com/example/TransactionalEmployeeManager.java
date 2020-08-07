package com.example;

import java.util.List;

public class TransactionalEmployeeManager {

	private TransactionManager transactionManager;
	private BankService bankService;

	public TransactionalEmployeeManager(TransactionManager transactionManager, BankService bankService) {
		this.transactionManager = transactionManager;
		this.bankService = bankService;
	}

	public void payEmployees() {
		transactionManager.doInTransaction(
			employeeRepository -> {
				List<Employee> employees = employeeRepository.findAll();
				for (Employee employee : employees) {
					bankService.pay(employee.getId(), employee.getSalary());
					employee.setPaid(true);
					employeeRepository.save(employee);
				}
				return null;
			}
		);
	}

}
