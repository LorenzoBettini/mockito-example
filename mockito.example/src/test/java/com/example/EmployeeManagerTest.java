package com.example;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.mockito.ArgumentMatchers.argThat;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.AdditionalAnswers.answer;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;

public class EmployeeManagerTest {

	private EmployeeManager employeeManager;

	private EmployeeRepository employeeRepository;

	private BankService bankService;

	@Before
	public void setup() {
		employeeRepository = mock(EmployeeRepository.class);
		bankService = mock(BankService.class);
		employeeManager = new EmployeeManager(employeeRepository, bankService);
	}

	@Test
	public void testPayEmployeesWhenNoEmployeesArePresent() {
		when(employeeRepository.findAll())
			.thenReturn(emptyList());
		assertThat(employeeManager.payEmployees())
			.isZero();
	}

	@Test
	public void testPayEmployeesWhenOneEmployeeIsPresent() {
		when(employeeRepository.findAll())
			.thenReturn(asList(new Employee("1", 1000)));
		assertThat(employeeManager.payEmployees()).isEqualTo(1);
		verify(bankService).pay("1", 1000);
	}

	@Test
	public void testPayEmployeesWhenSeveralEmployeesArePresent() {
		when(employeeRepository.findAll())
			.thenReturn(asList(
					new Employee("1", 1000),
					new Employee("2", 2000)));
		assertThat(employeeManager.payEmployees()).isEqualTo(2);
		verify(bankService).pay("2", 2000);
		verify(bankService).pay("1", 1000);
		verifyNoMoreInteractions(bankService);
	}

	@Test
	public void testPayEmployeesInOrderWhenSeveralEmployeeArePresent() {
		// an example of invocation order verification
		when(employeeRepository.findAll())
				.thenReturn(asList(
						new Employee("1", 1000),
						new Employee("2", 2000)));
		assertThat(employeeManager.payEmployees()).isEqualTo(2);
		InOrder inOrder = inOrder(bankService);
		inOrder.verify(bankService).pay("1", 1000);
		inOrder.verify(bankService).pay("2", 2000);
		verifyNoMoreInteractions(bankService);
	}

	@Test
	public void testExampleOfInOrderWithTwoMocks() {
		// Just an example of invocation order verification on several mocks
		when(employeeRepository.findAll())
				.thenReturn(asList(
						new Employee("1", 1000),
						new Employee("2", 2000)));
		assertThat(employeeManager.payEmployees()).isEqualTo(2);
		InOrder inOrder = inOrder(bankService, employeeRepository);
		inOrder.verify(employeeRepository).findAll();
		inOrder.verify(bankService).pay("1", 1000);
		inOrder.verify(bankService).pay("2", 2000);
		verifyNoMoreInteractions(bankService);
	}

	@Test
	public void testExampleOfArgumentCaptor() {
		// Just an example of ArgumentCaptor
		when(employeeRepository.findAll())
				.thenReturn(asList(
						new Employee("1", 1000),
						new Employee("2", 2000)));
		assertThat(employeeManager.payEmployees()).isEqualTo(2);
		ArgumentCaptor<String> idCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<Double> amountCaptor = ArgumentCaptor.forClass(Double.class);
		verify(bankService, times(2))
			.pay(idCaptor.capture(), amountCaptor.capture());
		assertThat(idCaptor.getAllValues()).containsExactly("1", "2");
		assertThat(amountCaptor.getAllValues()).containsExactly(1000.0, 2000.0);
		verifyNoMoreInteractions(bankService);
	}

	@Test
	public void testEmployeeSetPaidIsCalledAfterPaying() {
		Employee employee = spy(new Employee("1", 1000));
		when(employeeRepository.findAll())
				.thenReturn(asList(employee));
		assertThat(employeeManager.payEmployees()).isEqualTo(1);
		InOrder inOrder = inOrder(bankService, employee);
		inOrder.verify(bankService).pay("1", 1000);
		inOrder.verify(employee).setPaid(true);
	}

	@Test
	public void testPayEmployeesWhenBankServiceThrowsException() {
		Employee employee = spy(new Employee("1", 1000));
		when(employeeRepository.findAll())
				.thenReturn(asList(employee));
		doThrow(new RuntimeException())
				.when(bankService).pay(anyString(), anyDouble());
		// number of payments must be 0
		assertThat(employeeManager.payEmployees()).isZero();
		// make sure that Employee.paid is updated accordingly
		verify(employee).setPaid(false);
	}

	@Test
	public void testOtherEmployeesArePaidWhenBankServiceThrowsException() {
		Employee notToBePaid = spy(new Employee("1", 1000));
		Employee toBePaid = spy(new Employee("2", 2000));
		when(employeeRepository.findAll())
			.thenReturn(asList(notToBePaid, toBePaid));
		doThrow(new RuntimeException())
			.doNothing()
			.when(bankService).pay(anyString(), anyDouble());
		// number of payments must be 1
		assertThat(employeeManager.payEmployees()).isEqualTo(1);
		// make sure that Employee.paid is updated accordingly
		verify(notToBePaid).setPaid(false);
		verify(toBePaid).setPaid(true);
	}

	@Test
	public void testArgumentMatcherExample() {
		// equivalent to the previous test, with argument matcher
		Employee notToBePaid = spy(new Employee("1", 1000));
		Employee toBePaid = spy(new Employee("2", 2000));
		when(employeeRepository.findAll())
			.thenReturn(asList(notToBePaid, toBePaid));
		doThrow(new RuntimeException())
			.when(bankService).pay(
					argThat(s -> s.equals("1")),
					anyDouble());
		// number of payments must be 1
		assertThat(employeeManager.payEmployees()).isEqualTo(1);
		// make sure that Employee.paid is updated accordingly
		verify(notToBePaid).setPaid(false);
		verify(toBePaid).setPaid(true);
	}

	@Test
	public void testDoAnswerExample() {
		// equivalent to the previous test, with Answer
		Employee notToBePaid = spy(new Employee("1", 1000));
		Employee toBePaid = spy(new Employee("2", 2000));
		when(employeeRepository.findAll())
			.thenReturn(asList(notToBePaid, toBePaid));
		doAnswer(invocation -> {
				if (invocation.getArgument(0, String.class).equals("1"))
					throw new RuntimeException();
				return null;
			}).when(bankService).pay(anyString(), anyDouble());
		// number of payments must be 1
		assertThat(employeeManager.payEmployees()).isEqualTo(1);
		// make sure that Employee.paid is updated accordingly
		verify(notToBePaid).setPaid(false);
		verify(toBePaid).setPaid(true);
	}

	@Test
	public void testDoAnswer2Example() {
		// equivalent to the previous test, with Answer2
		Employee notToBePaid = spy(new Employee("1", 1000));
		Employee toBePaid = spy(new Employee("2", 2000));
		when(employeeRepository.findAll())
			.thenReturn(asList(notToBePaid, toBePaid));
		doAnswer(answer((String id, Double amount) -> {
				if (id.equals("1"))
					throw new RuntimeException();
				return null;
			})).when(bankService).pay(anyString(), anyDouble());
		// number of payments must be 1
		assertThat(employeeManager.payEmployees()).isEqualTo(1);
		// make sure that Employee.paid is updated accordingly
		verify(notToBePaid).setPaid(false);
		verify(toBePaid).setPaid(true);
	}
}