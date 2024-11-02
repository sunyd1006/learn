function info(message)
    print("== [warning]: " .. message)
end
info("lua library 'common' loaded")

function serilizeTable(table)
    local serilizedStr = "{"
    local idx = 1
    for k, v in pairs(table) do
        if idx ~= 1 then
            serilizedStr = serilizedStr .. ","
        end
        idx = idx + 1
        serilizedStr = serilizedStr .. k .. "=" .. v
    end
    serilizedStr = serilizedStr .. "}"
    return serilizedStr
end

function serilizeList(list)
    local serilizedStr = "{"
    local idx = 1
    for i=1, #list do
        if idx ~= 1 then
            serilizedStr = serilizedStr .. ","
        end
        idx = idx + 1
        serilizedStr = serilizedStr .. list[i]
    end
    serilizedStr = serilizedStr .. "}"
    return serilizedStr
end

