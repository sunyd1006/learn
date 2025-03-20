import sys
import os
sys.path.append(os.path.realpath('./common'))
import tool as t

import torch
from IPython import display
from d2l import torch as d2l


t.print_title("torch 相关算子")
t.myprint(torch.ones(1))
t.myprint(torch.ones(1, 1))

X = torch.Tensor([[1, 1, 1, 1], [2, 2, 2, 2]])
print("torch.Tensor([1, 1]) 是一个行向量")
W = torch.Tensor([1, 1])

z2 = torch.mul(X, W.reshape(-1, 1))
print(z2)


print(torch.zeros(10).shape, torch.zeros(10))

from torch import nn
gru_layer = nn.GRU(num_inputs, num_hiddens)
