
import torch

from d2l import torch as d2l
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
    expression_string = " " + expression_string if extra_info is None else extra_info + ": "
    print(expression_string, expression)

def print_title(name, level = 1):
    prefix="=" * (20 - level)
    print("\n" + prefix, name, prefix)


scator = torch.tensor([2, 1])
print("2", scator, scator.shape)

one_vector = torch.ones((1, 2))
print("1*2", one_vector, one_vector.shape)


import numpy as np
import torch

print_title("numpy和pytorch 的高级索引")
# NumPy 示例
arr = np.array([[0.1, 0.3, 0.6], [0.3, 0.2, 0.5]])
y = np.array([0, 2])
result = arr[[0, 1], y]
# 输出 [0.1 0.5]
myprint(result)

# PyTorch 示例
tensor = torch.tensor([[0.1, 0.3, 0.6], [0.3, 0.2, 0.5]])
y = torch.tensor([0, 2])
result = tensor[[0, 1], y]
# 输出 tensor([0.1000, 0.5000])
myprint(result)


# # 2.1
# x = torch.arange(12)
# print(" number", x.numel())
#
# # 2*3*4  的正态分布
# print("(2,3,4): ", torch.randn((2, 3, 4)))
#
# # answer
# X = torch.arange(12, dtype=torch.float32).reshape((3, 4))
# Y = torch.tensor([[1, 2, 3, 4], [5, 6, 7, 8],[9, 10, 11, 12]])
# print("X == Y", X == Y)
# print("X < Y", X < Y)
# print("X > Y", X > Y)
#
#
# # 2.2 求解范数
# print("l2 norm: ", torch.norm(torch.ones((1, 2, 3))))
#
#
# # x = torch.arange(4.0, requires_grad=True)
# # print("x.grad: ", x.grad)
# # y = x * x
# # print("y: ", y)
# # print("y.backward(): ", y.backward(), "x.grad: ", x.grad)
# # x.grad.zero_()
# # print("y.sum.backward(): ", y.sum.backward(), "x.grad: ", x.grad)
#
# x = torch.tensor([2., 1.], requires_grad=True)
# y = torch.tensor([[1., 2.], [3., 4.]], requires_grad=True)
#
# myprint(x.view(1, 2))
# z = torch.mm(x.view(1, 2), y)
#
# myprint(torch.mm(x.view(1, 2), y))
#

import torch

myprint(torch.ones(2, requires_grad=True))

"""
    对于理解torch.sum().backward()至关重要。
    如果y是一个标量， 即k=x1等，反正输入变量是一个，可以简单求导。 导数存储在 x.grad
    但如y是一个向量，k = (k1, k2)=(x1^2 + 3 * x2,  x2^2 + 2 * x1) , Y对X求导是一个向量。 
    需要考虑不同dk, 例如dk1, dk2, dk3在不同分量的权重。
    例如我们简写dk1=(dk1/dx1, dk1/dx2), dk2=(dk2/dx1, dk2/dx2)
            雅可比行列式 = dk = [ dk1/dx1,  dk2/dx1 ]  ==> [ 2x1,  2   ]
                              [ dk1/dx2,  dk2/dx2 ]      [   3,  2x2 ]
            简写列向量 dk1=(dk1/dx1, dk1/dx2),    dk2=(dk2/dx1, dk2/dx2)
            k.backward([0, 1]) 表示 0 * dk1 + 1 * dk2    =>   则 
            k.sum().backward() 相当于 k.backward([1, 1])  =>
            
    # https://zhuanlan.zhihu.com/p/83172023
    # https://zhuanlan.zhihu.com/p/27808095
"""

print_title("复杂行列式子求雅可比", 2)
# 初始化一个张量，并设置requires_grad=True
m = torch.tensor([[2.0, 3.0]], requires_grad=True)
# 初始化目标张量
k = torch.zeros(1, 2)

# 重置梯度
m.grad = torch.zeros_like(m)

# 定义目标函数
k[0, 0] = m[0, 0] ** 2 + 3 * m[0, 1]
k[0, 1] = m[0, 1] ** 2 + 2 * m[0, 0]

# 计算雅可比矩阵的第一列
k.backward(torch.FloatTensor([[1, 0]]), retain_graph=True)

# 初始化雅可比矩阵
j = torch.zeros(2, 2)
j[:, 0] = m.grad.data

# 重置梯度
m.grad.zero_()

# 计算雅可比矩阵的第二列
k.backward(torch.FloatTensor([[0, 1]]), retain_graph=True)
j[:, 1] = m.grad.data


m.grad.zero_()
k.sum().backward()
myprint(m.grad.data, "k.sum().backward")

# 输出雅可比矩阵
print('jacobian matrix is: ', j)

print_title("simple gradient")
a = torch.tensor([2.0, 3.0], requires_grad=True)
b = a + 3
c = b * b * 3
out = c.mean()
out.backward()
print("input", a.data)
print('compute result is', out.data.item())
print('input gradients are', a.grad.data)

