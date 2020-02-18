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
		List<Employee> employees =
			transactionManager.doInTransaction(EmployeeRepository::findAll);
		for (Employee employee : employees) {
			bankService.pay(employee.getId(), employee.getSalary());
			employee.setPaid(true);
			transactionManager.doInTransaction(
				employeeRepository -> employeeRepository.save(employee)
			);
		}
	}

}
