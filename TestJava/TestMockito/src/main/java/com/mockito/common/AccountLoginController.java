package com.mockito.common;

import javax.servlet.http.HttpServletRequest;

public class AccountLoginController {
	private final AccountDao accountDao;
	
	public AccountLoginController(AccountDao accountDao) {
		this.accountDao = accountDao;
	}
	
	public String login(HttpServletRequest request) {
		final String userName = request.getParameter("username");
		final String password = request.getParameter("password");
		try {
			Account account = accountDao.findAccount(userName, password);
			if (account == null) {
				return "/login";
			} else  {
				return "/index";
			}
		} catch (Exception e) {
			return "/505";
		}
	}

	public String getAccountLoginVersion() {
		return "version1";
	}
}
