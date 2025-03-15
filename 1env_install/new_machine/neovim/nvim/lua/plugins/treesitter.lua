require'nvim-treesitter.configs'.setup {
  -- 添加不同语言, one of "all" or a list of languages
  -- ensure_installed = { "vim", "help", "bash", "c", "cpp", "json", "lua", "python", "typescript", "tsx", "css", "markdown", "markdown_inline" },

  highlight = { enable = true },
  indent = { enable = true },

  -- 不同括号颜色区分
  rainbow = {
    enable = true,
    extended_mode = true,
    max_file_lines = nil,
  }
}
