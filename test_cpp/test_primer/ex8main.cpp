#include <iostream>
#include <stdexcept>

#include <vector>

#include <fstream>
#include <sstream>

#include <iterator>
#include <cstring>

#include "Sales_data.h"

using namespace std;

class testClassName {
    int a = 0;
};

std::istream& func8_1(std::istream &is) {
    string buf;
    cout << "please input str , input end will break";
    while (is >> buf) {
        if (buf == "end") break;
        std::cout << buf << std::endl;
    }
    is.clear(); // 将流的状态设置为有效
    return is;
}

void readFileToVec(const string& filename, std::vector<string>& vec) {
    ifstream ifs(filename);  // todo: you must include #include <fstream>
    if (ifs) {
        string buf;
        while (std::getline(ifs, buf)) {
            vec.push_back(buf);
        }
    }
}
struct PersonInfo{
    string name;
    vector<string> phones;
};

// g11 Sales_data.cpp ex8main.cpp -o main && ./main "./data/test_book.txt" "./data/test_book_out.txt"
int main(int argc, char **argv) {
    std::cout << "\n case: 打印 cpp 变量类型 8.1.word_transformation_bad.txt" << std::endl;
    testClassName item;
    std::cout << "typeid(testClassName): " << typeid(item).name() << std::endl;  // typeid(testClassName): 13testClassName

    std::cout << "\n case: 测试打印流的状态" << std::endl;
    auto old_state = std::cin.rdstate();
    std::cout << "std::cin.rdstate(): " << typeid(old_state).name() << std::endl; // std::cin.rdstate(): j

    std::cout << "\n习题 ------ 8.1" << std::endl;
    /** 报错真难看懂
        ex8.cpp:31:23: note: in implicit copy constructor for 'std::__1::basic_istream<char>' first required here
        std::istream is = func8_1(std::cin);
                          ^
        /Library/Developer/CommandLineTools/SDKs/MacOSX.sdk/usr/include/c++/v1/ios:312:5: note: declared private here
        ios_base(const ios_base&); // = delete;
        ^
     */
    // std::istream is = func8_1(std::cin);
    try {
        std::istream& is = func8_1(std::cin);
        std::cout << is.rdstate() << std::endl; // 不清楚是0，清除后是4，4代表1个
    } catch (exception e) {
        std::cout << " 已报错：" << e.what() << std::endl;
    }

    std::cout << "\n 章节 ------------------ 8.word_transformation_bad.txt 文件输入输出" << std::endl;
    std::cout << "\n习题 ------ 8.4" << std::endl;
    std::vector<string> fileContent;
    readFileToVec("./data/test_file.txt", fileContent);
    for (const auto &range : fileContent) {
        std::cout << range << std::endl;
    }

    std::cout << "\n习题 ------ 8.5 从命令行读取参数" << std::endl;
    ifstream ifs(argv[1]);  // ./data/test_book.txt
    cout << "argc len: " << argc << " args: " << argv[1] << endl;
    startMain(ifs, std::cout);
    ifs.close();

    std::cout << "\n习题 ------ 8.7 从命令行读取参数 读到文件 输出到文件" << std::endl;
    std::ifstream ifs1(argv[1]);  // ./data/test_book.txt
    std::ofstream ofs1(argv[2]);  // ./data/test_book_out.txt
    cout << "argc len: " << argc << " args: " << argv[2] << endl;
    startMain(ifs1, ofs1);

    std::cout << "\n习题 ------ 8.8 从命令行读取参数 读到文件 追加到文件" << std::endl;
    std::ifstream ifs2(argv[1]);  // ./data/test_book.txt
    std::ofstream ofs2(argv[2], ofstream::app);  // ./data/test_book_out.txt
    cout << "argc len: " << argc << " args: " << argv[2] << endl;
    startMain(ifs2, ofs2);

    std::cout << "\n习题 ------ 8.9 istringstream" << std::endl;
    std::istringstream iss("hello world1 \n hello world2") ;
    func8_1(iss);

    std::cout << "\n习题 ------ 8.10 vec内容放入iss中" << std::endl;
    for (const auto &range : fileContent) {
        std::istringstream iss(range);
        string world;
        while (iss >> world) {
            std::cout << world << " - ";
        }
        std::cout << std::endl;
    }

    std::cout << "\n习题 ------ 8.11 重写循环" << std::endl;
    string line, word;
    vector<PersonInfo> persons;
    std::fstream f_person_in("./data/test_person.txt");
    istringstream record;
    while(std::getline(f_person_in, line)) {
        record.clear();
        record.str(line);
        PersonInfo info;
        record >> info.name;
        while(record >> word) {
            info.phones.push_back(word);
        }
        persons.push_back(info);
    }
    for (const auto &person : persons) {
        cout << person.name << " phones num: " << person.phones.size() << endl;
    }

    std::cout << "\n习题 ------ 8.11 重写循环" << std::endl;
    return 0;
};