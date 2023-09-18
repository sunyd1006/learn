package com.learn_basic.designmodel;

import java.util.ArrayList;
import java.util.List;

/**
 * refer: https://www.runoob.com/design-pattern/observer-pattern.html
 * - 1个对象 Subject 有很多个观察者；有1个状态资源；1个通知观察者执行的方案 notifyAll
 * - 1个观察者，有1个观察的对象；1个执行方案 update
 */
public class ObserverPatternDemo {
  public static void main(String[] args) {
    Subject subject = new Subject();
    new ObserverHex(subject);
    new ObserverOctal(subject);

    System.out.println("First state change: 15");
    subject.setState(15);
    System.out.println("Second state change: 10");
    subject.setState(10);
  }
}

abstract class Observer {
  protected Subject subject;
  public abstract void update();
}

class Subject {
  protected int state;
  protected List<Observer> observers;
  public Subject(){
    this.observers = new ArrayList<>();
  }
  public int getState() {
    return state;
  }
  public void setState(int state) {
    this.state = state;
    notifyAllObservers();
  }

  public void attach(Observer observer) {
    observers.add(observer);
  }
  public void notifyAllObservers() {
    for (Observer observer : observers) {
      observer.update();
    }
  }
}

class ObserverHex extends Observer {
  public ObserverHex(Subject subject) {
    this.subject = subject;
    this.subject.attach(this); // ee: focus add this to subject !!
  }

  @Override
  public void update() {
    System.out.println("Hex String: "
            + Integer.toHexString(subject.getState()).toUpperCase());
  }
}

class ObserverOctal extends Observer {
  public ObserverOctal(Subject subject) {
    this.subject = subject;
    this.subject.attach(this);
  }

  @Override
  public void update() {
    System.out.println("Octal String: "
            + Integer.toOctalString(subject.getState()));
  }
}
