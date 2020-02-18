package com.example;

import java.util.List;
import java.util.ListIterator;

/**
 * An example of test double that is a "fake"
 * 
 * @author Lorenzo Bettini
 *
 */
public class EmployeeInMemoryRepository implements EmployeeRepository {

	private List<Employee> employees;

	public EmployeeInMemoryRepository(List<Employee> employees) {
		this.employees = employees;
	}

	@Override
	public List<Employee> findAll() {
		return employees;
	}

	@Override
	public Employee save(Employee employee) {
		ListIterator<Employee> listIterator = employees.listIterator();
		while (listIterator.hasNext()) {
			if (listIterator.next().getId().equals(employee.getId())) {
				listIterator.set(employee);
				return employee;
			}
		}
		employees.add(employee);
		return employee;
	}

}
