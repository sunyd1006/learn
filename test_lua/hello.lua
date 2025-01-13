require("./common")


-- 参考教程：https://www.runoob.com/lua/lua-tutorial.html


-- 2. 数据类型 https://www.runoob.com/lua/lua-data-types.html

-- 2.1 字符串类型
tab1 = { key1 = "val1", key2 = "val2", "val3" }
for k, v in pairs(tab1) do
    -- .. 是用于连接字符串的
    print(k .. " - " .. v)
end

-- -- 2.2 空类型 type() 返回的实际为字符串类型
-- print(type(type(X))==string)
-- print(type(type(X))=="string")

-- print(type(true))
-- -- 2.3 算术类型会尝试自动转换
-- print("2" + 6)


-- -- 2.* table 相当于一个map，但是idx是从1开始编号的！
local tbl = {"apple", "pear", "orange", "grape"}
for key, val in pairs(tbl) do
    print("Key", key)
end

-- table 相当于map
t = {}
-- t[strKey]
-- t.strKey                 -- 当索引为字符串类型时的一种简化写法
-- gettable_event(t,i) -- 采用索引访问本质上是一个类似这样的函数调用

-- function_test2.lua 脚本文件
function testFun(tab,fun)
    for k ,v in pairs(tab) do
            print(fun(k,v));
    end
end

-- 匿名函数
tab={key1="val1",key2="val2"};
testFun(tab, function(key,val)--匿名函数
    return key.."="..val;
end
);

-- 3. lua全局变量
-- test.lua 文件脚本
a = 5               -- 全局变量
local b = 5         -- 局部变量

function joke()
    c = 5           -- 全局变量
    local d = 6     -- 局部变量
end

joke()
print(c, d)          --> 5 nil
do
    local a = 6     -- 局部变量
    b = 6           -- 对局部变量重新赋值
    print(a, b);     --> 6 6
end
print(a,b)      --> 5 6


a, b = 10, 2*1          --   等价于    a=10; b=2*1
x, y = y, x             --   相当于 swap 'x' for 'y'
a, b = a+1, b+1, b+2    --   value of b+2 is ignored
print(a,b)              -->  1   2



-- 4. 循环 https://www.runoob.com/lua/lua-for-loop.html
maxValue = 6
step = 2
for i=1, maxValue, step  do
    print("for idx: " .. i)  -- 1, 3, 5,    7越界了不会输出来
end

-- for 循环
i = 0
maxTimes = 4
while( i < maxTimes )  do
    print("while idx: " .. i)
    i = i + 1
end

list = {"v1", "v2", "ve"}; for idx, value in ipairs(list) do print(idx, value) end


a = 100

--[ 检查布尔条件 --]
if( a == 10 ) then
   --[ 如果条件为 true 打印以下信息 --]
   print("a 的值为 10" )
elseif( a == 20 ) then
   --[ if else if 条件为 true 时打印以下信息 --]
   print("a 的值为 20" )
else
   --[ 以上条件语句没有一个为 true 时打印以下信息 --]
   print("没有匹配 a 的值" )
end


-- 5.  Lua 函数

function multi_variables(...)
    print("multi_variables总共传入 " .. select("#",...) .. " 个数")
    for i = 1, select('#', ...) do  -->获取参数总数
        local arg = select(i, ...); -->读取参数，arg 对应的是右边变量列表的第一个参数
        print("select(", i,") = ", arg);
    end
end

multi_variables(1,2,3,4)


-- 运算符 https://www.runoob.com/lua/lua-miscellaneous-operator.html

-- 验证加减乘除运算符的优先级
a, b, c, d = 2, 3, 4, 5
e1 =  (a + b) * c / d                     -- ( 30 * 15 ) / 5
e2 =  a + b * c / d
print("(a + b) * c / d 运算值为  :", e1, e2)


-- 字符串： https://www.runoob.com/lua/lua-strings.html


-- Lua metatable：https://www.runoob.com/lua/lua-metatables.html

-- print(serilizeTable({k1="v1", k2="v2", k3="v3"}))

mytable = setmetatable({key1 = "key1_in_table"}, {
    __index = function(mytable, key)
        print("setmetatable test, 听说传入了原表打印看看: ", serilizeTable(mytable))
        if key == "key2" then
            return "key2_in_function"
        else
            return nil
        end
    end
})
print(mytable.key1, mytable.key2)    -- key1_in_table	key2_in_function


