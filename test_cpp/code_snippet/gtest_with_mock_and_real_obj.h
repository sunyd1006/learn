#include <iostream>
#include <string>
#include "gmock/gmock.h"
#include "gtest/gtest.h"

using namespace testing;

class Manager;
typedef std::shared_ptr<Manager> ManagerPtr;

struct Manager {
    virtual std::string Print() {
        return std::string("RealMethod");
    }
};

struct MockManager : public Manager {
    MOCK_METHOD0(Print, std::string());
};

class TestMain : public ::testing::Test
{
public:
    bool mIsUseMockObj;
    Manager* ptr;

    TestMain() {
        mIsUseMockObj = true;
        MemInit();
    }

    void MemInit() {
        if (mIsUseMockObj) {
            ptr = new Manager();
        } else {
            ptr = new MockManager();
        }
    }
};

TEST_F(TestMain, testPrint) {
    if (mIsUseMockObj) {
        EXPECT_CALL(*ptr, Print())
            .WillRepeatedly(Return(std::string("MockMethod")));
    }
    ptr->Print();
}