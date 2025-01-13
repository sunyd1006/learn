require("common")
-- export LUA_PATH="/Users/sunyindong/codespace/learn/test_lua/?.lua;;"

-- print(serilizeTable({k1="v1", k2="v2", k3="v3", k4="v4"}))
-- print(serilizeList({k1}))

local x = 10

local function outer()
    local y = 20

    local function inner()
        print(x + y)
    end

    return inner
end

local func = outer() -- 返回内嵌函数 inner

--[[
    func 是 inner 函数的一个引用。
	debug.getupvalue(func, 1) 会返回 x 和 10，表示 inner 使用的第一个上值。
	debug.getupvalue(func, 2) 会返回 y 和 20。
]]
local name, value = debug.getupvalue(func, 1) -- 获取第一个上值

print(name)  -- 输出 'x'
print(value) -- 输出 10

name, value = debug.getupvalue(func, 2) -- 获取第二个上值

print(name)  -- 输出 'y'
print(value) -- 输出 20