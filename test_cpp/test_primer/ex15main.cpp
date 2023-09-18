#include <cstring>
#include <iostream>
#include <iterator>
#include <map>
#include <stdexcept>
#include <vector>

// current file include
#include <fstream>
#include <functional>
#include <sstream>

#include "Sales_data.h"
#include "Sales_item.h"
#include "ex15.h"
#include "tool.h"

using namespace std;

// define current function

// ex15.12
void print_debug(const Quote &q) { std::cout << q.debug() << std::endl; }

// g14 Sales_data.cpp ex15main.cpp -o main && ./main
int main() {
    lnPrintln("\n习题 ------ ex15.1 ");
    Quote ex1;
    print_total_info(ex1, 10);

    lnPrintln("\n习题 ------ ex15.5 ");
    BulkQuote ex5("ex5", 10, 10, 0.1);
    cout << ex5.net_price(10) << endl;
    print_total_info(ex5, 10);
    print_total_info(ex1, 10);

    lnPrintln("\n习题 ------ ex15.7 ");
    LimitQuote ex7("ex7", 10, 10, 0.1);
    print_total_info(ex7, 11);

    lnPrintln("\n习题 ------ ex15.9 ");
    BulkQuote bulk_quote("bulk_quote_1", 10.10, 5, 0.5);

    // The pointer is of static type Quote, but it's dynamic type is Bulk Quote
    // Because of polymorphism the Bulk Quote implementation of the net_price()
    // method gets called.
    Quote *quote_pointer = &bulk_quote;
    cout << quote_pointer->net_price(5) << endl;

    // The reference is of static type Quote, but it's dynamic type is Bulk Quote
    // Like with the pointer, the Bulk Quote implementation of the net_price()
    // method gets called.
    Quote &quote_reference = bulk_quote;
    cout << quote_reference.net_price(5) << endl;

    // The static type of this variable is Quote. Here the Bulk Quote object
    // gets upcasted. The Quote part of bulk_quote gets copied into quote, but
    // the rest is not handled. Because of the cast the Quote implementation of
    // the net_price() method gets called.
    Quote quote = bulk_quote;
    cout << quote.net_price(5) << endl;

    lnPrintln("\n习题 ------ ex15.11 ");
    LimitQuote lq11("lq11", 10, 100, 0.1);
    BulkQuote bq11("bq11", 10, 5, 0.5);
    Quote q11("quote11", 1);

    Quote *base = &lq11;
    cout << base->debug() << endl;
    println();

    base = &bq11;
    cout << base->debug() << endl;
    println();

    base = &q11;
    cout << base->debug() << endl;
    println();

    lnPrintln("\n习题 ------ ex15.17 ");
    // DiscQuote disc17; // ERROR: 定义一个抽象类，应该报错

    lnPrintln("\n习题 ------ ex15.26 ");
    Quote ex27_default, ex27("ex27", 10), ex27_copy_cons = ex27_default;
    /*
        Call function (Quote): default constructor
        Call function (Quote): init constructor
        Quote: copy constructing
     */

    println();
    BulkQuote ex27_DiscQuote("ex27", 10, 10, 0.1);
    BulkQuote ex27_copy_cons_disc = ex27_DiscQuote;
    /*
        Call function (Quote): init constructor
        Call function (DiscQuote): init constructor
        Call function (BulkQuote): init constructor
        Quote: copy constructing
        DiscQuote : copy constructor
        BulkQuote : copy constructor
     */

    lnPrintln("\n习题 ------ ex15.28 ");
    std::vector<Quote> ex28vec;
    string price_info;
    for (unsigned i = 1; i != 10; ++i) {
        println_line();
        ex28vec.push_back(BulkQuote("sss", i * 10.1, 10, 0.3));
        price_info.append(std::to_string(i * 10.1) + "  ");
    }

    double total = 0;
    string item_info;
    for (const auto &item : ex28vec) {
        total += item.net_price(20);
        item_info.append(std::to_string(item.net_price(20)) + string("    "));
    }
    println(price_info);
    println(item_info);
    std::cout << total << std::endl;

    lnPrintln("\n习题 ------ ex15.29 ");
    std::vector<std::shared_ptr<Quote>> ex29;
    for (unsigned i = 1; i != 10; ++i) {
        println_line();
        ex29.push_back(std::make_shared<BulkQuote>(BulkQuote("sss", i * 10.1, 10, 0.3)));
    }

    double total_p = 0;
    item_info.clear();
    for (auto item : ex29) {
        total_p += item->net_price(20);
        item_info.append(std::to_string(item->net_price(20)) + string("    "));
    }
    println(price_info);
    println(item_info);
    std::cout << total_p << std::endl;

    lnPrintln("\n习题 ------ ex15.30 ");
    Basket basket;
    for (unsigned i = 0; i != 10; ++i) {
        basket.addItem(BulkQuote("Bible", 20.6, 20, 0.3));
    }

    for (unsigned i = 0; i != 10; ++i) {
        basket.addItem(BulkQuote("C++Primer", 30.9, 5, 0.4));  // add item
    }

    for (unsigned i = 0; i != 10; ++i) {
        basket.addItem(Quote("CLRS", 40.1));  // add item
    }

    cout << basket.getTotalPrice() << endl;
    cout << (20.6 * 10 + 10 * 30.9 * 0.6 + 40.1 * 10) << endl;

    lnPrintln("\n\n\n习题 ------ ex15.36 ");
    Query ex36 = Query("fiery") & Query("bird") | Query("wind");
    cout << "rep of result: " << ex36 << endl;
    /*
        Call function (WordQuery):, init constructor
        Call function (Query):, init constructor, fiery
        Call function (WordQuery):, init constructor
        Call function (Query):, init constructor, bird
        Call function (BinaryQuery):, init constructor,  ( fiery & bird )
        Call function (AndQuery):, init constructor
        Call function (WordQuery):, init constructor
        Call function (Query):, init constructor, wind
        Call function (BinaryQuery):, init constructor,  (  ( fiery & bird )  |
       wind ) Call function (OrQuery):, init constructor rep of result:  (  (
       fiery & bird )  | wind )
     */

    lnPrintln("\n习题 ------ ex15.40 ");
    QueryText exFile40("./data/storyDataFile.txt");
    Query query40 = Query("Daddy") | Query("him");
    // Query query40 = Query("Daddy") ;
    QueryResult ret36 = query40.eval(exFile40);
    ret36.printResult();

    // ---------------------------------------------
    // lnPrintln("\n习题 ------ ex ");
    // lnPrintln("\n章节 ---\t10 ");

    return 0;
};
