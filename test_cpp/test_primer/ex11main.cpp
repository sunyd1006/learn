#include <iostream>
#include <iterator>
#include <vector>
#include <cstring>
#include <stdexcept>

// current file include
#include <map>
#include <unordered_map>
#include <unordered_set>
#include <set>
#include <algorithm>
#include <utility>

// #include "ex7.h"
#include "Sales_data.h"
#include "Sales_item.h"
#include "tool.h"
#include <sstream>
#include <fstream>

using namespace std;

//for ex11.4
// auto strip(string str) -> string {
auto strip(string str) -> string const & {
    auto ispunct = [](char c) { return (c == '.' || c == ',') ? true : false; };
    for (auto &ch : str) ch = tolower(ch);
    str.erase(remove_if(str.begin(), str.end(), ispunct), str.end());
    return str;
}

//  ex11_11.cpp
auto less(Sales_data const &lhs, Sales_data const &rhs) {
    return lhs.getIsbn() < rhs.getIsbn();
}

// ex11.33.cpp
std::map<string, string> buildMap(ifstream &map_file) {
    std::map<string, string> trans_map;
    for (string key, value; map_file >> key && getline(map_file, value); )
        // todo ask 为什么不是一个效果？
        // if (value.size() > 1) trans_map[key] = value.substr(1).substr(0, value.find_last_not_of(' '));
       if (value.size() > 1) trans_map.insert({ key, value.substr(1).substr(0, value.find_last_not_of(' ')) });
    return trans_map;
}

const string & transform(const string &s, const std::map<string, string> &m) {
    auto map_it = m.find(s);
    return map_it == m.cend() ? s : map_it->second;
}

void word_transform(ifstream &map, ifstream &input) {
    auto trans_map = buildMap(map);
    for (string text; getline(input, text); ) {
        std::istringstream iss(text);
        for (string word; iss >> word; )
            std::cout << transform(word, trans_map) << " ";
        std::cout << std::endl;
    }
}

class ItemKey{
public:
    ItemKey() { };
    ItemKey(string key, int id):keyName(key), keyId(id) { };
    bool operator<(const ItemKey& dy1) const { return this->keyName < dy1.keyName; }
    // c1 定义类内==，用于 unordered 的map和set
    bool operator==(const ItemKey& out) const { return (this->keyName == out.keyName) && (this->keyId == out.keyId); }
public:
    string keyName;
    int keyId;
};
// 打印类
std::ostream& operator << (std::ostream& os, const ItemKey& item){
    os << "(" << item.keyName << " , " << item.keyId << ")";
    return os;
}

// 用于定义非multi的map和set
bool compareItemKey(const ItemKey& left, const ItemKey& right) {
    return left.keyName < right.keyName;
}

// --------------------------------------------------------
// B1 定义类外hash函数，用于非multi的map和set
class HashClass {
public:
    size_t operator()(const ItemKey &item) const {
        return hash<string>()(item.keyName);
    }
};
// B2 定义类外hash函数，用于非multi的map和set
size_t hashItemKey(const ItemKey& key) {
    return hash<string>()(key.keyName);
}

// c2 定义类外==，用于 unordered 的map和set
bool equalsOp(const ItemKey& left, const ItemKey& right) {
    return (left.keyName == right.keyName);
}


// -----------------------------------
class Person {
public:
    Person(string name, int age) :name(name), age(age) {};
    bool operator==(const Person &B) {
        return (this->age == B.age);
    }
public:
    string name;
    int age;
};
//自定义哈希函数
class hash_fun {
public:
    int operator()(const Person &A) const {
        return A.age;
    }
};

size_t hashPerson(const Person& key) {
    return hash<string>()(key.name);
}

//重载 == 运算符，myset 可以继续使用默认的 equal_to<key> 规则
bool operator==(const Person &A, const Person &B) {
    return (A.age == B.age);
}
//完全自定义比较规则，弃用 equal_to<key>
class mycmp {
public:
    bool operator()(const Person &A, const Person &B) const {
        return (A.name == B.name) && (A.age == B.age) ;
    }
};

// define some variables
std::vector<int> test_int_vec;
std::vector<std::string> test_string_vec;
std::list<int> test_int_list;

