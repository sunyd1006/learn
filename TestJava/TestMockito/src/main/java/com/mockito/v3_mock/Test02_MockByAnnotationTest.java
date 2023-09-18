package com.mockito.v3_mock;

import com.mockito.common.Account;
import com.mockito.common.AccountDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class Test02_MockByAnnotationTest {
	@Mock(answer = Answers.RETURNS_SMART_NULLS)
	private AccountDao accountDao;
	
	@BeforeEach
	public void init() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testMock() {
		Account account = accountDao.findAccount("x", "x");
		System.out.println(account);
	}
}
