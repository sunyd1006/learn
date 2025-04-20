#include <stdio.h>

int main() {
    int num = 0x12345678;
    unsigned char *p = (unsigned char *)&num;

    // 数据的高位字节存储在内存的低地址处，低位字节存储在内存的高地址处。
    // 对于整数 0x12345678（十六进制）：内存地址递增方向：0x12 → 0x34 → 0x56 → 0x78
    // 高位字节 0x12 在最低地址，低位字节 0x78 在最高地址。
    for (int i = 0; i < 4; ++i) {
        printf("%x ", p[i]);
    }
    return 0;
}