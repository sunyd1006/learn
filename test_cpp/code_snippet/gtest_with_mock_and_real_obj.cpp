#include <iostream>
#include <memory>
#include <string>

#include "gmock/gmock.h"
#include "gtest/gtest.h"

using namespace testing;

struct RealManager {
    virtual std::string Print() {
        return std::string("RealMethod");
    }
};
struct MockManager: public RealManager {
    MockManager() {}
    MOCK_METHOD0(Print, std::string());
};

struct MockAndRealManager {
    bool mIsRealMethod;
    std::shared_ptr<RealManager> realPtr;
    std::shared_ptr<MockManager> mockPtr;

    MockAndRealManager(bool isRealMethod): mIsRealMethod(isRealMethod) {
        realPtr.reset(new RealManager());
        mockPtr.reset(new MockManager());
    }

    std::string Print() {
        if (mIsRealMethod) {
            return realPtr->Print();
        } else {
            EXPECT_CALL(*mockPtr, Print()).WillRepeatedly(Return(std::string("MockMethod")));
            return mockPtr->Print();
        }
    }
};

class TestMain : public ::testing::Test {
public:
    bool mIsRealMethod;
    std::shared_ptr<MockAndRealManager> ptr;

    TestMain() {
        mIsRealMethod = false;
        ptr.reset(new MockAndRealManager(mIsRealMethod));
    }

    static void TearDownTestCase() { std::cout << "LAST CASE DONE" << std::endl; }

    void expect_string_eq(const std::string& val, const std::string& realMethodResponse,
                          const std::string& mockMethodResponse) {
        if (mIsRealMethod) {
            EXPECT_EQ(val, realMethodResponse);
        } else {
            EXPECT_EQ(val, mockMethodResponse);
        }
    }
};

TEST_F(TestMain, testPrint) {
    std::string res = ptr->Print();
    expect_string_eq(res, "RealMethod", "MockMethod");
}

int main(int argc, char** argv) {
    testing::InitGoogleTest(&argc, argv);
    return RUN_ALL_TESTS();
}