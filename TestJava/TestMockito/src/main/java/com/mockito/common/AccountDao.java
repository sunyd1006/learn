package com.mockito.common;

public class AccountDao {
	public Account findAccount(String username, String password) {
		throw  new UnsupportedOperationException("findAccount不可调用");
	}
}
