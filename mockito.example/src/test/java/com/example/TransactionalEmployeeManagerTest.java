package com.example;

import static java.util.Arrays.asList;
import static org.mockito.AdditionalAnswers.answer;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

public class TransactionalEmployeeManagerTest {

	private TransactionalEmployeeManager employeeManager;

	private TransactionManager transactionManager;

	private EmployeeRepository employeeRepository;

	private BankService bankService;

	@Before
	public void setup() {
		employeeRepository = mock(EmployeeRepository.class);
		transactionManager = mock(TransactionManager.class);
		// make sure the lambda passed to the TransactionManager
		// is executed, using the mock repository
		when(transactionManager.doInTransaction(any()))
			.thenAnswer(
				answer((TransactionCode<?> code) -> code.apply(employeeRepository)));
		bankService = mock(BankService.class);
		employeeManager = new TransactionalEmployeeManager(transactionManager, bankService);
	}

	@Test
	public void testPayEmployeesWhenSeveralEmployeesArePresent() {
		Employee employee1 = new Employee("1", 1000);
		Employee employee2 = new Employee("2", 2000);
		when(employeeRepository.findAll())
			.thenReturn(asList(employee1, employee2));
		employeeManager.payEmployees();
		verify(bankService).pay("2", 2000);
		verify(bankService).pay("1", 1000);
		verify(employeeRepository).save(employee1);
		verify(employeeRepository).save(employee2);
		// also verify that a single transaction is executed
		verify(transactionManager, times(1)).doInTransaction(any());
	}

}