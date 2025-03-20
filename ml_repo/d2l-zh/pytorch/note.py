import sys
import os
sys.path.append(os.path.realpath('../common'))
import ztest as t

import torch
from IPython import display
from d2l import torch as d2l

Animator(xlabel='epoch', xlim=[1, num_epochs], ylim=[0.3, 0.9],
                        legend=['train loss', 'train acc', 'test acc'])


import torch.nn as nn
import torch.nn.functional as F

class HuberLoss(nn.Module):
    def __init__(self, sigma):
        super(HuberLoss, self).__init__()
        self.sigma = sigma
    def forward(self, y, y_hat):
        if F.l1_loss(y, y_hat) > self.sigma:
            loss = F.l1_loss(y, y_hat) - self.sigm a /2
        else:
            loss = ( 1 /( 2 *self.sigma) ) *F.mse_loss(y, y_hat)
        return loss

