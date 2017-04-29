import tensorflow as tf
import numpy as np
import matplotlib.pyplot as plt
from tensorflow.examples.tutorials.mnist import input_data
from autoencode import autoencoder
import sklearn.preprocessing
import multiprocessing
import cPickle as pickle

import sys
sys.path.append('../services/')
from preprocessing import load_variables
sys.path.append('../dbnTF_code/')
from clf_model import convert_label, perf_measure


def semi_trainingMNIST():
    mnist = input_data.read_data_sets('MNIST_data', one_hot=True)
    trX, trY, teX, teY = mnist.train.images, mnist.train.labels, mnist.test.images, mnist.test.labels

    learning_rate = 0.001
    batch_size = 100
    training_epochs = 25
    display_step = 1
    hidden_unit = [128]

    ae = autoencoder(input_size=trX.shape[1], output_size=trY.shape[1], hidden_size=hidden_unit
                     , learning_rate=learning_rate, epochs=training_epochs, batch_size=batch_size)
    ae.train(trX, trY, teX, teY)
    print ae.encoder_train.shape, ae.encoder_test.shape
    # encoder_ = ae.output_data(trX)
    # k = ae.weights_out

    # print trX.shape, teX.shape
    # print ae.output(trX).shape, ae.output(teX).shape
    # print ae.output_data(trX).shape

    plt.plot(ae.costs[:])
    plt.plot(ae.costs_su[:])
    plt.plot(ae.costs_un[:])
    plt.xlabel('Batch Number')
    plt.ylabel('Error')
    plt.legend(['Total Cost', 'Supervised Cost', 'Unsupervised Cost'], loc='upper right')
    # plt.show()
    plt.savefig('CostIteration.pdf')
    plt.close()
    # # print k.shape


def one_hot_vector(y):
    n_values = np.max(y) + 1
    y = np.eye(n_values)[y]
    return y


def results_print(TP_, FP_, FN_, TN_):
    if (TP_ + FP_) == 0:
        P = 0
    else:
        P = TP_ / float(TP_ + FP_)

    if (TP_ + FN_) == 0:
        R = 0
    else:
        R = TP_ / float(TP_ + FN_)

    if (P + R) == 0:
        F = 0
    else:
        F = 2 * P * R / (P + R)
    return P, R, F


def run_semi_autoencoder(input, folder, path):
    x_train, x_test, y_train, y_test = load_variables(folder, path)
    y_train_, y_test_ = one_hot_vector(convert_label(y_train)), one_hot_vector(convert_label(y_test))
    print folder, x_train.shape, x_test.shape, y_train_.shape, y_test_.shape

    learning_rate, batch_size, epochs, display_step, hidden_unit = 0.001, 40, 100, 5, [52]

    # if x_train.shape[1] > 1000:
    #     hidden_unit = [1000, 100]
    # if x_train.shape[0] > 1000:
    #     batch_size = 20
    # elif x_train.shape[0] < 150:
    #     batch_size = 6
    # else:
    #     batch_size = 8

    model = autoencoder(input_size=x_train.shape[1], output_size=y_train_.shape[1], hidden_size=hidden_unit
                        , learning_rate=learning_rate, epochs=epochs, batch_size=batch_size, display_step=display_step)
    model.train(x_train, y_train_, x_test, y_test_)
    y_pred_test = model.y_pred
    TP_, FP_, FN_, TN_ = perf_measure(y_test, y_pred_test)
    P, R, F = results_print(TP_, FP_, FN_, TN_)
    print '%s Precision %f Recall %f F1 %f' % (folder, P, R, F)

    plt.plot(model.costs[:])
    plt.plot(model.costs_su[:])
    plt.plot(model.costs_un[:])
    plt.xlabel('Batch Number')
    plt.ylabel('Error')
    plt.legend(['Total Cost', 'Supervised Cost', 'Unsupervised Cost'], loc='upper right')
    # plt.show()
    plt.savefig('./' + input + '/' + folder + '_CostIteration.pdf')
    plt.close()

    pickle.dump(model.encoder_train, open('./' + input + '/' + folder + '_ftr_semiAuto_train.p', 'wb'))
    pickle.dump(model.encoder_test, open('./' + input + '/' + folder + '_ftr_semiAuto_test.p', 'wb'))
    pickle.dump(model.costs_su, open('./' + input + '/' + folder + '_cost_supervised.p', 'wb'))
    pickle.dump(model.costs_un, open('./' + input + '/' + folder + '_cost_unsupervised.p', 'wb'))
    pickle.dump(model.y_pred, open('./' + input + '/' + folder + '_pred_test.p', 'wb'))

    # print model.y_pred

if __name__ == '__main__':
    # for i in range(0, 1):
    #     semi_trainingMNIST()

    # nameFile = 'top_program_25'
    nameFile = 'top_program_v2'
    with open('../dbnTF_code/' + nameFile + '.txt') as f:
        folders_ = f.read().splitlines()

    path_ = '../SemanticFeaturesBugCount/'
    for i in range(0, len(folders_)):
        # folders_[i] = 'apache_tika'
        run_semi_autoencoder(nameFile, folders_[i], path_)
        # for i in range(0, 10):
        #     run_semi_autoencoder(f, path_)