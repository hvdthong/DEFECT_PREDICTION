import numpy as np
# from sklearn.preprocessing import scale
from sklearn.preprocessing import MinMaxScaler
# from sklearn.preprocessing import Normalizer

# a = [[1, 2, 4], [2, 5, 7]]
# # print Normalizer(np.array(a), norm='l2')
# print MinMaxScaler(np.array(a))
#
# X = np.array(a)
# X_std = (X - X.min(axis=0)) / (X.max(axis=0) - X.min(axis=0))
# # X_scaled = X_std * (X.max - min) + min
# print X_std
#
# # w = np.loadtxt('w.txt', delimiter=',')
# # print w.shape
# # print type(w)
#
# a = np.array(a)
# print type(a.shape)
# print a.shape[1]

b, c = [], []
for i in range(0, 7):
    b.append(i)
for i in range(7, 14):
    c.append(i)
d = b + c

from random import shuffle
shuffle(d)
print d

import tensorflow as tf
initializer = tf.contrib.layers.xavier_initializer()
w = tf.Variable(initializer([784, 256]))
print w

w_1 = tf.Variable(initializer([128]))
print w_1

