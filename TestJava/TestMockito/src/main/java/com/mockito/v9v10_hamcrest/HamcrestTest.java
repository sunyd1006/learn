package com.mockito.v9v10_hamcrest;

import org.junit.jupiter.api.Test;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class HamcrestTest {

	@Test
	public void testBasic() {
		int i = 10;
		
		assertThat(i, equalTo(10));
		assertThat(i, not(equalTo(20)));
		assertThat(i, equalTo(10));
		assertThat(i, not(20));
	}
	
	@Test
	public void testEitherOr_BothAnd() {
		double price = 23.45;
		boolean isNext = false;
		assertThat(price, either(equalTo(23.45)).or(equalTo(23.54)));
		
		assertThat(price, both(equalTo(23.45)).and(not(equalTo(23.54))));
		
		assertThat(price, anyOf(is(23.45), is(43.34), is(54.24)));
		
		assertThat(price, allOf(is(23.45), not(is(43.34)) , not(is(54.24))));
		
		assertThat(Stream.of(1, 2, 3).anyMatch(i -> i > 2), equalTo(true));
		assertThat(Stream.of(1, 2, 3).allMatch(i -> i > 0), equalTo(true));
	}
	
	@Test
	public void testErrorMessage() {
		double price = 23.45;
		assertThat("price isnot 23.45. ",price, either(equalTo(23.45)).or(equalTo(23.54)));
	}
}
