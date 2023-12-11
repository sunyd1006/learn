#include <iostream>
#include <iterator>
#include <cstring>
#include <stdexcept>

#include <vector>
#include <list>
#include <deque>
#include <forward_list>

#include <stack>

// #include "ex7.h"
using namespace  std;

void remove_odds(std::forward_list<int>& list) {
    auto is_odd = [] (int i) {return i & 0x1; };
    list.remove_if(is_odd); // 内置函数
}

void insert_two_string(std::forward_list<string> &list, const string& str1, const string& str2) {
    auto prev = list.before_begin();
    for (auto cur = list.begin(); cur != list.end(); cur++) {
        if (*cur == str1) {
            list.insert_after(cur, str2);
            return;
        }
        prev = cur;
    }
    list.insert_after(prev, str2);
}

void replace_with(string &s, const string &old, const string& newVal) {
    for (auto cur = s.begin(); cur <= s.end() - old.size(); ) {
        if (old == string{cur, cur + old.size()}) {
            cout << " before erase: " << s << endl;
            cur = s.erase(cur, cur + old.size());
            cout << " after erase: " << s << endl;
            cur = s.insert(cur, newVal.begin(), newVal.end()); // s.insert(cur, str) 没有API
            // cur = s.insert(cur, newVal); // s.insert(cur, str) 没有API
            cur += newVal.size();
        } else ++cur;
    }
}

