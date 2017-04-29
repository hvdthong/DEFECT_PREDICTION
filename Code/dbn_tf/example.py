import numpy as np
import tensorflow as tf

# print np.sign([-5., 4.5])

p = [1, 4, 6, 3, 8]
# a = np.random.uniform(p)
#
# k = np.random.uniform(-1,0,1000)
# print a
# print k

print np.array(p).shape[0]
print p[-1]

ks = []
for v in range(500):
    # k = np.random.uniform(0, 1, 784)
    ks.append(np.random.uniform(0, 1, 784))
print np.array(ks).shape

ks = np.array(ks)
for i in ks:
    print i.shape

# rand = tf.random_uniform([500])
# value = tf.nn.relu(rand)
# sess = tf.Session()
# init = tf.initialize_all_variables()
# # print sess.run(rand)
# with sess.as_default():
#     print len(rand.eval())
#     print rand.eval()
#     print value.eval()

