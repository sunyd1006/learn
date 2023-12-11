#include <iostream>
#include <iterator>
#include <vector>
#include <cstring>
#include <stdexcept>

#include "Sales_data.h"
using namespace  std;

// g++ 编译2个文件，-c生成可执行main
// g11 Sales_data.cpp ex7main.cpp -o main && ./main
// g11 ex7main.cpp -o main && ./main
int main() {
    cout << "\n case: ex7.word_transformation_bad.txt " << endl;
    Sales_data item("aaaa", 10, 10);
    item.printString();

    cout << "\n case: ex7.3 " << endl;
    Sales_data total;
    // startMain();       // 注意何时打开注释

    cout << "\n case: ex7.11 " << endl;
    Sales_data item4(std::cin);
    print(std::cout, item4) << std::endl;


    cout << "\n case: 测试static 成员 " << endl;
    Sales_data item5("book123123");
    item5.printString();

    cout << "\n case: Sales_data.cpp的static 定义部分： " << endl;
    // item5.initByCall();  // 如何调用静态函数呢？
    item5.printStaticVal();

    cout << endl << "================= tmp ============" << endl;
    return 0;
};
