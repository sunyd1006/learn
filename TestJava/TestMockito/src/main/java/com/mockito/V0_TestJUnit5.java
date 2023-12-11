package com.mockito;

import org.junit.Test;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;

public class V0_TestJUnit5 {
	
	@BeforeEach
	public void BeforeEach() {
		System.out.println("========  BeforeEach =========");
	}
	
	@Test
	public void printHaha() {
		System.out.println("=================");
	}

	@AfterEach
	public void AfterEach() {
		System.out.println("========  AfterEach =========");
	}
}