// define some functions
// NOTE: must g11, beacause g++ uses c03 by default
int main() {
    std::cout << "\n习题 ------ 9.13 元素赋值转换" << std::endl;
    list<int> ilst(5, 4);
    vector<int> ivc(5, 5);

    // from list<int> to vector<double>
    vector<double> dvc(ilst.begin(), ilst.end());
    cout << " from list<int> to vector<double> before: ";
    for (auto i : ilst) cout << i << " ";
    cout << endl;
    cout << " from list<int> to vector<double> :";
    for (auto d : dvc) cout << d << " ";
    cout << endl;

    // from vector<int> to vector<double>
    cout << " from vector<int> to vector<double> before: ";
    for (auto i : ilst) cout << i << " ";
    vector<double> dvc2(ivc.begin(), ivc.end());
    for (auto i : ivc) cout << i << " ";
    cout << endl;
    cout << " from vector<int> to vector<double> : ";
    for (auto d : dvc2) cout << d << " ";

    std::cout << "\n习题 ------ 9.14 char* 的list 赋值给 vector" << std::endl;
    std::list<const char*> li914{"sun", "yin", "dong"};
    for (const auto &ofli : li914) {
        std::cout << ofli << " ";
    }
    // std::vector<std::string>> vecOfli(li914.begin(), li914.end());
    std::vector<std::string> vecOfli;
    vecOfli.assign(li914.cbegin(), li914.cend());
    for (const auto &ofli : vecOfli) {
        std::cout << ofli << std::endl;
    }
    std::cout << std::endl;

    std::cout << "\n习题 ------ 9.16 " << std::endl;
    std::list<int>      li{ 1, 2, 3, 4, 5 };
    std::vector<int>    vec2{ 1, 2, 3, 4, 5 };
    std::vector<int>    vec3{ 1, 2, 3, 4 };
    std::cout << (std::vector<int>(li.begin(), li.end()) == vec2 ? "true" : "false") << std::endl;
    std::cout << (std::vector<int>(li.begin(), li.end()) == vec3 ? "true" : "false") << std::endl; // FALSE

    std::cout << "\n习题 ------ 9.18 " << std::endl;
    deque<string> input;
    cout << "input str to vector(end with eof): ";
    for (string str; cin >> str; input.push_back(str)) {
        if (str == "eof") break;
    }
    for (auto iter = input.cbegin(); iter != input.cend(); ++iter)
        cout << *iter << endl;

    std::cout << "\n习题 ------ 9.19 " << std::endl;
    list<string> inputLi;
    cout << "input str to vector(end with eof): ";
    for (string str; cin >> str; inputLi.push_back(str))
        if (str == "eof") break;
    for (auto iter = inputLi.cbegin(); iter != inputLi.cend(); ++iter)
        cout << *iter << endl;

    std::cout << "\n习题 ------ 9.25 " << std::endl;
    int ia[] = { 0, 1, 1, 2, 3, 5, 8, 13, 21, 55, 89 };
    // init
    vector<int> vec(ia, end(ia));
    list<int> lst(ia, end(ia));
    // remove odd value
    for(auto it = lst.begin();  it != lst.end(); )
        if(*it & 1) it = lst.erase(it);
        else ++it; // it 注意要 ++

    // remove even value
    for(auto it = vec.begin(); it != vec.end(); ) {
        if (*it & 0) it = vec.erase(it);
        else ++it;
    }

    // print
    cout << "list : ";
    for(auto i : lst)   cout << i << " ";
    cout << "\nvector : ";
    for(auto i : vec)   cout << i << " ";
    cout << std::endl;

    std::cout << "\n习题 ------ 9.27 " << std::endl;
    forward_list<int> data = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    remove_odds(data);
    for (const auto &item : data) {
        cout << item << " ";
    }

    forward_list<string> liStr = {"hello", "sun", "yin", "dong"};
    insert_two_string(liStr, "hello","sir" );
    for (const auto &item : liStr) {
        cout << item << " ";
    }

    std::cout << "\n习题 ------ 9.29" << std::endl;
    vector<int> test_resize_vec{1, 2, 3, 4, 5};
    test_resize_vec.resize(10);
    for (const auto &item : test_resize_vec) {
        cout << item << " ";
    }
    cout << endl;
    test_resize_vec.resize(3);
    for (const auto &item : test_resize_vec) {
        cout << item << " ";
    }

    std::cout << "\n习题 ------ 9.32" << std::endl;
    // vector<int> test_iter{0, 1, word_transformation_bad.txt, 3};
    list<int> test_iter{0, 1, 2, 3};
    auto iter = test_iter.begin();
    while (iter != test_iter.end()) {
        iter = test_iter.insert(iter, *iter++);
        // iter += word_transformation_bad.txt; // vector 支持随机访问
        advance(iter, 2); // 不支持随机访问， list, forward_list
    }
    for (const auto &item : test_iter) {
        cout << item << " ";
    }

    std::cout << "\n习题 ------ 9.33" << std::endl;
    vector<int> v{ 1, 2, 3, 4, 5, 6, 7, 8, 9 };
    auto begin = v.begin();
    while (begin != v.end()) {
        ++begin;
        begin = v.insert(begin, 42);
        // /*begin = */ v.insert(begin, 42);
        ++begin; // 可能会失效
    }
    for (auto i : v)
        cout << i << " ";

    std::cout << "\n习题 ------ 9.34" << std::endl;
    vector<int> data_insert{ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
    for(auto cur = data_insert.begin(); cur != data_insert.end(); ++cur)
        if(*cur & 0x1)
            cur = data_insert.insert(cur, *cur*10), ++cur;

    for (auto i : data_insert)
        cout << i << " ";

    std::cout << "\n习题 ------ 9.38 vec_who_to_resize" << std::endl;
    std::vector<std::string> vec_who_to_resize;
    cout << "input str to vector(end with eof): ";
    for (std::string buffer; std::cin >> buffer; vec_who_to_resize.push_back(buffer)) {
        if (buffer == "eof") break;
        std::cout << " cur capacity: " << vec_who_to_resize.capacity() << " size: " << vec_who_to_resize.size() << std::endl;
    }

    std::cout << "\n习题 ------ 9.41" << std::endl;
    vector<char> vec_to_init_str{ 'p', 'e', 'z', 'y' };
    string str(vec_to_init_str.cbegin(), vec_to_init_str.cend());
    cout << str << endl;

    std::cout << "\n习题 ------ 测试 字符串结尾" << std::endl;
    char *cp = "hello world"; // 会已 \0结尾
    char noNull[] = {'h', 'e' }; // 不会以 \0 结尾
    cout << cp << " " << noNull << endl; // hello world heq0U

    std::cout << "\n习题 ------ 9.43" << std::endl;
    string test_repalce_with{ "To drive straight thru is a foolish, tho courageous act." };
    replace_with(test_repalce_with, "tho", "though");
    replace_with(test_repalce_with, "thru", "through");
    cout << test_repalce_with << endl;

    std::cout << "\n习题 ------ 9.47 " << std::endl;
    string numbers{ "123456789" };
    string alphabet{ "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ" };
    string target{ "ab2c3d7R4E6" };
    cout << "numeric characters: ";
    for (int pos = 0; (pos = target.find_first_of(numbers, pos)) != string::npos; ++pos)
        cout << target[pos] << " ";
    cout << "\nalphabetic characters: ";
    for (int pos = 0; (pos = target.find_first_of(alphabet, pos)) != string::npos; ++pos)
        cout << target[pos] << " ";
    cout << endl;

    std::cout << "\n习题 ------ 9.52 " << std::endl;
    string expression{ "This is (pezy)." };
    bool bSeen = false;
    stack<char> stk;
    for (const auto &s : expression){
        if (s == '(') { bSeen = true; continue; }
        else if (s == ')') bSeen = false;

        if (bSeen) stk.push(s);
    }

    string repstr;
    while (!stk.empty()) {
        repstr += stk.top();
        stk.pop();
    }
    expression.replace(expression.find("(")+1, repstr.size(), repstr);
    cout << expression << endl;

    std::cout << "\n 章节 ------------------ 8.word_transformation_bad.txt" << std::endl;
    std::cout << "\n习题 ------ 8.11" << std::endl;
    return 0;
};
