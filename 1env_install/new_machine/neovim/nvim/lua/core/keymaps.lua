vim.g.mapleader = " "

-- local 表示这是局部变量
local keymap = vim.keymap

-- ---------- 插入模式 ---------- ---
-- s: 在insert模式下，设置ESC为jk，后面就可用用jk了
keymap.set("i", "jk", "<ESC>")

-- ---------- 视觉模式 ---------- ---
-- 单行或多行移动
keymap.set("v", "J", ":m '>+1<CR>gv=gv")
keymap.set("v", "K", ":m '<-2<CR>gv=gv")

-- ---------- 正常模式 ---------- ---
-- 窗口
keymap.set("n", "<C-w>v", "<C-w>v") -- 垂直新增窗口
keymap.set("n", "<C-w>s", "<C-w>s") -- 水平新增窗口
-- <C-l> 等等依赖tumux的


-- 取消高亮
keymap.set("n", "<leader>nh", ":nohl<CR>")

-- 切换buffer
keymap.set("n", "<C-L>", ":bnext<CR>")
keymap.set("n", "<C-H>", ":bprevious<CR>")

-- ---------- 插件 ---------- ---
-- nvim-tree
keymap.set("n", "<C-b>", ":NvimTreeToggle<CR>")


-- 进入telescope页面会是插入模式，回到正常模式就可以用j和k来移动了
local builtin = require('telescope.builtin')
keymap.set('n', '<C-p>', builtin.find_files, {})
keymap.set('n', '<C-f>', builtin.live_grep, {})  -- 环境里要安装 ripgrep
keymap.set('n', '<leader>fb', builtin.buffers, {})
keymap.set('n', '<leader>fh', builtin.help_tags, {})


