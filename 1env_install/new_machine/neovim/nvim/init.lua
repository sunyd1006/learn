
-- 这是启动文件，nvim绘自动加载这里的配置

-- 默认加载lua/xxxx下的路径，不需要写lua/
require("plugins.plugins-setup")   -- 新增插件在这里添加，保存后就能自动安装

require("core.options")
require("core.keymaps")

-- lua/plusgins 是管理插件的地方，这里用于加载各个插件的配置
require("plugins.lualine")
require("plugins/nvim-tree")
require("plugins/treesitter")
require("plugins/lsp")
require("plugins/cmp")
require("plugins/comment")
require("plugins/autopairs")
require("plugins/bufferline")
require("plugins/gitsigns")
require("plugins/telescope")
