import os
import numpy as np
import cPickle as pickle
import sys


def get_data(path):
    train, test, lbl_train, lbl_test = [], [], [], []

    with open(path + '/train.txt') as f:
        lines = f.read().splitlines()
        lines = [l.split(',') for l in lines]
        data = [l[1:] for l in lines]
        label = [int(l[0]) for l in lines]
        train, lbl_train = data, label

    with open(path + '/test.txt') as f:
        lines = f.read().splitlines()
        lines = [l.split(',') for l in lines]
        data = [l[1:] for l in lines]
        lbl_lines = [int(l[0]) for l in lines]
        test, lbl_test = data, lbl_lines
    return train, test, np.array(lbl_train), np.array(lbl_test)


def dictionary(train, test):
    dict_ = []
    total = train + test
    words = [w for l in total for w in l]
    lengths = [len(l) for l in total]

    for w in words:
        if w not in dict_:
            dict_.append(w)
    return sorted(dict_), max(lengths)


def mapping(data, dict, max_len):
    ftrs = []
    for l in data:
        ftr = [0] * max_len
        for i in range(0, len(l)):
            index = dict.index(l[i])
            ftr[i] = index / (float(len(dict)))
        ftrs.append(np.array(ftr))
    return np.array(ftrs)


def save_variables(folder, path):
    train_, test_, lbl_train_, lbl_test_ = get_data(path + folder)
    dict_, max_len_ = dictionary(train_, test_)

    ftr_train = mapping(train_, dict_, max_len_)
    ftr_test = mapping(test_, dict_, max_len_)

    pickle.dump(ftr_train, open(path + folder + '/ftr_train.p', 'wb'))
    pickle.dump(ftr_test, open(path + folder + '/ftr_test.p', 'wb'))
    pickle.dump(lbl_train_, open(path + folder + '/lable_train.p', 'wb'))
    pickle.dump(lbl_test_, open(path + folder + '/label_test.p', 'wb'))


def load_variables(folder, path):
    train = pickle.load(open(path + folder + '/ftr_train.p', 'rb'))
    test = pickle.load(open(path + folder + '/ftr_test.p', 'rb'))
    lbl_train = pickle.load(open(path + folder + '/lable_train.p', 'rb'))
    lbl_test = pickle.load(open(path + folder + '/label_test.p', 'rb'))
    return train, test, lbl_train, lbl_test


def load_dbn_ftr(folder, path):
    dbn_train = pickle.load(open(path + folder + '/ftr_dbn_train.p', 'rb'))
    dbn_test = pickle.load(open(path + folder + '/ftr_dbn_test.p', 'rb'))
    dbn_errors = pickle.load(open(path + folder + '/dbn_errors.p', 'rb'))
    return dbn_train, dbn_test, dbn_errors


if __name__ == '__main__':
    path_ = '../SemanticFeaturesBugCount/'
    # path_ = '../data/'
    folders_ = os.listdir(path_)
    for f in folders_:
        # save_variables(f, path_)
        train, test, lbl_train, lbl_test = load_variables(f, path_)
        print f, train.shape, test.shape, lbl_train.shape, lbl_test.shape
        # print train[0]
        # print test[0]
        # exit()