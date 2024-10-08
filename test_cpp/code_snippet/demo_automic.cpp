#include <atomic>
#include <iostream>


std::atomic<int> atomic_int;
int expected, desired;
bool exchanged;

void print(const std::string& desc = "")
{
    std::cout << "atomic_int = " << atomic_int
	      << "  expected = " << expected
	      << "  desired = " << desired
	      << "  exchanged = " << std::boolalpha << exchanged
          << " description = " << desc
	      << "\n";
}

int main()
{
    atomic_int= 3;
    expected = 4, desired = 5, exchanged = false;
    print("before compare_exchange_strong");

    // expected != atomic_int   ==>  expected 被修改
    exchanged= atomic_int.compare_exchange_strong(expected, desired);
    print("after compare_exchange_strong");

    // expected == atomic_int   ==>  atomic_int 被修改
    exchanged= atomic_int.compare_exchange_strong(expected, desired);
    print("after compare_exchange_strong");


    std::cout << "compare_exchange_weak" << std::endl;
    atomic_int = 1;
    expected = 4, desired = 5, exchanged = false;
    print("before compare_exchange_weak");
    while (!atomic_int.compare_exchange_weak(expected, desired)) {
        print("change loop");
    }
    print("after compare_exchange_weak");
}

