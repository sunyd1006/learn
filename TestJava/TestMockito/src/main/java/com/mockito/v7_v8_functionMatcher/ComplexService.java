package com.mockito.v7_v8_functionMatcher;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;

public class ComplexService {
	
	public int method1(int i, String s, Collection<?> c,  Serializable serial) {
		throw new RuntimeException();
	}
	
	public void method2(int i, String s, Collection<?> c, Serializable serial) {
		throw new RuntimeException();
	}
}
