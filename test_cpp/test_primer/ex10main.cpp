#include <iostream>
#include <iterator>
#include <vector>
#include <cstring>
#include <stdexcept>

// current file include
#include <algorithm> // 俗称标准库算法p342 copy count
#include <numeric> // accumulate
#include <iterator> // back_inserter
#include <list>
#include <sstream>
#include <fstream>

// #include "ex7.h"
#include "Sales_data.h"
#include "Sales_item.h"

using namespace  std;
using namespace placeholders;


// from ex 10.9
void elimdups(std::vector<std::string> &vs) {
    std::sort(vs.begin(), vs.end());
    auto new_end = std::unique(vs.begin(), vs.end());
    vs.erase(new_end, vs.end());
}

// ex10.18
void biggies_partition(std::vector<std::string> &vs, std::size_t sz) {
    elimdups(vs);
    auto pivot = partition(vs.begin(), vs.end(), [sz](const std::string &s){
        return s.size() >= sz;}
    );
    for(auto it = vs.cbegin(); it != pivot; ++it)
        std::cout << *it << " ";
}

// ex10.19
void biggies_stable_partition(std::vector<std::string> &vs, std::size_t sz) {
    elimdups(vs);
    auto pivot = stable_partition(vs.begin(), vs.end(), [sz](const std::string& s){
        return s.size() >= sz;
    });
    for(auto it = vs.cbegin(); it != pivot; ++it)
        std::cout << *it << " ";
}

// define some functions
void elimDupsWords(vector<int> &words) {
    sort(words.begin(), words.end());
    auto end_unique = unique(words.begin(), words.end());
    words.erase(end_unique, words.end());
}
void elimDupsWords(vector<string> &words) {
    sort(words.begin(), words.end());
    auto end_unique = unique(words.begin(), words.end());
    words.erase(end_unique, words.end());
}

bool isShorter(const string &s1, const string &s2) {
    return s1.size() < s2.size();
}

// ex10.22
bool check_size(std::string word, std::size_t sz) {
    return word.size() <= sz;
}


// define some variables
std::vector<int> test_int_vec;
std::vector<std::string> test_string_vec;
std::list<int> test_int_list;

