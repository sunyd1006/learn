package com.mockito.v9v10_hamcrest;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Factory;

public class CompareThan<T extends Number> extends BaseMatcher<T> {
	private final T expect;
	private boolean isLow;
	
	public CompareThan(T expect, boolean isLow) {
		this.expect = expect;
		this.isLow = isLow;
	}
	
	@Override
	public boolean matches(Object actual) {
		Class<? extends Number> aClass = expect.getClass();
		 if (aClass == Integer.class) {
			return isLow ? ((Integer) expect > (Integer) actual) : ((Integer) expect < (Integer) actual);
		} else	 if (aClass == Short.class) {
			 return isLow ? ((Short) expect > (Short) actual) : ((Short) expect < (Short) actual);
		 } else	 if (aClass == Byte.class) {
			 return isLow ? ((Byte) expect > (Byte) actual) : ((Byte) expect < (Byte) actual);
		 } else	 if (aClass == Double.class) {
			 return isLow ? ((Double) expect > (Double) actual) : ((Double) expect < (Double) actual);
		 } else	 if (aClass == Float.class) {
			 return isLow ? ((Float) expect > (Float) actual) : ((Float) expect < (Float) actual);
		 } else	 if (aClass == Long.class) {
			 return isLow ? ((Long) expect > (Long) actual) : ((Long) expect < (Long) actual);
		 } else {
		 	throw new AssertionError("The number type " + aClass + " not supported. ");
		 }
	}
	
	@Factory
	public static <T extends  Number> CompareThan<T> gt(T expect) {
		return new CompareThan<>(expect, false);
	}
	
	@Factory
	public static <T extends  Number> CompareThan<T> lt(T expect) {
		return new CompareThan<>(expect, true);
	}
	
	@Override
	public void describeTo(Description description) {
		description.appendText("compare two number failed!");
	}
}