std::vector<int> int_vec{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
std::vector<int> int_du_vec{0, 1, 2, 3, 4, 5, 5, 5, 5, 5};
std::vector<std::string> str_vec{
        "the", "quick", "red", "fox", "jumps", "over", "the", "slow", "red", "turtle"
};

// g14 Sales_data.cpp ex11main.cpp -o main && ./main
int main() {
    lnPrintln("\n习题 ------ ex11.3 ");
    ifstream filein("./data/test_file.txt");
    istream_iterator<string> file_it(filein), file_eof;
    set<string> exclude = {"is", "a"};
    map<string, size_t> word_count;
    while (file_it != file_eof) {
        if (exclude.find(*file_it) == exclude.end()) {
            ++word_count[strip(*file_it)];
        }
        ++file_it;
    }
    for (const auto &item : word_count) {
        std::cout << item.first << " -> " << item.second << "   ";
    }

    lnPrintln("\n习题 ------ ex11.9 ");
    // ex 11.9
    std::map<std::string, std::list<std::size_t>> m;

    // ex 11.10
    // can be declared.
    std::map<std::vector<int>::iterator, int> mv;
    std::map<std::list<int>::iterator, int> ml;
    std::vector<int> vi;
    mv.insert(std::pair<std::vector<int>::iterator, int>(vi.begin(), 0));

    // TODO 注意 list 未定义 <
    // but when using this one the compiler complained that
    // error: no match for 'operator<' in '__x < __y'
    // std::list<int> li;
    // ml.insert(std::pair<std::list<int>::iterator, int>(li.begin(), 0));

    lnPrintln("\n习题 ------ ex11.11 ");
    using Less = bool (*)(Sales_data const&, Sales_data const&);
    std::multiset<Sales_data, Less> bookstore(Less);

    lnPrintln("\n习题 ------ ex11.13 pair插入方式");
    vector<pair<string, int>> test_pair_vec;
    test_pair_vec.push_back(make_pair("sun", 1));
    test_pair_vec.push_back(std::make_pair("ying", 2));
    test_pair_vec.push_back({ "dong", 3});
    test_pair_vec.emplace_back("yeah", 4); //!! easiest way.
    for (const auto &item : test_pair_vec) {
        cout << item.first << " " << item.second << " ";
    }
    println();

    lnPrintln("\n习题 ------ ex11.17");
    test_string_vec.push_back("test_string_vec");
    multiset<string> str_multiset{"mutiset", "mutiset"};
    copy(test_string_vec.begin(), test_string_vec.end(), inserter(str_multiset, str_multiset.end())); // legal
    println(str_multiset);
    // todo // illegal, no `push_back` in `set`.
    // copy(test_string_vec.begin(), test_string_vec.end(),  back_inserter(str_multiset));
    // print(str_multiset);

    copy(str_multiset.begin(), str_multiset.end(), inserter(test_string_vec, test_string_vec.end())); // legal.
    std::cout << test_string_vec.size() ;
    println(test_string_vec);
    copy(str_multiset.begin(), str_multiset.end(), back_inserter(test_string_vec)); // legal.
    println(test_string_vec);

    str_multiset.insert("insertByFunc");
    println(str_multiset);


    lnPrintln("\n习题 ------ ex11.23 ");
    istringstream iStr("sun hao hu wenrui sun yindong");
    string fname, lname;
    map<string, vector<string>> count_map;
    multimap<string, string> count_multi;
    while (iStr >> fname >> lname) {
        count_map[fname].push_back(lname);
        count_multi.insert({fname, lname});
    }
    for (const auto &item : count_map) {
        std::cout << item.first << " -> ";
        println(item.second);
    }
    for (const auto &item : count_multi) {
        std::cout << item.first << " -> " << item.second << "   ";
    }

    lnPrintln("\n习题 ------ ex11.26 ");
    std::map<int, std::string> ex26 = { { 1,"ss" },{ 2,"sz" } };
    using KeyType = std::map<int, std::string>::key_type;
    std::cout << "type to subscript: " << typeid(KeyType).name() << std::endl;
    std::cout << "returned from the subscript operator: " << typeid(decltype(ex26[1])).name() << std::endl;

    lnPrintln("\n习题 ------ ex11.31 ");
    multimap<string, string> ex31map{{"sun", "book1"}, {"sun", "book2"}, {"sun", "book3"}, {"hu", "book1"}};
    string deleteName("sun");
    ex31map.erase(deleteName);
    for (const auto &item : ex31map) {
        std::cout << item.first << " -> " << item.second << "   ";
    }
    println();

    ex31map.emplace("sun", "book1");
    ex31map.emplace("sun", "book2");
    ex31map.emplace(make_pair("sun", "book3"));
    string deleteBookName("book2");
    println("但是上面删除了所有sun的book 我只想删除 book2");
    for (const auto &item : ex31map) {
        std::cout << item.first << " -> " << item.second << "   ";
    }
    println();
    for (auto pos = ex31map.equal_range(deleteName); pos.first != pos.second; ++pos.first) {
        if (pos.first->second == deleteBookName) {
           ex31map.erase(pos.first);
            break;
        }
    }
    for (const auto &item : ex31map) {
        std::cout << item.first << " -> " << item.second << "   ";
    }

    lnPrintln("\n习题 ------ ex11.31 ");
    ifstream ifs_map("./data/word_transformation_bad.txt"), ifs_content("./data/given_to_transform.txt");
    if (ifs_map && ifs_content) word_transform(ifs_map, ifs_content);
    else std::cerr << "can't find the documents." << std::endl;

    lnPrintln("\n习题 ------ ex11.32 ItemKey作为key，要自定义hash和comp函数 ");
    map<ItemKey, string, decltype(compareItemKey) *> ex32mapNotDefinedInClass(compareItemKey);
    ex32mapNotDefinedInClass.insert({ItemKey("sun", 1), "sun"});
    ex32mapNotDefinedInClass.insert({ItemKey("hu", 1), "hu"});

    map<ItemKey, string> ex32mapDefinedInClass;
    ex32mapDefinedInClass.insert({ItemKey("sun", 1), "sun"});
    ex32mapDefinedInClass.insert({ItemKey("hu", 1), "hu"});

    for (const auto &item : ex32mapNotDefinedInClass) {
        std::cout << "item: " << item.first << " id: " << item.second << endl;
    }
    for (const auto &item : ex32mapDefinedInClass) {
        std::cout << "item: " << item.first << " id: " << item.second << endl;
    }

    ItemKey sun("sun", 1), hu("hu", 1);

    // B1 使用了类内less定义, 用于unorder的map或者set声明
    unordered_map<ItemKey, string, HashClass> un_ne_ex32map;
    unordered_set<ItemKey, HashClass> un_ne_ex32set;
    string tmpStr("tmpstr");
    un_ne_ex32map[sun] = tmpStr;
    // un_ne_ex32map[hu] = tmpStr;   // todo 此处可以

    un_ne_ex32map.emplace(hu, tmpStr); // todo 为什么不行呢
    un_ne_ex32map.insert({hu, tmpStr}); // todo 为什么不行呢
    un_ne_ex32map.insert(make_pair(hu, tmpStr)); // todo 为什么不行呢
    un_ne_ex32map.insert(unordered_map<ItemKey, string, HashClass>::value_type(hu, tmpStr)); // todo 为什么不行呢

    un_ne_ex32set.insert(sun);
    un_ne_ex32set.insert(hu);

    lnPrintln("打印 un_ne_ex32map");
    for (const auto &item : ex32mapNotDefinedInClass) {
        std::cout << "item: " << item.first << " id: " << item.second << " ";
    }
    lnPrintln("打印 un_ne_ex32set");
    println(un_ne_ex32set);

    // B2 使用了类外less定义, 用于unorder的map或者set声明
    unordered_map<ItemKey, string , decltype(hashItemKey) *, decltype(equalsOp) *> un_wai_ex32map(42, hashItemKey, equalsOp);
    unordered_set<ItemKey , decltype(hashItemKey) *, decltype(equalsOp) *> un_wai_ex32set(42, hashItemKey, equalsOp);
    un_wai_ex32map[sun] = tmpStr; //     un_wai_ex32map.emplace(hu, 1); // todo 为什么不行呢
    un_wai_ex32map[hu] = tmpStr;
    un_wai_ex32set.insert(sun);
    un_wai_ex32set.insert(hu);
    lnPrintln("打印 un_wai_ex32map");
    for (const auto &item : ex32mapNotDefinedInClass) {
        std::cout << "item: " << item.first << " id: " << item.second << " ";
    }
    lnPrintln("打印 un_ne_ex32set");
    println(un_ne_ex32set);

    //使用自定义的 hash_fun 哈希函数，比较规则仍选择默认的 equal_to<key>,前提是必须重载 == 运算符
    // std::unordered_set<Person, decltype(hashPerson) *> myset1(42, hashPerson);
    // myset1.insert({"zhangsan", 40});
    // myset1.insert({"lisi", 40});
    // myset1.insert({"lisi", 30});
    //
    // //使用自定义的 hash_fun 哈希函数，以及自定义的 mycmp 比较规则
    // std::unordered_set<Person, hash_fun, mycmp> myset2;
    // myset2.insert({"zhangsan", 40});
    // myset2.insert({"zhangsan", 40});
    // myset2.insert({"lisi", 40});
    // myset2.insert({"lisi", 30});

    std::unordered_map<Person, int, hash_fun, mycmp> myset2;
    // myset2[Person("zhangsan", 40)] = 1;          // 用下标可以
    // myset2[Person("zhangsan", 40)] = 1;
    // myset2[Person("lisi", 40)] = 1;
    // myset2[Person("lisi", 30)] = 1;


    // myset2.insert(make_pair({"zhangsan", 40}, 1));
    // myset2.insert(make_pair({"zhangsan", 40}, 1));
    // myset2.insert(make_pair({"lisi", 40}, 1));
    // myset2.insert(make_pair({"lisi", 30}, 1));


    // cout << "myset1:" << endl;
    // for (auto iter = myset1.begin(); iter != myset1.end(); ++iter) {
    //     cout << iter->name << " " << iter->age << endl;
    // }
    // cout << "myset2:" << endl;
    // for (auto iter = myset2.begin(); iter != myset2.end(); ++iter) {
    //     cout << iter->name << " " << iter->age << endl;
    // }

    // ----------------------------------------------
    // lnPrintln("\n习题 ------ ex10.36 ");
    // lnPrintln("\n章节 ------------------ 10.word_transformation_bad.txt");

    return 0;




};