std::vector<int> int_vec{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
std::vector<int> int_du_vec{0, 1, 2, 3, 4, 5, 5, 5, 5, 5};
std::vector<std::string> str_vec{
        "the", "quick", "red", "fox", "jumps", "over", "the", "slow", "red", "turtle"
};

// define some functions
// g11 ex10main.cpp -o main && ./main
// 会报重复编译错误：g11 ex10main.cpp -o main && ./main
int main() {
    std::cout << "\n习题 ------ 10.1 测试函数" << std::endl;
    auto c3= count(int_vec.begin(), int_vec.end(), 3);
    cout << c3 << endl;

    std::cout << "\n习题 ------ 10.word_transformation_bad.txt 测试函数" << std::endl;
    list<int> int_li{0, 1, 2, 3, 4, 5, 5, 5, 5, 5};
    auto c5= count(int_li.begin(), int_li.end(), 5);
    cout << "count(5): " << c5 << endl;

    std::cout << "\n章节 ------------------ 10.word_transformation_bad.txt 测试函数 equal" << std::endl;
    auto iseq = equal(int_vec.begin(), int_vec.end(), int_li.begin());
    cout << iseq << endl;

    std::cout << "\n习题 ------ 10.word_transformation_bad.txt 测试函数 " << std::endl;
    auto res = accumulate(int_vec.begin(), int_vec.end(), 0);
    cout << "acc(int_vec): " << res << endl;

    std::cout << "\n习题 ------ 10.3 测试函数 " << std::endl;
    vector<double> double_vec{0, 1, 2, 3, 4, 5, 5, 5, 5, 5, 0.1};
    auto resd = accumulate(double_vec.begin(), double_vec.end(), 0);
    cout << "acc(double_vec): " << resd << endl;

    vector<int> int_ep_vec;
    auto it = back_inserter(int_ep_vec);
    *it = 40;
    cout << " size of vec: " << int_ep_vec.size() << endl;

    int a1[] = { 0, 1, 2, 3, 4};
    int a2[sizeof(a1)/sizeof(*a1)];
    auto copy_ret = copy(begin(a1), end(a1), a2);
    for (auto start = begin(a2); start != end(a2); ++start) {
        cout << *start << " ";
    }
    cout << endl;

    list<int> li = {0, 1, 2, 3};
    vector<int> repalce_copy_vec;
    auto rplace_copy = replace_copy(li.begin(), li.end(), back_inserter(repalce_copy_vec), 0, 100);
    for (const auto &item : repalce_copy_vec) {
        cout << item << " ";
    }

    std::cout << "\n习题 ------ 10.6 测试函数 " << std::endl;
    vector<int> fill_n_vec{ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
    fill_n(fill_n_vec.begin(), fill_n_vec.size(), 0);
    for (auto i : fill_n_vec)
        cout << i << " ";
    cout << endl;

    std::cout << "\n习题 ------ 10.8 " << std::endl;
    vector<int> empty_vec_for_insert;
    stringstream stringin("1 word_transformation_bad.txt 3 4 5");
    int int_val;
    while (stringin >> int_val) {
        cout << int_val << " ";
        empty_vec_for_insert.push_back(int_val);
    }
    cout << endl;
    list<int> copy_em_li;
    // 错误：copy(empty_vec_for_insert.begin(), empty_vec_for_insert.end(), back_inserter(copy_em_li));
    copy(empty_vec_for_insert.begin(), empty_vec_for_insert.end(), back_inserter(copy_em_li));
    for (const auto &item : copy_em_li) {
        cout << item << " ";
    }
    cout << endl;

    std::cout << "\n习题 ------ 10.9 " << std::endl;
    vector<int> dis_int_val{1, 2, 4, 3, 5, 8};
    println(dis_int_val, "原始的vec");
    elimDupsWords(int_vec);
    println(dis_int_val, "now");

    std::cout << "\n习题 ------ 10.11 " << std::endl;
    vector<string> dis_str_val{"hi", "sun", "ying", "dong", "hi"};
    println(dis_str_val, "原始的vec");
    elimDupsWords(dis_str_val);
    stable_sort(dis_str_val.begin(), dis_str_val.end(), isShorter);
    println(dis_str_val, "sort的vec");

    println("\n习题 ------ 10.13 ");
    // size <= 6 : 10
    // size <= 3 : 5

    auto predicate = [] (const string &s) { return s.size() >= 5; };
    auto pivot = partition(str_vec.begin(), str_vec.end(), predicate);
    for (auto it = str_vec.cbegin(); it != pivot; ++it) {
        cout << *it << " ";
    }
    cout << endl;
    for_each(str_vec.cbegin(), str_vec.cend(), [](const string& s) { cout << s << " "; });
    println("\n习题 ------ 10.18 ");
    std::vector<std::string> v1(str_vec);
    biggies_partition(v1, 4);
    std::cout << std::endl;

    println("\n习题 ------ 10.19 ");
    std::vector<std::string> v2(str_vec);
    biggies_stable_partition(v2, 4);
    std::cout << std::endl;

    println("\n习题 ------ 10.20 ");
    const int sz = 6;
    auto ret = count_if(str_vec.begin(), str_vec.end(), [sz](const string &s) { return s.size() >= sz;});
    println("size: " + to_string(ret));

    println("\n习题 ------ ex10.21 ");
    int i = 7;
    auto check_and_decrement = [&i]() { return i > 0 ? !--i: !i; };
    std::cout << "ex10.21: ";
    while(!check_and_decrement())
        std::cout << i << " ";
    std::cout << i << std::endl;

    println("\n习题 ------ ex10.22 ");
    // using namespace std::placeholders;
    auto check6 = bind(check_size, _1, 3);
    // auto ret = count_if(str_vec.begin(), str_vec.end(), [sz](const string &s) { return s.size() >= sz;});
    ret = count_if(str_vec.begin(), str_vec.end(), check6);
    println("size: " + to_string(ret));

    println("\n习题 ------ ex 测试partition ");
    vector<int> pa_vec{0, 1, 2, 3, 4, 5};
    auto last = partition(pa_vec.begin(), pa_vec.end(), [](const int i) {
        return i <= 3;
    });
    for_each(pa_vec.begin(), last, [](const int s) { cout << s << " "; });
    println();

    println("\n章节 ------------------ 10.4");
    vector<int> duplicate_vec{0, 0, 1, 1, 2, 3, 3};
    vector<int> test_int_vec;
    test_int_vec = duplicate_vec;
    print("现有元素为");
    auto print_int_lambda = [](const int i) { std::cout << i << " "; };
    for_each(unique(test_int_vec.begin(), test_int_vec.end()), test_int_vec.end(), print_int_lambda);

    println("\n习题 ------ ex10.27 ");
    test_int_vec.clear();
    std::unique_copy(duplicate_vec.begin(), duplicate_vec.end(), back_inserter(test_int_vec));
    println(duplicate_vec);
    println(test_int_vec);

    println("\n习题 ------ ex10.28 ");
    list<int> re2{-1}, re3{-1}, re4{-1};
    copy(pa_vec.cbegin(), pa_vec.cend(), front_inserter(re2)); // 5 4 3 word_transformation_bad.txt 1 0 -1
    copy(pa_vec.cbegin(), pa_vec.cend(), back_inserter(re3)); // -1 0 1 word_transformation_bad.txt 3 4 5
    copy(pa_vec.cbegin(), pa_vec.cend(), inserter(re4, re4.begin())); // 0 1 word_transformation_bad.txt 3 4 5 -1
    println(re2);
    println(re3);
    println(re4);

    lnPrintln("\n习题 ------ ex10.29 ");
    ifstream file_in("data/test_file.txt"); // todo fstream
    istream_iterator<string> file_it(file_in), eof;
    while (file_it != eof) {
        test_string_vec.push_back(*file_it++);
    }
    println(test_string_vec);

    lnPrintln("\n习题 ------ ex10.30 输入eof");
    cout << "input str to vector(end with eof): ";
    istream_iterator<string> cin_in(cin), eof_int;
    ofstream file_out("data/test_file_out.txt");
    ostream_iterator<string> file_out_it(file_out, " ");    // 每个输入后面跟一个分隔符 " "
    while (cin_in != eof_int) {
        if (*cin_in == "eof") break;
        *file_out_it++ = *cin_in++;
    }

    lnPrintln("\n习题 ------ ex10.30 官方写法 不会消除注释代码没问题");
    // std::vector<int> cin_vec((std::istream_iterator<int>(std::cin)), std::istream_iterator<int>());
    // std::sort(cin_vec.begin(), cin_vec.end());
    // std::copy(cin_vec.cbegin(), cin_vec.cend(), std::ostream_iterator<int>(std::cout, " "));

    lnPrintln("\n习题 ------ ex10.31 官方写法");
    // std::vector<int> cin_unique_copy_vec((std::istream_iterator<int>(std::cin)), std::istream_iterator<int>());
    // std::sort(cin_unique_copy_vec.begin(), cin_unique_copy_vec.end());
    // std::unique_copy(cin_unique_copy_vec.cbegin(), cin_unique_copy_vec.cend(), std::ostream_iterator<int>(std::cout, " "));

    lnPrintln("\n习题 ------ ex10.34 ");
    for (auto it = str_vec.rbegin(); it != str_vec.rend(); it++) {
        print(*it);
    }

    lnPrintln("\n习题 ------ ex10.35 ");
    println(str_vec);
    for (auto it = std::prev(str_vec.cend()); true; --it) {
        std::cout << *it << " ";
        if (it == str_vec.cbegin()) break;
    }

    lnPrintln("\n习题 ------ ex10.36 ");
    auto ret36 = find(int_vec.rbegin(), int_vec.rend(), 0);
    std::cout << std::distance(ret36, int_vec.rend()) << std::endl;
    std::list<int> lst = { 1, 2, 3, 4, 0, 5, 6 };
    auto found_0 = std::find(lst.crbegin(), lst.crend(), 0);
    std::cout << std::distance(found_0, lst.crend()) << std::endl;

    lnPrintln("\n习题 ------ ex10.37 ");
    reverse_copy(int_vec.begin() + 2, int_vec.begin() + 8, back_inserter(test_int_list));
    println(test_int_list);

    // ----------------------------------------------
    // println("\n习题 ------ ex 10.11 ");
    // println("\n章节 ------------------ ex 10.word_transformation_bad.txt");


    return 0;
};
