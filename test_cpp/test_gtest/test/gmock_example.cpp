#include "add.h"
#include "gmock/gmock.h"
#include "gtest/gtest.h"
#include "tool.h"

#include <iostream>
#include <string>

using namespace std;
using namespace testing;

// https://google.github.io/googletest/gmock_cook_book.html
namespace sun_test_gtest {
    class InnerClass;
    typedef std::shared_ptr<InnerClass> InnerClassPtr;

    class Master;
    typedef std::shared_ptr<Master> MasterPtr;

    struct Param {
        string name;
        string desc;

        string toString() const {
            string ret;
            ret.append("Param( ");
            ret.append("name: " + name + " , ");
            ret.append("desc: " + desc + " )" );
            return ret;
        }
    };

    class InnerClass {
    private:
        int count;
    public:
        InnerClass() {}

        InnerClass(int c) : count(c) {}

        virtual string getStringNull() { return string("Null"); }

        virtual string getStringValue(int c) { return std::to_string(c); }

        virtual int setArgPointeeDemo(int *param1, vector<int> &testVector) {
            *param1 = 10000;
            testVector.push_back(100001);
            testVector.push_back(100002);
            testVector.push_back(100003);
            return 0;
        }

        virtual string PrintParam(const Param& param) {
            std::cout << param.toString() << std::endl;
            return "call inner.PrintParam";
        }
    };

    class MockInnerClass : public InnerClass {
    public:
        MockInnerClass() {} // not ;
        MockInnerClass(int c) : InnerClass(c) {}
        MOCK_METHOD0(getStringNull, string());
        MOCK_METHOD1(getStringValue, string(int c));
        MOCK_METHOD1(PrintParam, string(const Param& param));
        MOCK_METHOD2(setArgPointeeDemo, int(int* param1, vector<int> &testVector));
    };

    class Master {
    private:
        InnerClassPtr mInnerClassPtr;
    public:
        Master() {}
        Master(InnerClassPtr InnerClassPtr) : mInnerClassPtr(InnerClassPtr) {}

        void Call_InnerFunc() {
            int* testInt;
            vector<int> testVec;
            int res =  mInnerClassPtr->setArgPointeeDemo(testInt, testVec);
            print("call InnerClass(setArgPointeeDemo)");
            print(std::string("调用返回参数param1：") + std::to_string(*testInt));
            println(testVec, "调用返回参数testVec: ");
        }
    };

    MATCHER_P(ParamObjMatcher, other, "Param is Equal") {
        bool ret = (static_cast<Param>(arg).name == other.name);
        cout << "IsMatcherSuccess: " << ret << endl;
        return static_cast<Param>(arg).name == other.name;
    }

    class TestMain : public ::testing::Test {
    public:
        TestMain() {}
    };

    TEST_F(TestMain, Macher_p) {
        // NOTE: 注意关于 shared_ptr的写法，对new出来的指针进行stub， 用mockPtr来使用
        MockInnerClass *mock = new MockInnerClass();
        InnerClassPtr mockPtr(mock);

        Param param;
        param.name = "sunyindong";

        EXPECT_CALL(*mock, PrintParam(_))
            .WillRepeatedly(Return(param.name + "ReturnByMock"));

        EXPECT_CALL(*mock, PrintParam(ParamObjMatcher(param)))
            .WillOnce(Return(param.name + "ReturnByYes"));

        string stringRet = mockPtr->PrintParam(param);
        std::cout << stringRet << std::endl;

//        EXPECT_EQ(stringRet, param.name + "ReturnByMock");
        EXPECT_EQ(stringRet, param.name + "ReturnByYes");

    }

