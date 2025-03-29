local opt = vim.opt

-- 行号
opt.relativenumber = true
opt.number = true

-- 缩进
opt.tabstop = 2
opt.shiftwidth = 2
opt.expandtab = true
opt.autoindent = true

-- 防止包裹
opt.wrap = false

-- 光标行高亮
opt.cursorline = true

-- 启用鼠标
opt.mouse:append("a")

-- 系统剪贴板: 方便快盘的拷贝到nvim里面，和从nvim复制的内容就可以很好的拷贝到外部
opt.clipboard:append("unnamedplus")

-- 默认新窗口右和下
opt.splitright = true
opt.splitbelow = true

-- 搜索
opt.ignorecase = true
opt.smartcase = true

-- 外观
opt.termguicolors = true
opt.signcolumn = "yes"       -- 左侧多一列，对debug非常有用
vim.cmd[[colorscheme tokyonight-moon]]
