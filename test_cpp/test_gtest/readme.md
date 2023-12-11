NeedBeMock：Master的内部组件，它是一个类
MockNeedBeMock: 对NeedBeMock的Mock
Master：需要被测的类，其内部data-member即成员变量为NeedBeMock

TestMain: 继承自gtest的测试类



# 执行方法

当前目录是项目的根目录

cd build; cmake ..; make;
