require("mason").setup({
  ui = {
      icons = {
          package_installed = "✓",
          package_pending = "➜",
          package_uninstalled = "✗"
      }
  }
})

require("mason-lspconfig").setup({
  -- 确保安装，根据需要填写
  ensure_installed = {
    "lua_ls",
  },
})

-- TODO: 禁用了 lua_ls 的 LSP 配置，因为
-- The `require('lspconfig')` "framework" is deprecated, use vim.lsp.config (see :help lspconfig-nvim-0.11) instead.
-- Feature will be removed in nvim-lspconfig v3.0.0
-- stack traceback:
--         .../site/pack/packer/start/nvim-lspconfig/lua/lspconfig.lua:81: in function '__index'
--         /Users/bytedance/.config/nvim/lua/plugins/lsp.lua:20: in main chunk
--         [C]: in function 'require'
        -- ...odespace/learn/1env_install/new_mac/neovim/nvim/init.lua:14: in main chunk
-- local capabilities = require('cmp_nvim_lsp').default_capabilities()
-- require("lspconfig").lua_ls.setup {
--   capabilities = capabilities,
-- }
