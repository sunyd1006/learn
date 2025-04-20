import torch

# https://www.cnblogs.com/bytesfly/p/why-need-gradient-arg-in-pytorch-backward.html?utm_source=chatgpt.com
# 所谓的标量对向量求导，本质上是函数对各个自变量求导，这里只是把各个自变量看成一个向量，和数学上的定义并不冲突。
# https://docs.pytorch.org/docs/stable/generated/torch.autograd.backward.html
# https://cs231n.github.io/optimization-2/

x = torch.tensor([1.0, 2.0, 3.0], requires_grad=True)
y = x ** 2 + 2
z = torch.sum(y)
z.backward()
print("z", z)
print("x.grad", x.grad)


x1 = torch.tensor(1, requires_grad=True, dtype=torch.float)
x2 = torch.tensor(2, requires_grad=True, dtype=torch.float)
x3 = torch.tensor(3, requires_grad=True, dtype=torch.float)
y = torch.randn(3)
y[0] = x1 * x2 * x3
y[1] = x1 + x2 + x3
y[2] = x1 + x2 * x3
# x = torch.tensor([x1, x2, x3])
y.backward(torch.tensor([0.1, 0.2, 0.3], dtype=torch.float))
print(x1.grad)
print(x2.grad)
print(x3.grad)
