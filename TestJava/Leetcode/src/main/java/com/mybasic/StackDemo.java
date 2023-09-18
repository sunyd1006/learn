package com.mybasic;

import org.junit.Test;
import java.util.*;

public class StackDemo {
	Stack<Integer> inStack;
	Stack<Integer> outStack;
	
	public StackDemo() {
		this.inStack = new Stack<>();
		this.outStack = new Stack<>();
	}
	
	public void push(int x){
		inStack.push(x);
	}
	
	public int peek(){
		if(outStack.isEmpty()){
			moveInToout();
		}
		return outStack.peek();
	}
	
	public boolean empty(){
		return inStack.isEmpty() && outStack.isEmpty();
	}
	
	public int pop(){
		if(outStack.isEmpty()){
			moveInToout();
		}
		return outStack.pop();
	}
	
	private void  moveInToout(){
		while(!inStack.isEmpty()){
			outStack.push(inStack.pop());
		}
	}
	
	@Test
	public void test(){
		StackDemo stackDemo = new StackDemo();
		stackDemo.push(1);
		stackDemo.push(2);
		
		System.out.println(stackDemo.peek());
		System.out.println(stackDemo.pop());
		System.out.println(stackDemo.pop());
		
		System.out.println(stackDemo.empty());
	}
}
