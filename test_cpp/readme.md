# cpp dev 常用component

- cmake 使用：https://www.hahack.com/codes/cmake/
- cpp 协程：https://zhuanlan.zhihu.com/p/218156339



**调试信息 FUNCTION**

```cpp
// __FUNCTION__ ：函数名, 相当于字符串
// __TIME__ ：文件运行的时间
// __LINE__ ：所在行数
// __FILE__：文件的名字
printf("The file is %s.\n",__FILE__);
printf( "The date is %s.\n", __DATE__ );
printf( "The time is %s.\n", __TIME__);
printf( "This is line %d.\n", __LINE__ );
printf( "This function is %s.\n", __FUNCTION__ );
```
