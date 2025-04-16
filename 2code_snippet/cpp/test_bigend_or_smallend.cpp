#include <stdio.h>

int main() {
    int num = 0x12345678;
    unsigned char *p = (unsigned char *)&num;

    for (int i = 0; i < 4; ++i) {
        printf("%x ", p[i]);
    }
    return 0;
}