-- 数组：https://www.runoob.com/lua/lua-arrays.html

local myArray = {10, 20, 30, 40, 50}
-- 循环遍历数组
for i = 1, #myArray do
    print(myArray[i])
end


-- 错误处理：https://www.runoob.com/lua/lua-error-handling.html

--[[
    xpcall(f, err):
        f：要调用的函数。
    	err：自定义的错误处理函数。当 f 发生错误时，Lua 会调用这个错误处理函数，并将错误信息传递给它。
]]
function myfunction ()
    n = n/nil
end

function myerrorhandler( err )
    print( "ERROR:", err )
end

funcExecStatus = xpcall( myfunction, myerrorhandler )
print(funcExecStatus)


--[[   // 这是多行注释的写法
基本类型和运算符
* map叫做table, 支持table.keyName, table[keyName], gettable_event(table, keyName)) 这三种写法
*    map是从1开始编号的
*    当我们为 table a 并设置元素，然后将 a 赋值给 b，则 a 与 b 都指向同一个内存。如果 a 设置为 nil ，则 b 同样能访问 table 的元素。如果没有指定的变量指向a，Lua的垃圾回收机制会清理相对应的内存。

* 数组的写法   list = {"v1", "v2", "ve"}   for idx, value in ipairs(list) do print(idx, value) end
* ~= 是不等于, <=, >= 等等其他的和cpp一样。 and or not 语法和python一样
* .. 连接两个字符串，相当于字符串+号；    #"a str or strVariables": 返回字符串长度
* 除了 ^ 和 .. 外所有的二元运算符都是左连接的，但是基本的加减乘除运算符的优先级还是遵守的
* 字符串：包含中文的一般用 utf8.len(), string.len() 函数用于计算只包含 ASCII 字符串的长度
*   string.upper(argument):
*   string.upper(argument):
*   string.find


* 数组
local myArray = {10, 20, 30, 40, 50}
-- 循环遍历数组
for i = 1, #myArray do
    print(myArray[i])
end




作用域:
* Lua 中的变量全是全局变量，哪怕是语句块或是函数里，除非用 local 显式声明为局部变量。
* 在Lua 语言中，全局变量无须声明即可使用，使用未经初始化的全局变量不会导致错误。当使用未经初始化的全局变量时，得到的结果为 nil:


循环：
* while( true )  do  print("循环将永远执行下去")  end
* for的三个表达式在循环开始前一次性求值，以后不再进行求值

函数：
* 未设置该参数默认为全局函数，如果你需要设置函数为局部函数需要使用关键字 local。


Lua模块与包:
* 查找自定义的lua package时，需要配置环境变量 LUA_PATH。
* 加载lua package会优先查找LUA_PATH, 这个值会初始化到 存储Lua 文件的路径的全局变量 package.path 中，所以可以配置在~/.bashrc里，或者是~/.profile文件里面，如果找过目标文件，则会调用 package.loadfile 来加载模块。
* 其次再查找C 程序库，搜索的文件路径是从全局变量 package.cpath 获取，而这个变量则是通过环境变量 LUA_CPATH 来初始。只不过现在换成搜索的是 so 或 dll 类型的文件。如果找得到，那么 require 就会通过 package.loadlib 来加载它。

Lua metatable:

当你通过键来访问 table 的时候，如果这个键没有值，那么Lua就会寻找该table的metatable。
如果metatable中存在__index 键。
    如果__index包含一个表格，Lua会在表格中查找相应的键。
        如果存在则由 __index 返回结果。如果不存在，返回结果为 nil；
    否则如果__index是一个函数，Lua就会调用那个函数，table和键会作为参数传递给函数。

Lua coroutine:
* 相当于一个线程，但是多个线程只能在一个核上运行。协同程序则是协作式的，只有一个协同程序处于运行状态，其他协同程序必须等待当前协同程序主动放弃执行权。
* 而协同程序可以共享相同的堆栈和上下文，因此创建和销毁协同程序的开销较小。
* 协同程序通常通过参数传递和返回值来进行数据共享，不同协同程序之间的数据隔离性较好。


Lua debug: 没有认真看


--]]