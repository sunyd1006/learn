import torch
import torch.nn as nn

embedding = nn.Embedding(10, 3)  # 10个词，每个词3维向量

input_ids = torch.tensor([1, 2, 5])  # 模拟3个词的 ID
output = embedding(input_ids)

print(output.shape)  # torch.Size([3, 3])
print(output)        # 每个词对应一个 [3] 维的向量