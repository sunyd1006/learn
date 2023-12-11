package com.learn_basic.designmodel;

/**
 * https://www.cnblogs.com/yssjun/p/11102162.html
 * 3种工厂，简单工厂，工厂方法模式，抽象工厂模式
 * <p>
 * 简单工厂: 工厂负责具体的 new 实例
 * 工厂方法：工厂接否负责定义能力1，具体工厂负责实现具体能力1
 * 抽象工厂：工厂接否负责定义能力123，具体工厂负责实现具体能力123
 */
public class FactoryMethodDemo {
	public static void main(String[] args) {
		AbstractFactory miFactory = new XiaoMiFactory();
		AbstractFactory appleFactory = new AppleFactory();
		miFactory.makePhone();            // make xiaomi phone!
		appleFactory.makePhone();        // make iphone!
		
		// 抽象工厂：就是一个工厂里面可以建造多个东西
		miFactory.makePC();
		appleFactory.makePC();
	}
}

/**
 * 抽象工厂：小米工厂可以生产，PC，Phone 。
 */
interface PC {
	void make();
}

interface Phone {
	void make();
}

class MiPC implements PC {
	public MiPC() {
		this.make();
	}
	
	@Override
	public void make() {
		System.out.println("make mi pc!");
	}
}


class MiPhone implements Phone {
	public MiPhone() {
		this.make();
	}
	
	@Override
	public void make() {
		System.out.println("make xiaomi phone!");
	}
}

class IPhone implements Phone {
	public IPhone() {
		this.make();
	}
	
	@Override
	public void make() {
		// TODO Auto-generated method stub
		System.out.println("make iphone!");
	}
}

interface AbstractFactory {
	Phone makePhone();
	
	PC makePC();
}

class XiaoMiFactory implements AbstractFactory {
	@Override
	public Phone makePhone() {
		return new MiPhone();
	}
	
	@Override
	public PC makePC() {
		return new MiPC();
	}
}

class AppleFactory implements AbstractFactory {
	@Override
	public Phone makePhone() {
		return new IPhone();
	}
	
	@Override
	public PC makePC() {
		return new ApplePC();
	}
}

class ApplePC implements PC {
	public ApplePC() {
		this.make();
	}
	
	@Override
	public void make() {
		System.out.println("make apple pc!");
	}
}
