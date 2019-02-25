package com.example;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

public class EmployeeManagerBDDTest {

	@Mock
	private EmployeeRepository employeeRepository;

	@Mock
	private BankService bankService;

	@InjectMocks
	private EmployeeManager employeeManager;

	@Captor
	private ArgumentCaptor<String> idCaptor;

	@Captor
	private ArgumentCaptor<Double> amountCaptor;

	@Spy
	private Employee notToBePaid = new Employee("1", 1000);

	@Spy
	private Employee toBePaid = new Employee("2", 2000);

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testPayEmployeesWhenNoEmployeesArePresent() {
		given(employeeRepository.findAll())
			.willReturn(emptyList());
		assertThat(employeeManager.payEmployees()).isEqualTo(0);
	}

	@Test
	public void testPayEmployeesWhenOneEmployeeIsPresent() {
		given(employeeRepository.findAll())
			.willReturn(asList(new Employee("1", 1000)));
		assertThat(employeeManager.payEmployees()).isEqualTo(1);
		then(bankService).should().pay("1", 1000);
	}

	@Test
	public void testPayEmployeesWhenSeveralEmployeeArePresent() {
		given(employeeRepository.findAll())
			.willReturn(asList(
					new Employee("1", 1000),
					new Employee("2", 2000)));
		assertThat(employeeManager.payEmployees()).isEqualTo(2);
		then(bankService).should().pay("2", 2000);
		then(bankService).should().pay("1", 1000);
		then(bankService).shouldHaveNoMoreInteractions();
	}

	@Test
	public void testPayEmployeesInOrderWhenSeveralEmployeeArePresent() {
		// an example of invocation order verification
		given(employeeRepository.findAll())
			.willReturn(asList(
					new Employee("1", 1000),
					new Employee("2", 2000)));
		assertThat(employeeManager.payEmployees()).isEqualTo(2);
		InOrder inOrder = inOrder(bankService);
		then(bankService).should(inOrder).pay("1", 1000);
		then(bankService).should(inOrder).pay("2", 2000);
		then(bankService).shouldHaveNoMoreInteractions();
	}

	@Test
	public void testExampleOfInOrderWithTwoMocks() {
		// Just an example of invocation order verification on several mocks
		given(employeeRepository.findAll())
			.willReturn(asList(
					new Employee("1", 1000),
					new Employee("2", 2000)));
		assertThat(employeeManager.payEmployees()).isEqualTo(2);
		InOrder inOrder = inOrder(bankService, employeeRepository);
		then(employeeRepository).should(inOrder).findAll();
		then(bankService).should(inOrder).pay("1", 1000);
		then(bankService).should(inOrder).pay("2", 2000);
		then(bankService).shouldHaveNoMoreInteractions();
	}

	@Test
	public void testExampleOfArgumentCaptor() {
		// Just an example of ArgumentCaptor
		given(employeeRepository.findAll())
			.willReturn(asList(
					new Employee("1", 1000),
					new Employee("2", 2000)));
		assertThat(employeeManager.payEmployees()).isEqualTo(2);
		then(bankService).should(times(2)).pay(idCaptor.capture(), amountCaptor.capture());
		assertThat(idCaptor.getAllValues()).containsExactly("1", "2");
		assertThat(amountCaptor.getAllValues()).containsExactly(1000.0, 2000.0);
		then(bankService).shouldHaveNoMoreInteractions();
	}

	@Test
	public void testPayEmployeesWhenBankServiceThrowsException() {
		given(employeeRepository.findAll())
			.willReturn(asList(notToBePaid));
		willThrow(new RuntimeException()).given(bankService).pay(anyString(), anyDouble());
		// number of payments must be 0
		assertThat(employeeManager.payEmployees()).isEqualTo(0);
		// make sure that Employee.paid is updated accordingly
		then(notToBePaid).should().setPaid(false);
	}

	@Test
	public void testOtherEmployeesArePaidWhenBankServiceThrowsException() {
		given(employeeRepository.findAll())
			.willReturn(asList(notToBePaid, toBePaid));
		willThrow(new RuntimeException())
			.willDoNothing()
			.given(bankService).pay(anyString(), anyDouble());
		// number of payments must be 1
		assertThat(employeeManager.payEmployees()).isEqualTo(1);
		// make sure that Employee.paid is updated accordingly
		then(notToBePaid).should().setPaid(false);
		then(toBePaid).should().setPaid(true);
	}

	@Test
	public void testArgumentMatcherExample() {
		given(employeeRepository.findAll())
			.willReturn(asList(notToBePaid, toBePaid));
		willThrow(new RuntimeException())
			.given(bankService).pay(
					argThat(s -> s.equals("1")),
					anyDouble());
		// number of payments must be 1
		assertThat(employeeManager.payEmployees()).isEqualTo(1);
		// make sure that Employee.paid is updated accordingly
		then(notToBePaid).should().setPaid(false);
		then(toBePaid).should().setPaid(true);
	}

}
