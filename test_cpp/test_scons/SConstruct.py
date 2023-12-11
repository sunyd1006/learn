import os


def print_center(contents, sep="-", defaultLen=40):
    contents = " " + contents + " "
    format_str = "\n{0:" + sep + "^" + str(defaultLen) + "}"
    # print('\n{0:-^60}'.format(contents)) # 占5个字符空间，0是format参数中的变量索引
    print(format_str.format(contents))  # 占5个字符空间，0是format参数中的变量索引


# -------------------------- 1-3 添加编译程序常见命令 --------------------------
# Program('main.cpp') # 默认参数
# Program('ex_lib_target', 'main.cpp')  # 隐式传递参数 obj文件还是main.o
# Program('ex_lib_target',['prog.c','file1.c','file2.c']) # 隐式传递参数
# Program(target='ex_lib_target', source=src_files) # 显示传递参数

# 3.2 使用Glob、Split
# Program('ex_lib_target', Glob('*.c'))
# src_files_list = Split('main.c  file1.c  file2.c')
# src_files_list = Split("""main.c
#  file1c
#  file2.c""")
# print(src_files_list)

# 3.3 在多个程序之间共享源文件
# common=['common1.c', 'common2.c']
# foo_files=['foo.c'] + common
# bar_files=['bar1.c', 'bar2.c'] + common
# Program('foo', foo_files)
# Program('bar', bar_files)

# -------------------------- 4. 添加编译程序常见命令 --------------------------
# 4.2、使用StaticLibrary显示编译静态库
# StaticLibrary('foo', ['f1.c', 'f2.c', 'f3.c']) # Library 是编译静态库
# SharedLibrary('ex_lib_target', ['ex_lib.cpp'])

# 4.2、链接库文件
# 使用$LIBS变量指定库文件，使用$LIBPATH指定存放库文件的目录，不需要指定库文件的前缀（比如lib）或后缀（比如.a或.lib）

# 4.3 本文示例
SharedLibrary('include/lib_test', ['ex_lib.cpp'])
env = Environment(CCFLAGS='-D DEBUG')
env.Program("build/ex_lib_target", 'ex_lib_main.cpp', LIBS=[
    'lib_test'], LIBPATH='include')  # 不需要./include
# ask: LIBPATH包括默认的/usr/lib, /usr/local/lib

# 4.4 参考资料
"""
-Wl.-rpath= 用于帮助ex_shared指定搜索第三方库文件的目录，（但不一定需要）
形如libXXXX.a libXXXX.so的动态库, 它们在 include 目录, 使用方法如下：
    A.扩展动态库搜索目录 export LD_LIBRARY_PATH=.:$LD_LIBRARY_PATH
    B. 适用相对路径： g++ main.cpp include/libxxxx.a -o main
    C. 使用参数指定：g++ main.cpp -Linclude -lxxxx -o mian , 即-L指定目录 -l指定具体动态库
"""
# --- 静态库编译和使用命令
# g++ ex_lib.cpp -c -o ex_lib.o                   # 预编译+编译+汇编
# ar rcs include/libex_lib_static.a ex_lib.o              # 链接成静态库
# g++ ex_lib_main.cpp include/libex_lib_static.a -o ex_static && ./ex_static # 使用静态库
# g++ ex_lib_main.cpp -Linclude -lex_lib_static -o ex_static && ./ex_static # 使用静态库

# --- 动态库编译和使用命令
# g++ ex_lib.cpp -c -o ex_lib.o                              # 预编译+编译+汇编
# g++ ex_lib.o -fPIC -shared -o include/libex_lib_shared.so     # 链接链接动态链接库
# g++ ex_lib_main.cpp include/libex_lib_shared.so -o ex_shared && ./ex_shared # 使用动态库 Method1
# g++ ex_lib_main.cpp -Linclude -lex_lib_shared  -o ex_shared && ./ex_shared  # 使用动态库 Method2



