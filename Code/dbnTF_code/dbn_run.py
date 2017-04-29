# Getting the MNIST data provided by Tensorflow
from tensorflow.examples.tutorials.mnist import input_data
from NN import NN
from rbm import RBM
import numpy as np
import cPickle as pickle
import matplotlib.pyplot as plt
import os
import sys

sys.path.append('../services/')
from preprocessing import load_variables, load_dbn_ftr


def running_MNIST():
    # Loading in the mnist data
    mnist = input_data.read_data_sets("MNIST_data/", one_hot=True)
    trX, trY, teX, teY = mnist.train.images, mnist.train.labels, mnist.test.images, \
                         mnist.test.labels

    print trX.shape
    # exit()

    RBM_hidden_sizes = [100]  # create 2 layers of RBM with size 400 and 100

    # Since we are training, set input as training data
    inpX = trX

    # Create list to hold our RBMs
    rbm_list = []

    # Size of inputs is the number of inputs in the training set
    input_size = inpX.shape[1]

    # For each RBM we want to generate
    for i, size in enumerate(RBM_hidden_sizes):
        print 'RBM: ', i, ' ', input_size, '->', size
        rbm_list.append(RBM(input_size, size, epochs=5, learning_rate=0.25, batchsize=128))
        input_size = size

    # For each RBM in our list
    for rbm in rbm_list:
        print 'New RBM:'
        # Train a new one
        rbm.train(inpX)
        # Return the output layer
        # inpX = rbm.rbm_outpt(inpX)
        print teX.shape, trX.shape
        # inpX = teX
        print rbm.rbm_outpt(teX).shape
        print rbm.rbm_outpt(trX).shape
        print rbm.errors
        print len(rbm.errors)
        exit()

    # for rbm in rbm_list:
    #     print rbm.w.shape, rbm.vb.shape, rbm.hb.shape
    #     print rbm.errors

        # plt.plot(rbm.errors[0::100])
        # plt.xlabel('Batch Number')
        # plt.ylabel('Error')
        # plt.legend(['Error'], loc='upper right')
        # # plt.show()
        # plt.savefig('Error.jpg')

        #     np.savetxt('w.txt', rbm.w, delimiter=',')

        # nNet = NN(RBM_hidden_sizes, trX, trY)
        # nNet.load_from_rbms(RBM_hidden_sizes, rbm_list)
        # nNet.train()


def running_data(folder, path):
    train, test, lbl_train, lbl_test = load_variables(folder, path)  # "folder" first, "path" second
    print folder, train.shape, test.shape

    RBM_hidden_sizes = [100]  # create 1 layers of RBM with size 100

    # Since we are training, set input as training data

    # Create list to hold our RBMs
    rbm_list, errors = [], []

    # Size of inputs is the number of inputs in the training set
    input_size = train.shape[1]
    batchsize = (train.shape[0] / 40) + 1

    # For each RBM we want to generate
    for i, size in enumerate(RBM_hidden_sizes):
        print 'RBM: ', i, ' ', input_size, '->', size
        rbm_list.append(RBM(input_size, size, epochs=45, learning_rate=1.0, batchsize=batchsize))
        input_size = size

    # For each RBM in our list
    for i in range(0, len(rbm_list)):
        rbm = rbm_list[i]
        print 'New RBM:'
        # Train a new one
        rbm.train(train)
        # Return the output layer for training data
        train = rbm.rbm_outpt(train)
        test = rbm.rbm_outpt(test)
        # Get errors
        errors.append(np.array(rbm.errors))

    errors = np.array(errors)
    errors = np.mean(errors, axis=0)
    print folder, train.shape, test.shape, errors.shape

    pickle.dump(train, open(path + folder + '/ftr_dbn_train.p', 'wb'))
    pickle.dump(test, open(path + folder + '/ftr_dbn_test.p', 'wb'))
    pickle.dump(errors, open(path + folder + '/dbn_errors.p', 'wb'))

    plt.plot(errors)
    plt.xlabel('Epoch Number')
    plt.ylabel('Error')
    plt.legend(['Error'], loc='upper right')
    plt.savefig(path + folder + '/Error_%s_layers_%d_sizes_%d.jpg' % (folder, len(RBM_hidden_sizes), RBM_hidden_sizes[0]))
    plt.close()


def output_data(folder, path):
    dbn_train, dbn_test, dbn_errors = load_dbn_ftr(folder, path)
    # w, b = pickle.load(open(folder + '/w.p', 'rb')), pickle.load(open(folder + '/vb.p', 'rb'))
    # print dbn_train.shape, dbn_test.shape, dbn_errors.shape
    # print dbn_errors
    # print dbn_train[0]
    # print dbn_test[0]
    return dbn_train, dbn_test, dbn_errors


if __name__ == '__main__':
    # example for DBN
    # running_MNIST()

    path_ = '../SemanticFeaturesBugCount/'
    # path_ = '../data/'
    folders_ = os.listdir(path_)
    for f in folders_:
        # f = 'dankurka_mgwt'
        running_data(f, path_)
        # exit()

    # for f in folders_:
    #     output_data(f, path_)

