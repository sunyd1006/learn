#include<stdio.h>
​
//定义gpu 核函数
__global__ void helloFromGPU()
{
   printf("Hello World from GPU!\n");
}
int main()
{
    //hello from cpu
    printf("Hello World from CPU!\n");
    //调用核函数
    helloFromGPU<<<1,1>>>();
    cudaDeviceReset();
    return 0;
}