# -------------------------- 5 节点对象 --------------------------
"""
- https://blog.csdn.net/andyelvis/article/details/7084259
- scons用Node表示目录和文件，用Object代表其中一个编译Object对象，他可用用于其他Program.
特别的用File表示文件，用Dir表示目录，用Entry表示一个节点（不清楚是file还是dir)
- Object/env.GetBuildPath


2、显示创建文件和目录节点
xyzzy=Entry('xyzzy') 不确定是文件或者目录

4、将一个节点的文件名当作一个字符串
import os.path
program_list=Program('hello.c')
program_name=str(program_list[0])
if not os.path.exists(program_name):
     print program_name, "does not exist!"

5、GetBuildPath：从一个节点或字符串中获得路径
env=Environment(VAR="value")
n=File("foo.c")
print(env.GetBuildPath([n, "sub/dir/$VAR"]))

% scons -Q
['foo.c', 'sub/dir/value']

"""


# -------------------------- 6 依赖性 --------------------------
"""
https://blog.csdn.net/andyelvis/article/details/7173678

1. Decider 默认根据文件内容是否改变来触发编译即md5，还有timestamp-match、MD5-timestamp、自定义函数来
1.5、混合使用不同的方式来决定一个文件是否改变

3、隐式依赖：$CPPPATH 变量
    - 它告诉SCons去当前目录('.')查看那些被源文件hello.c（.c或.h文件）包含的文件, 如果那些文件发生了内容改变，就需要重新编译。
Program('hello.c', CPPPATH='.')
Program('hello.c', CPPPATH=['include', '/home/project/inc'])

4、缓存隐式依赖：缓存了hello.c依赖hello.h，以后就不用分析谁依赖谁了
但是缓存可能会出两个问题。
--implicit-deps-changed: 重新扫描依赖
--implicit-deps-unchanged：假设当前源文件.cpp没有改变依赖关系，就不扫描了，

5、显示依赖：Depends函数。可用于定制化编译的顺序
hello=Program('hello.c')
goodbye=Program('goodbye.c')
Depends(hello,goodbye)

6、忽略依赖：Ignore函数
即使一个依赖的文件改变了，也不想要重新编译


7、顺序依赖：Requires函数
- version.c是用来记录版本的，hello.c依赖于它链接，但不必每次都重新编译version.c
- Requires 仅仅指定编译顺序，version_obj > hello 编译

version_obj = Object('version.c')
hello = Program('hello.c', LINKFLAGS = str(version_obj[0]))
Requires(hello, version_obj)


9、AlwaysBuild函数
- hello目标程序每次都会被编译
- 但 scons -Q heelo.o 时只要hello.c内容没有改变其实hello不会被编译，因为hello.o不依赖于hello，而hello依赖与hell.o
hello=Program('hello.c')
AlwaysBuild(hello)

"""


# -------------------------- 7 环境 --------------------------
"""
https://blog.csdn.net/andyelvis/article/details/7269830
"""
print_center('7 环境')
print("Shell is", os.environ['SHELL'])

# 7.2.1、创建一个构造环境：Environment函数
env = Environment(CC='gcc', CCFLAGS='-O2 -DFoo')

# 7.2.2、从一个构造环境中获取值
print("CC is: %s" % env['CC'])
print("LATEX is: %s" % env.get('LATEX', None))


# 7.2.3、从一个构造环境中扩展值：subst方法
print("CCCOM is:", env['CCCOM'])
print("CCCOM is:", env.subst('$CCCOM'))

# 7.2.4、处理值扩展的问题
# AllowSubstExceptions()


# 7.2.5、控制默认的构造环境：DefaultEnvironment函数
# env=DefaultEnvironment(tools=['gcc','gnulink'], CC='/usr/local/bin/gcc')

# 7.2.6、多个构造环境
opt = Environment(CCFLAGS='-O2')
dbg = Environment(CCFLAGS='-g')
# dbg.Program(source = ["ex7_main.cpp", "ex7_hello.cpp"])
dbg.Program(target="build/ex7_main", source=["ex7_hello.cpp", "ex7_main.cpp"])

