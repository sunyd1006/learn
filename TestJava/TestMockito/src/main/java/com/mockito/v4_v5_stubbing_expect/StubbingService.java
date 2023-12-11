package com.mockito.v4_v5_stubbing_expect;

public class StubbingService {
	public String runRealMethod() {
		System.out.println("============== You call runRealMethod");
		return "runRealMethod";
	}
	
	public String runStubbingMethod() {
		System.out.println("============== You call runStubbingMethod");
		return "runStubbingMethod";
	}
}
