package com.mockito.v7_v8_functionMatcher;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class V8_WildcardArgumentsMatcherTest {
	@Mock
	private ComplexService service;
	
	@BeforeEach
	public void TearUp() {
		this.service = Mockito.mock(ComplexService.class);
	}
	
	@Test
	public void wildcardMethod1() {
		Mockito.when(service.method1(ArgumentMatchers.anyInt(), ArgumentMatchers.anyString(), ArgumentMatchers.anyCollection(), ArgumentMatchers.isA(Serializable.class))).thenReturn(100);
		int result = service.method1(1, "Alex", Collections.emptyList(), "Mockito");
		Assertions.assertEquals(result, 100);
		
		result = service.method1(1, "Jobs", Collections.emptyList(),"Mockito");
		Assertions.assertEquals(result, 100);
	}
	
	@Test
	public void wildcardMethod1WithSpec() {
		// NOTE: 注意请不要先写特例版本(*, a, *)，再写统配版本（*， *， *）否则存在覆盖问题
		Mockito.when(service.method1(ArgumentMatchers.anyInt(), ArgumentMatchers.anyString(), ArgumentMatchers.anyCollection(), ArgumentMatchers.isA(Serializable.class))).thenReturn(-1);
		Mockito.when(service.method1(ArgumentMatchers.anyInt(), ArgumentMatchers.eq("Alex"), ArgumentMatchers.anyCollection(), ArgumentMatchers.isA(Serializable.class))).thenReturn(100);
		Mockito.when(service.method1(ArgumentMatchers.anyInt(), ArgumentMatchers.eq("Jobs"), ArgumentMatchers.anyCollection(), ArgumentMatchers.isA(Serializable.class))).thenReturn(200);
		
		int result = service.method1(1, "Alex", Collections.emptyList(),"Mockito");
		Assertions.assertEquals(result, 100);
		
		result = service.method1(1, "Jobs", Collections.emptyList(),"Mockito");
		Assertions.assertEquals(result, 200);
		
		result = service.method1(1, "JackMa", Collections.emptyList(),"Mockito");
		Assertions.assertEquals(result, -1);
	}
	
	// 验证通配符的执行
	@Test
	public void wildcardMethod2() {
		List<Object> empList = Collections.emptyList();
		Mockito.doNothing().when(service).method2(ArgumentMatchers.anyInt(), ArgumentMatchers.anyString(), ArgumentMatchers.anyCollection(), ArgumentMatchers.isA(Serializable.class));
		
		service.method2(1, "Alex", Collections.emptyList(),"Mockito");
		Mockito.verify(service, Mockito.times(1)).method2(1, "Alex", Collections.emptyList(),"Mockito");
		Mockito.verify(service, Mockito.times(1)).method2(ArgumentMatchers.anyInt(), ArgumentMatchers.eq("Alex"), ArgumentMatchers.anyCollection(), ArgumentMatchers.isA(Serializable.class));
	}
	
	@AfterEach
	public void TearDown() {
		Mockito.reset(service);
	}
}
