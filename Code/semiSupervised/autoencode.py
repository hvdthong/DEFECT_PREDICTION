import tensorflow as tf
from tensorflow.contrib import losses
import numpy as np
from random import shuffle
from scipy import spatial
import sys
import math

sys.path.append('../dbnTF_code/')


class autoencoder(object):
    def __init__(self, input_size, output_size, hidden_size, epochs, learning_rate, batch_size, display_step):
        # Defining the hyperparameters
        self.input = input_size  # Size of input
        self.output = output_size  # Size of the class label
        self.hidden = hidden_size  # Size of dimension reduction
        self.epochs = epochs  # Amount of training iterations
        self.learning_rate = learning_rate  # The step used in gradient descent
        self.batch_size = batch_size  # The size of how much data will be used for training per sub iteration
        self.display_step = display_step

        # self.weights, self.biases = [], []
        self.encoderLayers = [self.input] + self.hidden
        self.decoderLayers = list(reversed(self.hidden)) + [self.input]
        self.weights_encoder, self.biases_encoder = {}, {}
        self.weights_decoder, self.biases_decoder = {}, {}

    def initialPara(self):
        weights_encode, biases_encode = {}, {}
        weights_decode, biases_decode = {}, {}
        for i in range(0, len(self.encoderLayers) - 1):
            if i + 1 == len(self.encoderLayers):  # break the loop
                break
            else:
                weights_encode.update({'encoder_h' + str(i + 1): tf.Variable(
                    tf.random_normal([self.encoderLayers[i], self.encoderLayers[i + 1]]))})
                biases_encode.update(
                    {'encoder_b' + str(i + 1): tf.Variable(tf.random_normal([self.encoderLayers[i + 1]]))})

                ####################################################################################
                # weights_encode.update({'encoder_h' + str(i + 1): tf.get_variable('encoder_h' + str(i + 1),
                #                                                                  shape=[self.encoderLayers[i],
                #                                                                         self.encoderLayers[i + 1]],
                #                                                                  initializer=tf.contrib.layers.xavier_initializer()
                #                                                                  )})
                # biases_encode.update({'encoder_b' + str(i + 1): tf.get_variable('encoder_b' + str(i + 1),
                #                                                                shape=[self.encoderLayers[i + 1]],
                #                                                                initializer=tf.contrib.layers.xavier_initializer()
                #                                                                )})
                ####################################################################################

        for i in range(0, len(self.decoderLayers) - 1):
            if i + 1 == len(self.decoderLayers):  # break the loop
                break
            else:
                weights_decode.update({'decoder_h' + str(i + 1): tf.Variable(
                    tf.random_normal([self.decoderLayers[i], self.decoderLayers[i + 1]]))})
                biases_decode.update(
                    {'decoder_b' + str(i + 1): tf.Variable(tf.random_normal([self.decoderLayers[i + 1]]))})

                ####################################################################################
                # weights_decode.update({'decoder_h' + str(i + 1): tf.get_variable('decoder_h' + str(i + 1),
                #                                                                  initializer=tf.contrib.layers.xavier_initializer(),
                #                                                                  shape=[self.decoderLayers[i],
                #                                                                         self.decoderLayers[i + 1]]
                #                                                                  )})
                # biases_decode.update({'decoder_b' + str(i + 1): tf.get_variable('decoder_b' + str(i + 1),
                #                                                                initializer=tf.contrib.layers.xavier_initializer(),
                #                                                                shape=[self.decoderLayers[i + 1]]
                #                                                                )})
                ####################################################################################

        self.weights_encoder, self.biases_encoder = weights_encode, biases_encode
        self.weights_decoder, self.biases_decoder = weights_decode, biases_decode

    def encoder(self, x):
        layers = x
        for i in range(0, len(self.weights_encoder)):
            layers = tf.nn.sigmoid(
                tf.add(tf.matmul(layers, self.weights_encoder['encoder_h' + str(i + 1)]),
                       self.biases_encoder['encoder_b' + str(i + 1)]))

            # layers = tf.add(tf.matmul(layers, self.weights_encoder['encoder_h' + str(i + 1)]),
            #                 self.biases_encoder['encoder_b' + str(i + 1)])

            # layers = tf.nn.softplus(
            #     tf.add(tf.matmul(layers, self.weights_encoder['encoder_h' + str(i + 1)]),
            #            self.biases_encoder['encoder_b' + str(i + 1)]))
        return layers

    def decoder(self, x):
        layers = x
        for i in range(0, len(self.weights_decoder)):
            layers = tf.nn.sigmoid(
                tf.add(tf.matmul(layers, self.weights_decoder['decoder_h' + str(i + 1)]),
                       self.biases_decoder['decoder_b' + str(i + 1)]))

            # layers = tf.nn.softplus(
            #     tf.add(tf.matmul(layers, self.weights_decoder['decoder_h' + str(i + 1)]),
            #            self.biases_decoder['decoder_b' + str(i + 1)]))
        return layers

    def index_pos_neg(self, y):
        neg, pos = [], []
        for i in range(0, len(y)):
            if (y[i] == [1, 0]).all():
                neg.append(i)
            else:
                pos.append(i)
        return neg, pos

    def random_select(self, y, size):
        idx = np.random.randint(len(y), size=size)
        return [y[i] for i in idx]

    def index_highest_cosine(self, x_value, x, idx_neg):
        cosine_ = []
        for idx in idx_neg:
            x_idx = x[idx]
            cosine_.append(1 - spatial.distance.cosine(x_value, x_idx))
        max_idx = cosine_.index(max(cosine_))
        return idx_neg[max_idx]

    def smart_select(self, x, y, idx_neg, idx_pos, size):
        idx_pos_size = self.random_select(idx_pos, size)
        idx_neg_size = []
        for i in idx_pos_size:
            x_pos = x[i]
            idx_neg_size.append(self.index_highest_cosine(x_pos, x, idx_neg))
        return idx_neg_size, idx_pos_size

    def solving_unbalanced(self, x, y, size):
        idx_neg, idx_pos = self.index_pos_neg(y)

        # if len(idx_neg) > len(idx_pos):
        #     idx_neg_size, idx_pos_size = self.smart_select(x, y, idx_neg, idx_pos, size)
        # else:
        #     idx_neg_size, idx_pos_size = self.smart_select(x, y, idx_pos, idx_neg, size)

        # idx_neg_size, idx_pos_size = self.random_select(idx_neg, size), self.random_select(idx_pos, size)
        idx_neg_size, idx_pos_size = self.smart_select(x, y, idx_neg, idx_pos, size)

        idx_size = np.concatenate((idx_neg_size, idx_pos_size), axis=0)
        shuffle(idx_size)
        x_, y_ = x[idx_size], y[idx_size]
        return x_, y_

    def train(self, X, Y, X_test, Y_test):
        X_ = tf.placeholder('float', [None, X.shape[1]])
        Y_ = tf.placeholder('float', [None, Y.shape[1]])
        batch_size = self.batch_size

        self.initialPara()
        encoder_op = self.encoder(X_)
        decoder_op = self.decoder(encoder_op)

        # unsupervised learning
        y_pred_un = decoder_op
        y_true_un = X_

        # loss function for unsupervised learning
        cost_un = tf.reduce_mean(tf.pow(y_pred_un - y_true_un, 2))

        # supervised learning
        # note that we take the last element of hidden units
        W_1 = tf.Variable(tf.random_normal([self.hidden[-1], self.hidden[-1] / 4]))
        b_1 = tf.Variable(tf.random_normal([self.hidden[-1] / 4]))
        out_W1 = tf.nn.sigmoid(tf.add(tf.matmul(encoder_op, W_1), b_1))

        W_2 = tf.Variable(tf.random_normal([self.hidden[-1] / 4, Y.shape[1]]))
        b_2 = tf.Variable(tf.random_normal([Y.shape[1]]))

        # W_2 = tf.Variable(tf.random_normal([self.hidden[-1], Y.shape[1]]))
        # b_2 = tf.Variable(tf.random_normal([Y.shape[1]]))
        # y_pred = tf.nn.sigmoid(tf.add(tf.matmul(encoder_op, W_2), b_2))

        y_pred = tf.nn.sigmoid(tf.add(tf.matmul(out_W1, W_2), b_2))  # sigmoid
        # y_pred = tf.nn.softmax(tf.add(tf.matmul(out_W1, W_2), b_2))  # sigmoid
        y_true = Y_
        # # # loss function for supervised learning
        # # # regularizers = tf.nn.l2_loss(W_1) + tf.nn.l2_loss(W_2)
        # # # cost_su = tf.reduce_mean(cost_su + 0.01 * regularizers)
        cost_su = tf.reduce_mean(tf.pow(y_pred - y_true, 2))
        # cost_su = tf.reduce_mean(tf.nn.softmax_cross_entropy_with_logits(labels=y_true, logits=y_pred))

        # y_pred = tf.add(tf.matmul(out_W1, W_2), b_2)
        # y_true = Y_
        # pos, neg = self.index_pos_neg(Y)
        # pos_weight = len(pos) / float(len(pos) + len(neg))
        # class_weight = tf.constant([pos_weight, 1.0 - pos_weight])
        # weight_per_label = tf.transpose(tf.matmul(y_true, tf.transpose(class_weight)))  # shape [1, batch_size]
        # cost_su = tf.reduce_mean(
        #     tf.multiply(weight_per_label, tf.nn.softmax_cross_entropy_with_logits(logits=y_pred, labels=y_true)))

        # y_pred = tf.add(tf.matmul(out_W1, W_2), b_2)  # sigmoid
        # y_true = Y_
        # pos, neg = self.index_pos_neg(Y)
        # pos_weight = float(len(pos) + len(neg)) / float(len(pos))
        # class_weight = tf.constant([pos_weight, 1.0 - pos_weight])
        # weighted_logits = tf.multiply(y_pred, class_weight)
        # cost_su = tf.reduce_mean(
        #     tf.nn.softmax_cross_entropy_with_logits(logits=weighted_logits, labels=y_true))

        # cost_su = losses.log_loss(predictions=y_pred, labels=y_true, weights=pos_weight)

        # y_pred = tf.add(tf.matmul(out_W1, W_2), b_2)
        # y_true = Y_
        # pos, neg = self.index_pos_neg(Y)
        # # pos_weight = float(len(pos) + len(neg)) / float(len(pos))
        # pos_weight = float(len(pos)) / float(len(pos) + len(neg))
        # class_weight = tf.constant([pos_weight, 1.0 - pos_weight])
        # cost_su = tf.reduce_mean(
        #     tf.nn.weighted_cross_entropy_with_logits(targets=y_true, logits=y_pred, pos_weight=class_weight))
        ####################################################################################
        # W_1 = tf.get_variable('W_1', shape=[self.hidden[-1], self.hidden[-1] / 2],
        #                       initializer=tf.contrib.layers.xavier_initializer())
        # b_1 = tf.get_variable('b_1', shape=[self.hidden[-1] / 2],
        #                       initializer=tf.contrib.layers.xavier_initializer())
        # out_W1 = tf.nn.sigmoid(tf.add(tf.matmul(encoder_op, W_1), b_1))
        #
        # W_2 = tf.get_variable('W_2', shape=[self.hidden[-1] / 2, Y.shape[1]],
        #                       initializer=tf.contrib.layers.xavier_initializer())
        # b_2 = tf.get_variable('b_2', shape=[Y.shape[1]],
        #                       initializer=tf.contrib.layers.xavier_initializer())
        # y_pred = tf.nn.sigmoid(tf.add(tf.matmul(out_W1, W_2), b_2))  # Softmax
        # y_true = Y_
        ####################################################################################

        # define loss and optimizer, minimize the squared error
        total_cost = cost_un + cost_su
        # learning_rate = tf.placeholder(tf.float32, shape=[])
        # optimizer = tf.train.RMSPropOptimizer(learning_rate=learning_rate
        #                                       , decay=0.8, epsilon=1e-10).minimize(total_cost)

        optimizer = tf.train.AdamOptimizer(learning_rate=self.learning_rate).minimize(total_cost)
        # optimizer = tf.train.GradientDescentOptimizer(learning_rate=self.learning_rate).minimize(total_cost)

        init = tf.global_variables_initializer()
        sess = tf.InteractiveSession()
        # write = tf.summary.FileWriter("./tb/1")
        # write.add_graph(sess.graph)
        sess.run(init)
        # launch the graph

        total_batch = int(X.shape[0] / batch_size)
        weights_, biases_, costs, cost_uns, cost_sus = [], [], [], [], []

        # training cycle
        for epoch in range(self.epochs):
            # loop over all batches
            for i in range(total_batch):
                # batch_xs = X[i * batch_size:(i + 1) * batch_size]
                # batch_ys = Y[i * batch_size:(i + 1) * batch_size]
                batch_xs, batch_ys = self.solving_unbalanced(X, Y, batch_size / 2)
                _, c, u, s = sess.run([optimizer, total_cost, cost_un, cost_su],
                                      feed_dict={X_: batch_xs, Y_: batch_ys})
            # w_encode = self.weights['encoder_h1'].eval()
            # w_decode = self.weights['decoder_h1'].eval()
            # weights_.append({'encoder_h1': w_encode, 'decoder_h1': w_decode})
            # display logs per epoch step

            if epoch % self.display_step == 0:
                # print("Epoch:", '%04d' % (epoch + 1), "cost={:.9f}".format(c))
                print 'Epoch: %04d, totalCost: %.4f, unCost: %.4f, suCost: %.4f' % (epoch + 1, c, u, s)
            cost_uns.append(u)
            cost_sus.append(s)
            costs.append(c)
            # self.learning_rate = self.learning_rate * 0.8

            # early stopping
            # if epoch >= 1:
            #     # print math.fabs(s - cost_sus[epoch - 1])
            #     if math.fabs(s - cost_sus[epoch - 1]) <= 0.005 and math.fabs(u - cost_uns[epoch - 1]) < 0.005:
            #         break

        print("optimization finished!!")

        self.costs = costs
        self.costs_un = cost_uns
        self.costs_su = cost_sus

        y_pred_label = tf.argmax(y_pred, 1)
        value_pred = y_pred_label.eval({X_: X_test})
        self.y_pred = value_pred
        # y_true_label = tf.argmax(y_true, 1)
        # value_true = y_true_label.eval({Y_: Y_test})

        X_ = tf.placeholder('float', [None, X.shape[1]])
        encoder_ = self.encoder(X_)

        self.encoder_train = encoder_.eval({X_: X})
        self.encoder_test = encoder_.eval({X_: X_test})