    TEST_F(TestMain, SetArgPointee) {
        MockInnerClass *mock = new MockInnerClass();
        InnerClassPtr mockPtr(mock);
        // SetArgPointee :设置第1（0，1）个参数为，指向5的指针。
        // MOCK_METHOD(void, Mutate, (bool mutate, int* value), (override));
        // EXPECT_CALL(mutator, Mutate(true, _)).WillOnce(SetArgPointee<1>(5));
        vector<int> testVec;
        testVec.push_back(1);
        testVec.push_back(2);

        EXPECT_CALL(*mock, setArgPointeeDemo(_, _))
                .WillRepeatedly(
                        DoAll(SetArgPointee<0>(5), SetArgReferee<1>(testVec), Return(1)));

        printInfoLine("Call Call_InnerFunc");
        Master master(mockPtr);
        master.Call_InnerFunc();

        // 无法设定masterPtr参数，放弃
        // MasterPtr masterPtr(mockPtr);
        // masterPtr->Call_InnerFunc();
        // cout << "Call Call_InnerFunc result end: " << endl;
    }

    // 测试 expect_call的调用次数
    TEST_F(TestMain, Times_WillOne) {
        MockInnerClass mock; // MockInnerClass mock2(0);
        EXPECT_CALL(mock, getStringNull()).Times(2);
        EXPECT_CALL(mock, getStringValue(_)).Times(1); // #1: EXPECT_CALL

        // 正确调用
        mock.getStringNull();
        mock.getStringNull();
        mock.getStringValue(3);

        // 不正确的调用
        // mock.getStringNull(); // 至少要调用2次
        // mock.getStringValue(2);

        printInfoLine();
        EXPECT_CALL(mock, getStringValue(_)) // 100 200 1
                .WillOnce(Return("100"))
                .WillOnce(Return("200"))
                .WillRepeatedly(Return("1")); // 没有这行可以调用2次，有这行可以调用无数次。

        cout << mock.getStringValue(0) << endl;
        cout << mock.getStringValue(0) << endl;
        cout << mock.getStringValue(0) << endl;
        cout << mock.getStringValue(0) << endl;

        // 不可以在调用 getStringValue()了
        EXPECT_CALL(mock, getStringValue(0)).Times(0);
        // cout << mock.getStringValue() << endl;


        printInfoLine("限定expect_call 调用次数");
        int n = 20;
        // NOTE: 前20次调用第一个函数，然后失效；后无数次调用第2个getStringValue，这个未定义Times故而不失效
        InSequence seq; // 必须定义Sequence，否则第2个 expect_call 就会禁用第一个 expect_call
        EXPECT_CALL(mock, getStringValue(_))
                .Times(20)
                .WillRepeatedly(Return("前20次调用"));
        EXPECT_CALL(mock, getStringValue(_))
                .WillRepeatedly(Return("后无数次调用"));

        int tmpN = 0;
        while (++tmpN <= n) {
            print(mock.getStringValue(tmpN));
            print(" ");
        }

        println();
        while (++tmpN <= n + 10) {
            print(mock.getStringValue(tmpN));
            print(" ");
        }
    }

    // 测试调用 应当按照EXPECT_CALL的声明顺序调用, 而不能够调换
    TEST_F(TestMain, InSequence) {
        MockInnerClass mock;
        {
            InSequence seq;
            EXPECT_CALL(mock, getStringNull()).WillRepeatedly(Return(" mock call getStringNull(): "));
            // #0: 这个EXPECT_CALL(getStringValue)的编号一旦调用一次，然后这个expect_call方法就会失效inactive，后续就不会匹配到它
            EXPECT_CALL(mock, getStringValue(_))
                    .WillRepeatedly(
                            Return(" mock call getStringValue(): "));
        }

        // 正确的调用
        mock.getStringNull();
        mock.getStringValue(2); // 达到指定掉要次数后 Actual: called once - saturated and retired
        // mock.getStringNull(); // 不可以调用 getStringNull，它已经 retired了

        // 下两行是不正确的调用
        // mock.getStringValue(2); // 因为 getStringNull()一次都没有调用，故而不满足调用顺序
        // mock.getStringNull();
    }

