package com.mockito.v9v10_hamcrest;

import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.both;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.AdditionalMatchers.gt;


public class V10_CompareThanTest {
	@Test
	public void test() {
		// 自定义的 gt lt 版本
		assertThat(10, CompareThan.gt(8));
		assertThat(10, CompareThan.lt(80));
		assertThat(15, both(CompareThan.gt(10)).and(CompareThan.lt(20)));
	
		
		// 这里有点问题
		// assertThat(17, is(org.hamcrest.Matchers.greaterThan(10)));
		// list.add(17);
		// verify(list).add(org.mockito.AdditionalMatchers.gt(10));
	}
}
