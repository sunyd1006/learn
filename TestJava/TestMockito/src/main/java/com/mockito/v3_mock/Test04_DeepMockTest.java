package com.mockito.v3_mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class Test04_DeepMockTest {
	
	// 深入Mock， 即 lesson03Service 依赖 Lesson03, 深入Mock 是指 mock lesson03Service的时候，也mock lesson
	@Mock(answer = Answers.RETURNS_DEEP_STUBS)
	Lesson03Service lesson03Service;
	
	@BeforeEach
	public void init() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testDeepMock() {
		Lesson03 lesson03 = lesson03Service.get();
		// NOTE: lesson03此处并没有显示Mock，但其依然可以调用foo()方法
		lesson03.foo();
	}
}