    // 被测试函数调用一定次数后，就禁用EXPECT_CALL()
    // https://google.github.io/googletest/gmock_cook_book.html#controlling-when-an-expectation-retires
    TEST_F(TestMain, RetiresOnSaturation) {
        MockInnerClass mock;

        EXPECT_CALL(mock, getStringValue(_)).WillRepeatedly(Return("not10"));
        EXPECT_CALL(mock, getStringValue(10)).WillOnce(Return("yes10"));

        println(mock.getStringValue(0)); // 匹配 第1个模拟方法
        println(mock.getStringValue(10)); // 匹配 第2个模拟方法
        // print(mock.getStringValue(10)); // Error 报错, 虽然tims(1)了，但是还是active的。

        printInfoLine();

        EXPECT_CALL(mock, getStringValue(10)).WillOnce(Return("yes10")).RetiresOnSaturation();
        println(mock.getStringValue(10)); // 匹配 第3个模拟方法
        println(mock.getStringValue(10)); // 匹配 第1个模拟方法

    }


    // don't mock all overload's function
    // class MockFoo : public Foo {
    // ...
    // using Foo::Add;
    // MOCK_METHOD(int, Add, (Element x), (override));
    // // We don't want to mock int Add(int times, Element x);
    // ...
    // };

    // Field(&Foo::number, Ge(3))
    // EXPECT_CALL(foo, Bar(Pointee(Ge(3)))); // 参数是个指针，指向的值必须大于3.
    // 如果传入参数为null会报错。

    // 返回引用
    // MOCK_METHOD(Bar&, GetBar, (), (override));
    // EXPECT_CALL(foo, GetBar()).WillOnce(ReturnRef(bar));

    // SetArgPointee :设置第1（0，1）个参数为，指向5的指针。
    // MOCK_METHOD(void, Mutate, (bool mutate, int* value), (override));
    // EXPECT_CALL(mutator, Mutate(true, _)).WillOnce(SetArgPointee<1>(5));

    // SetArrayArgument < n >
    // class MockArrayMutator : public ArrayMutator {
    // public:
    // MOCK_METHOD(void, Mutate, (int* values, int num_values), (override));
    // }
    // MockArrayMutator mutator;
    // int values[5] = {1, 2, 3, 4, 5};
    // EXPECT_CALL(mutator, Mutate(NotNull(), 5))
    // .WillOnce(SetArrayArgument<0>(values, values + 5));

    // 设置 默认 返回 执行 逻辑
    // ON_CALL(foo, Sign(_)).WillByDefault(Return(-1));

    // 使用 Invoke ：使用 Invoke ()调用的函数或 functor 必须具有与使用它的 mock
    // 函数相同的参数数目。 使用 invokewithouttargs () ，它类似于 Invoke ()
    // ，只是它不会将模拟函数的参数传递给被调用方 MockFoo foo; Helper helper; EXPECT_CALL(foo,
    // Sum(_, _)) .WillOnce(&CalculateSum) .WillRepeatedly(Invoke(NewPermanentCallback(Sum3, 1)));
    // EXPECT_CALL(foo, ComplexJob(_))
    // .WillOnce(Invoke(&helper, &Helper::ComplexJob))
    // .WillOnce([] { return true; })
    // .WillRepeatedly([](int x) { return x > 0; });

    // foo.Sum(5, 6); // Invokes CalculateSum(5, 6).
    // foo.Sum(2, 3); // Invokes Sum3(1, 2, 3).
    // foo.ComplexJob(10); // Invokes helper.ComplexJob(10).
    // foo.ComplexJob(-1); // Invokes the inline lambda.

    // IgnoreResult ()
    //
    // double DistanceToOrigin(Unused, double x, double y) {
    // return sqrt(x*x + y*y);
    // }
    // EXPECT_CALL(mock, Bar(5, _, _)).WillOnce(Invoke(DistanceToOriginWithIndex));

    // ReturnRef(*variable*)
    // SetArgReferee

} // namespace sun_test_gtest
