#include "ex26.h"

#include <iostream>

#include "tool.h"

using namespace std;

template <>
int Counted<Sigleton>::objNumber = 0;
template <>
size_t Counted<Sigleton>::objMaxNumer = 3;

template <typename BeingCounted>
void Counted<BeingCounted>::init() {
    if (objNumber >= objMaxNumer) {
        throw TooManyObjects();
    }
    ++objNumber;
}

template <typename BeingCounted>
Counted<BeingCounted>::Counted() {
    init();
}
template <typename BeingCounted>
Counted<BeingCounted>::Counted(const Counted &rhs) {
    init();
}

Sigleton *Sigleton::makePrinter() { return new Sigleton; }
Sigleton *Sigleton::makePrinter(const Sigleton &rhs) { return new Sigleton(rhs); }