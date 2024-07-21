#include <iostream>
#include <string>
#include <vector>
#include <memory>

#include <iostream>
#include <string>
#include <vector>
#include <memory>

#include <iostream>
#include <string>
#include <vector>
#include <memory>

#include <iostream>
#include <string>
#include <vector>
#include <memory>


/**

   例如，假设有一个Subject类和一个Observer类，Subject类保持一个shared_ptr指向Observer对象，同时Observer类也保持一个指向Subject对象的引用。
        这样就形成了循环引用，导致对象无法被正确释放。使用weak_ptr可以解决这个问题。
        Subject类保持一个weak_ptr指向Observer对象，当需要通知Observer对象时，先通过weak_ptr提升为shared_ptr，然后再通知Observer对象。
        这样就避免了循环引用，同时也避免了内存泄漏。

   另一个使用场景是缓存。在缓存中，为了避免对象在缓存中被删除时仍然保持着对对象的强引用，可以使用weak_ptr来监视被缓存的对象。
   当需要使用缓存中的对象时，可以先通过weak_ptr提升为shared_ptr，然后再进行使用。
**/

// 定义Person类，用于表示一个人及其朋友关系
class Person {
public:
    explicit Person(const std::string &name) : name_(name) {}

    // 添加一个朋友到当前人的朋友列表中
    void AddFriend(const std::shared_ptr<Person> &friendPtr) {
        friends_.emplace_back(friendPtr);
    }

    // 通知所有的朋友他们还活着
    void NotifyFriends() const {
        for (const auto &weakFriend : friends_) {
            auto sharedFriend = weakFriend.lock();
            if (sharedFriend) {
                std::cout << sharedFriend->GetName() << " is still alive!" << std::endl;
            } else {
                std::cout << "Friend is no longer alive." << std::endl;
            }
        }
    }

    // 获取名字
    std::string GetName() const {
        return name_;
    }

private:
    std::string name_;          // 名字
    std::vector<std::weak_ptr<Person>> friends_; // 弱指针朋友列表
};

// 主函数
int main() {
    auto john = std::make_shared<Person>("John");
    auto jane = std::make_shared<Person>("Jane");

    john->AddFriend(jane);      // 约翰添加简作为他的朋友
    jane->AddFriend(john);      // 简添加约翰作为她的朋友

    john->NotifyFriends();      // 输出: Jane is still alive!
    jane->NotifyFriends();      // 输出: John is still alive!

    auto newJane = std::make_shared<Person>("New Jane");   // 创建一个新的简对象
    john->NotifyFriends();      // 输出: Friend is no longer alive.

    return 0;
}
