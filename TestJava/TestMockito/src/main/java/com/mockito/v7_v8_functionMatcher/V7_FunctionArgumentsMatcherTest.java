package com.mockito.v7_v8_functionMatcher;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.mockito.Mockito.*;

public class V7_FunctionArgumentsMatcherTest {
	
	@Test
	public void badicTest() {
		List<Integer> list = mock(ArrayList.class);
		when(list.get(0)).thenReturn(100);
		
		Assertions.assertEquals(list.get(0), 100);
		Assertions.assertEquals(list.get(1), null);
	}
	
	@Test
	public void testComplexFunctionMatcher() {
		Foo foo = mock(Foo.class);
		when(foo.fooFunc(isA(Parent.class))).thenReturn(100);
		int result = foo.fooFunc(new Child1());
		
		// mock对象foo, 应该调用假方法的模拟，
		Assertions.assertEquals(result, 100);
		
		// NOTE: 重置 foo 的方法模拟
		reset(foo);
		
		when(foo.fooFunc(isA(Child1.class))).thenReturn(100);
		result = foo.fooFunc(new Child2());
		// mock对象foo，应该调用假方法默认设定，此设定对基本类型返回默认值
		Assertions.assertEquals(result, 0);
		
		// NOTE: 重置 foo 的方法模拟
		reset(foo);
		
		when(foo.fooFunc(any(Child1.class))).thenReturn(100);
		result = foo.fooFunc(new Child2());
		// mock对象foo，应该调用假方法默认设定，此设定对基本类型返回默认值
		Assertions.assertEquals(result, 100);
	}
	
	static class Foo{
		int fooFunc(Parent p) {
			return p.work();
		}
	}
	interface Parent {
		int work();
	}
	
	class Child1 implements Parent {
		@Override
		public int work() {
			return 51;
		}
	}
	
	class Child2 implements Parent {
		@Override
		public int work() {
			return 52;
		}
	}
}
