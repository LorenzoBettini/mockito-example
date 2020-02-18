package com.example;

import java.util.function.Function;

@FunctionalInterface
public interface TransactionCode<T> extends Function<EmployeeRepository, T> {

}
