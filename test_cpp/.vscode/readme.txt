
.vscode/settings.json: https://code.visualstudio.com/docs/configure/settings
* workspace级别的配置，在不同的wrokspace之间可以配置不同的值
* user级别的配置存在出一个特定的的安装路径，

tasks.json：一些shell脚本，用来自动编译等等
launch.json: 用于debug的，mac m1用的lldb, mac intel 用gdb
c_cpp_properties.json: 配置项目特定的C/C++设置

vscode offical references: https://code.visualstudio.com/docs/cpp/launch-json-reference
参考文献：https://yang-xijie.github.io/LECTURE/vscode-cpp/7_%E7%94%A8VSCode%E8%B0%83%E8%AF%95Cpp%E4%BB%A3%E7%A0%81/


vscode 预定义变量的名字：https://blog.csdn.net/acktomas/article/details/102851702
// ${workspaceFoler}

code snniptes:
https://blog.csdn.net/maokelong95/article/details/54379046

# cpp 代码格式化配置信息，采用google模式
BasedOnStyle:LLVM
AllowshortfunctionsonASingleLine:false
AlwaysBreakTemplateDeclarations: true
BinPackParameters:false
BreakConstructorInitializersBeforeComma:true
ConstructorInitializerAllonOneLine0rOnePerLine: true
ConstructorInitializerIndentwidth: 4
IndentFunctionDeclarationAfterType: false
IndentWidth:4