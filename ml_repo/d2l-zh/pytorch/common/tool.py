

def myprint(expression, extra_info = None):
    import inspect
    # 获取当前堆栈帧
    frame = inspect.currentframe().f_back
    # 获取源代码行
    line = inspect.getframeinfo(frame).code_context[0]
    # 去掉首尾的空白字符
    line = line.strip()
    # 提取表达式部分，假设调用形式为: get_expression_string(expression)
    expression_string = line[line.index('(') + 1:line.rindex(')')]
    expression_string = expression_string + ": " if extra_info is None else extra_info + ": "
    print(expression_string, expression)

def print_title(name, level = 1):
    prefix="=" * (20 - level)
    print("\n" + prefix, name, prefix)