# 7.2.8、替换值：Replace方法
# SCons函数和方法的调用是没有顺序的， 在SConstruct中先Program后Replace，Replace的内容也可能会被应用与上面
env.Replace(NEW_VARIABLE='xyzzy')
print("NEW_VARIABLE = %s" % env['NEW_VARIABLE'])

# 7.2.9、在没有定义的时候设置值：SetDefault方法
env.SetDefault(SPECIAL_FLAG='-extra-option')
print("CCCOM is(SetDefault):", env.subst('$CCCOM'))


# 7.2.10、追加到值的末尾：Append方法
env.Append(CCFLAGS=['-DLAST'])
print("CCCOM is(Append):", env.subst('$CCCOM'))

# 7.2.11、追加唯一的值：AppendUnique方法
env.AppendUnique(CCFLAGS=['-g'])  # 不存在-g才追加


# 7.2.12、在值的开始位置追加值：Prepend方法
env.Prepend(CCFLAGS=['-DFIRST'])
print("CCCOM is(Prepend):", env.subst('$CCCOM'))

# 7.2.13、在前面追加唯一值：PrependUnique方法

# 7.3、控制命令的执行环境

"""
    SCons将不能找到你在命令行里执行的所有工具
    如果你的程序不在默认$PATH路径中，则需要在SConstruct里面添加路径
    POSIX系统的默认$PATH=/usr/local/bin:/opt/bin:/bin:/usr/bin:/snap/bin
"""
path = ['/usr/local/bin', '/bin', '/usr/bin']
env = Environment(ENV={'PATH': path})

# 7.3.1、从外部环境获得PATH值
# 你可能想获得外部的PATH来作为命令的执行环境。你可以使用来自os.environ的PATH值来初始化PATH变量：
env=Environment(ENV={'PATH':os.environ['PATH']})
print("env['ENV']['PATH']", env['ENV']['PATH'])
# print("env['ENV']['LIB']", env['ENV'].subst('LIB'))

# 你设置可以设置整个的外部环境：
env=Environment(ENV=os.environ)
print("env['ENV']['PATH']", env['ENV']['PATH'])
# print("env['ENV']['LIB']", env['ENV'].subst('LIB'))

# 7.3.2、在执行环境里增加PATH的值
env.PrependENVPath('PATH', '/usr/local/bin')
env.AppendENVPath('LIB', '/usr/local/lib')
print("env['ENV']['PATH']", env['ENV']['PATH'])
print("env['ENV']['LIB']", env['ENV']['LIB'])

# -------------------------- 8 自动将命令行选项赋值给Construction变量 --------------------------
print_center('8 自动将命令行选项赋值给Construction变量')
"""
https://blog.csdn.net/andyelvis/article/details/7308227
"""
env.Append(CPPPATH=['/include', '/usr/local/include', '/usr/include'])
flags = {'CPPPATH': ['/usr/opt/include', '/usr/local/include']}
env.MergeFlags(flags)
print(env['CPPPATH'])

# 2、将编译参数分离成变量：ParseFlags函数
# 以下方式都可以被解析
env = Environment(parse_flags="-I/opt/include -L/opt/lib -lfoo")
d = env.ParseFlags("-I/opt/include -L/opt/lib -lfoo")
d = env.ParseFlags(["-I/opt/include", ["-L/opt/lib", "-lfoo"]])
"""
    CPPPATH: ['/opt/include']
    LIBPATH: ['/opt/lib']
    LIBS: ['foo']
    cc -o f1.o -c -I/opt/include f1.c
"""

# 3、查找已经安装的库信息：ParseConfig函数

"""
关于 CPPPATH/LIBPATH/LIBS总结:sunyd
1. $CPPPATH: -I、源文件依赖的h和cpp文件在哪里？, 且这里面的文件内容发生改变后，scons会重新编译当前program
2. $LIBPATH: -L、指定存放库文件的目录
3. $LIBS: -l、指定库文件
"""


print_center('scons')
