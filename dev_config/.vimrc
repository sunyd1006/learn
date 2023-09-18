" ==== the config of your vim
set number
" 临时方案 输入 /search\c 并按回车以搜索 "search"，不区分大小写。 输入 /SEARCH\C 并按回车以搜索 "SEARCH"，区分大小写。
set ignorecase
set smartcase
set tabstop=4
set softtabstop=4
set shiftwidth=4
set expandtab


" ==== bind jk to esc for backing normal mode quickly, and bind , to leader
" install Plug 'nathanalderson/vim-mac-command-key' you can use command as leader
" let g:vim_mac_command_key_leader = 1
" inoremap 使得vim在插入模式下生效
" nnoremap 使得viz在normal模式下生效
" map会递归的使用映射，而nnoremap不会递归的使用映射。
let mapleader = ","
inoremap jk      <Esc><cr>
nnoremap <C-s> :w<cr>

" ==== 为vimgrep配置快捷键
nnoremap co :copen<cr>
nnoremap cc :cclose<cr>
nnoremap cn :cnext
nnoremap cp :cprev

" ==== 清空quicklist
nnoremap cqf :call setqflist([])<cr>
nnoremap cll :call setloclist(0, [])<CR>


" ===== vim自带的快捷键
" 在vim分屏中来回切换 <C-W>c close分屏 <C-w>p/<C-w>w左右跳动 <C-w>h光标focus左侧树形目录, <C-w>l 右侧目录, <C-w>j 移动到下面, <C-w>k 上面
nnoremap <C-c> <C-w>c
nnoremap <C-p> <C-w>p
nnoremap <C-g> <C-w>w
nnoremap <C-h> <C-w>h
nnoremap <C-l> <C-w>l
nnoremap <C-j> <C-w>j
nnoremap <C-k> <C-w>k

" ==== the shortcut of 'tabn'
nnoremap <C-w> :tabclose<CR>
nnoremap <C-j-j> :tabn<CR>
nnoremap <C-k-k> :tabn<CR>


" to edit and reload .vimrc quickly
map <leader>ee :e ~/.vimrc<cr>
map <leader>ss :source ~/.vimrc<cr>

" When .vimrc is edited, reload it
autocmd! bufwritepost .vimrc source ~/.vimrc


" ==== below config for vim-insterestingwords
let g:interestingWordsGUIColors = ['#8CCBEA', '#A4E57E', '#FFDB72', '#FF7272', '#FFB3FF', '#9999FF']
let g:interestingWordsTermColors = ['154', '121', '211', '137', '214', '222']
let g:interestingWordsRandomiseColors = 1


" ==== plug config
call plug#begin('~/.vim/plugged')

Plug 'scrooloose/nerdtree'
Plug 'mhinz/vim-startify'
Plug 'tpope/vim-fugitive'
Plug 'lfv89/vim-interestingwords'
Plug 'rking/ag.vim'

call plug#end()


" =========== NERD doc: https://github.com/preservim/nerdtree/blob/master/doc/NERDTree.txt ========
nnoremap <C-b> :NERDTreeToggle<CR>
nnoremap <C-n> :NERDTreeFocus<CR>
nnoremap <C-f> :NERDTreeFind<CR>

let NERDTreesighlightCursorline = 1
let NERDTreeNaturalSort = 1

" 在 vim 启动的时候默认开启 NERDTree（autocmd 可以缩写为 au）
" autocmd VimEnter * NERDTree
autocmd VimLeave * exe 'NERDTreeClose'

" =========== ag.vim doc: https://github.com/rking/ag.vim/blob/master/doc/ag.txt
" 始终从always start searching from your project root instead of the cwd
" let g:ag_working_path_mode="r"

nnoremap ag  :Ag
nnoremap agl :LAgAdd

let g:ag_prg="ag --vimgrep --smart-case"
let g:ag_highlight=1
let g:ag_format="%f:%l:%m"
let g:ag_open_list = 0



