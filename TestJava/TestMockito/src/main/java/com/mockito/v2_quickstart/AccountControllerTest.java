package com.mockito.v2_quickstart;

import com.mockito.common.Account;
import com.mockito.common.AccountDao;
import com.mockito.common.AccountLoginController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import static org.mockito.Mockito.when;

public class AccountControllerTest {
	private AccountDao accountDao;
	private HttpServletRequest request;
	private AccountLoginController accountLoginController;
	
	@BeforeEach
	public void setUp() {
		this.accountDao = Mockito.mock(AccountDao.class);
		this.request = Mockito.mock(HttpServletRequest.class);
		this.accountLoginController = new AccountLoginController(accountDao);
	}

	@Test
	public void testLogin() {
		Account account = new Account();
		when(request.getParameter("username")).thenReturn("alex");
		when(request.getParameter("password")).thenReturn("123123");
		
		String result;
		
		// controller.login() with login
		when(accountDao.findAccount(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(null);
		 result = accountLoginController.login(request);
		Assertions.assertEquals(result, "/login");
	
		// controller.login() with index
		when(accountDao.findAccount(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(account);
		result = accountLoginController.login(request);
		Assertions.assertEquals(result, "/index");
		
		
		// controller.login() with exception
		when(accountDao.findAccount(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenThrow(UnsupportedOperationException.class);
		result = accountLoginController.login(request);
		Assertions.assertEquals(result, "/505");
	}

	
	
}
