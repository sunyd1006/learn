package com.mockito.v6_spy;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class SpyMock {

	@Spy
	private List<String> list = new ArrayList<>();

	@BeforeEach
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testSpyByAnnotations() {
		list.add("Mockito");
		list.add("PowerMock");

		Assertions.assertEquals(list.get(0), "Mockito");
		Assertions.assertEquals(list.get(1), "PowerMock");
		Assertions.assertEquals(list.isEmpty(), false);

		// NOTE: 明显spy不为空，但我们就是让他返回空
		Mockito.when(list.isEmpty()).thenReturn(true);
		Mockito.when(list.size()).thenReturn(0);
		Assertions.assertEquals(list.isEmpty(), true);
		Assertions.assertEquals(list.size(), 0);

		// 但是 其他没有被 stub的方法不受影响
		Assertions.assertEquals(list.get(0), "Mockito");
		Assertions.assertEquals(list.get(1), "PowerMock");
	}

	@Test
	public void testSpyByExplicitDeclaration() {
		List<String> realList = new ArrayList<>();
		List<String> spy = Mockito.spy(realList);
		spy.add("Mockito");
		spy.add("PowerMock");
		Assertions.assertEquals(spy.get(0), "Mockito");

		// NOTE: 明显spy不为空，但我们就是让他返回空
		Mockito.when(spy.isEmpty()).thenReturn(true);
		Assertions.assertEquals(spy.isEmpty(), true);
	}
}
