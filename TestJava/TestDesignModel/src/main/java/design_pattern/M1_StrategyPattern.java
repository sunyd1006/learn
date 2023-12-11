package design_pattern;

import static java.lang.Thread.sleep;

/**
 *  ---------------------- NOTE:1    FlyBehavior / QuackBehavior
 */
interface FlyBehavior {
	void fly();
}

class FlyWithWings implements FlyBehavior {
	@Override
	public void fly() {
		System.out.println("I'm flying! ");
	}
}

class FlyNoWay implements FlyBehavior {
	@Override
	public void fly() {
		System.out.println("I can't fly! ");
	}
}

interface QuackBehavior {
	void quark();
}

class Quark implements QuackBehavior {
	@Override
	public void quark() {
		System.out.println("Quark");
	}
}

// 沉默的夸克
class MuteQuark implements QuackBehavior {
	@Override
	public void quark() {
		System.out.println("MuteQuark! ");
	}
}

// 短促而尖厉地叫；吱吱叫
class Squeak implements QuackBehavior{
	@Override
	public void quark() {
		System.out.println("Squeak! ");
	}
}


/**
 *  ---------------------- NOTE:2 Duck 行为
 */
abstract class Duck {
	FlyBehavior flyBehavior;
	QuackBehavior quackBehavior;
	
	public abstract void display();
	
	public void performFly() {
		flyBehavior.fly();
	}
	
	public void performQuark() {
		quackBehavior.quark();
	}
	
	public void swim() {
		System.out.println("All ducks folat, even decoys");
	}
	
	// NOTE:3 是不是用来动态设定子类行为的？子类的fly，quark行为可以被动态改变了。
	public void setFlyBehavior(FlyBehavior flyBehavior) {
		this.flyBehavior = flyBehavior;
	}
	
	public void setQuackBehavior(QuackBehavior quackBehavior) {
		this.quackBehavior = quackBehavior;
	}
}

class ModelDuck extends Duck {
	public ModelDuck() {
		flyBehavior = new FlyNoWay();
		quackBehavior = new Quark();
	}
	
	@Override
	public void display() {
		System.out.println("I'm a model duck.");
	}
}

/**
 * I can't fly!
 * I'm flying!
 */
public class M1_StrategyPattern {
	public static void main(String[] args) {
		try {
			
			Duck model = new ModelDuck();
			model.setFlyBehavior(new FlyNoWay());
			model.performFly();
			
			model.setFlyBehavior(new FlyWithWings());
			model.performFly();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
