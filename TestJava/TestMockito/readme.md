
# tutorial docs

refer to: https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html#12

code来源：https://qppw4bc6rk.feishu.cn/wiki/wikcncfDMoBWOrmp3nkcxTz3ejd， 这是学习 B站 Mockito的视频源码，自己手写的

## NOTE

代码里面混用的Junit4和Junit5，主要使用注释@Test的时候,@Before 必须一致

# Stubbing写法

6. verfify in order.
7. 多次返回值。建议用when(mock.fun).thenReturn("one", "two"); 后面的mock会覆盖前面的mock
8. subbing with callbacks: 用thenAnswer格式
9. doXXX和 thenXX的区别：以下情况必须要doXXX。doAnswer支持lamda表达式
   stub void methods
   stub the same method more than once, to change the behaviour of a mock in the middle of a test.
   stub methods on spy objects (see below)
10. & 17 Spying on real objects: 对真实对象进行spy，只add()两个元素, 但mock size()可以返回100
    //you can create partial mock with spy() method:
    List list = spy(new LinkedList());

//you can enable partial mock capabilities selectively on mocks:
Foo mock = mock(Foo.class);
//Be sure the real implementation is 'safe'.
//If real implementation throws exceptions or depends on specific state of the object then you're in trouble.
when(mock.someMethod()).thenCallRealMethod();

14. mock的一些智能用法
    Foo mock = mock(Foo.class, RETURNS_SMART_NULLS);  对于没有stub的方法能智能返回空集合、空对象等，而非直接返回NPE。
    Foo mock = mock(Foo.class, RETURNS_DEEP_STUBS);   // 能对mock方法返回的值进行进一步mock，但违反迪米特法则【这是对象A可以调用对象B的方法，但是不应该通过对象B去调用对象B的朋友对象C的方法】
    when(mock.getBar().getName()).thenReturn("deep");
15. Capturing arguments
    rgumentCaptor `<Person>` argument = ArgumentCaptor.forClass(Person.class);
    verify(mock).doSomething(argument.capture());
    assertEquals("John", argument.getValue().getName());
16. Verification with timeout (Since 1.8.5)， 如果要则是接下来几秒钟是否发生某种事情
    //passes when someMethod() is called no later than within 100 ms
    //exits immediately when verification is satisfied (e.g. may not wait full 100 ms)
    verify(mock, timeout(100)).someMethod();
    //above is an alias to:
    verify(mock, timeout(100).times(1)).someMethod();
17. one-line stubs: Car boringStubbedCar = when(mock(Car.class).shiftGear()).thenThrow(EngineNotStarted.class).getMock();
18. Custom verification failure message (Since 2.1.0).
    // will work with any verification mode
    verify(mock, times(2).description("someMethod should be called twice")).someMethod();
19. Java 8 Lambda Matcher Support (Since 2.1.0) 验证mock对象的参数满足要求；   55. Verification with assertions (Since 5.3.0)
    // Java 7 equivalent - not as neat
    verify(list, times(2)).add(argThat(new ArgumentMatcher `<String>`(){
    public boolean matches(String arg) {
    return arg.length() < 5;
    }
    }));

    verify(serviceMock).doStuff(assertArg(param -> {
    assertThat(param.getField1()).isEqualTo("foo");
    assertThat(param.getField2()).isEqualTo("bar");
    }));

# 没看懂

* BDD，behavior-driven-development:
  * Start learning about BDD here: https://en.wikipedia.org/wiki/Behavior-driven_development
* 初始化22：手写, MockitoAnnotations.openMocks(Object), MockitoJUnitRunner or MockitoRule.
  =======

# 代码说明

https://qppw4bc6rk.feishu.cn/wiki/wikcncfDMoBWOrmp3nkcxTz3ejd
这是学习 B站 Mockito的视频源码，自己手写的

# 注意事项

- 此处混用的Junit4和Junit5，主要使用注释@Test的时候,@Before 必须一致

>>>>>>> master
>>>>>>>
>>>>>>
>>>>>
>>>>
>>>
>>
