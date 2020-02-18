package com.example;

public interface TransactionManager {

	<T> T doInTransaction(TransactionCode<T> code);

}
