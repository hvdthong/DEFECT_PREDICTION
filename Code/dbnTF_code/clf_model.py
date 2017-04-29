import os
import numpy as np
from dbn_run import output_data
from sklearn.linear_model import LogisticRegression
from sklearn import svm
from sklearn.naive_bayes import GaussianNB
from sklearn import tree
from sklearn.ensemble import RandomForestClassifier
import pandas as pd
import math

import sys

sys.path.append('../services/')
from preprocessing import load_variables


def perf_measure(y_actual, y_hat):
    TP = 0
    FP = 0
    TN = 0
    FN = 0

    for i in range(len(y_hat)):
        if y_actual[i] == y_hat[i] == 1:
            TP += 1
    for i in range(len(y_hat)):
        if y_hat[i] == 1 and y_actual[i] != y_hat[i]:
            FP += 1
    for i in range(len(y_hat)):
        if y_hat[i] == 0 and y_actual[i] != y_hat[i]:
            FN += 1
    for i in range(len(y_hat)):
        if y_actual[i] == y_hat[i] == 0:
            TN += 1

    return TP, FP, FN, TN


def convert_label(y):
    y_ = y
    for i in range(0, len(y)):
        if y[i] == 0:
            y_[i] = 0
        elif y[i] > 0:
            y_[i] = 1
    return np.array(y_)


def count_pos_neg(y):
    cnt_pos = np.count_nonzero(y)
    cnt_neg = 0
    for i in y:
        if i == 0:
            cnt_neg += 1
    return cnt_pos, cnt_neg


def clf_algorithm(folder, clf, X_train, y_train, X_test, y_test):
    if clf == 'LR':
        model = LogisticRegression(class_weight='balanced', max_iter=20000)
    elif clf == 'SVM':
        model = svm.LinearSVC(class_weight='balanced')
    elif clf == 'NB':
        model = GaussianNB()
    elif clf == 'DT':
        model = tree.DecisionTreeClassifier(class_weight='balanced')
    elif clf == 'RF':
        model = RandomForestClassifier()

    model.fit(X_train, y_train)
    predict_ = model.predict(X_test)
    TP_, FP_, FN_, TN_ = perf_measure(y_test, predict_)

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
    print '%s Classification %s Precision %f Recall %f F1 %f' % (folder, clf, P, R, F)
    return P, R, F


def convert_label_codeFtr(y):
    y_ = [0] * len(y)
    y = y.tolist()
    for i in range(0, len(y)):
        if y[i] == 0 or math.isnan(y[i]):
            y_[i] = 0
        else:
            y_[i] = 1
    return np.array(y_)


def clf_codeFtr(folder, clf, features, train, test):
    train, test = train.loc[train['project'] == folder], test.loc[test['project'] == folder]
    X_train, X_test = train[features], test[features]
    y_train, y_test = train['bug_count'], test['bug_count']
    # print folder, len(features), X_train.shape, X_test.shape, Y_train.shape, Y_test.shape
    y_train, y_test = convert_label_codeFtr(y_train), convert_label_codeFtr(y_test)
    # print Y_train.shape, Y_test.shape

    X_train, X_test = X_train.fillna(0), X_test.fillna(0)
    p, r, f = clf_algorithm(folder, clf, X_train, y_train, X_test, y_test)
    return p, r, f


def density_matrix(x):
    dimension = x.shape[0] * x.shape[1]
    non_zero = np.count_nonzero(x)
    return non_zero / float(dimension)


if __name__ == '__main__':
    path_ = '../SemanticFeaturesBugCount/'
    print path_

    ####################################################################################
    ####################################################################################
    # folders_ = os.listdir(path_)
    # # clfs = ['LR', 'SVM', 'NB', 'DT']
    # for f in folders_:
    # #     # if f == 'b3log_b3log-solo':
    #     dbn_train, dbn_test, dbn_errors = output_data(f, path_)
    #     ftr_train, ftr_test, lbl_train, lbl_test = load_variables(f, path_)
    #     lbl_train, lbl_test = convert_label(lbl_train), convert_label(lbl_test)
    #     tr_cnt_pos, tr_cnt_neg = count_pos_neg(lbl_train)
    #     te_cnt_pos, te_cnt_neg = count_pos_neg(lbl_test)
    #     if (tr_cnt_pos > 10) and (tr_cnt_neg > 10) and (len(lbl_train) >= 200) \
    #             and (te_cnt_pos > 10) and (te_cnt_neg > 10):
    #         print f
    # #         for clf in clfs:
    # #             clf_algorithm(f, clf, dbn_train, lbl_train, dbn_test, lbl_test)
    # #     else:
    # #         print '%s invalid_data' % f
    ####################################################################################
    ####################################################################################
    # with open('./top_program_25.txt') as f:
    #     folders_ = f.read().splitlines()

    # with open('./top_program_v2.txt') as f:
    #     folders_ = f.read().splitlines()
    # clfs = ['LR', 'NB', 'DT']
    # for clf in clfs:
    #     precision, recall, f1 = [], [], []
    #     for f in folders_:
    #         dbn_train, dbn_test, dbn_errors = output_data(f, path_)
    #         ftr_train, ftr_test, lbl_train, lbl_test = load_variables(f, path_)
    #         lbl_train, lbl_test = convert_label(lbl_train), convert_label(lbl_test)
    #         p, r, f = clf_algorithm(f, clf, dbn_train, lbl_train, dbn_test, lbl_test)
    #         precision.appmend(p), recall.append(r), f1.append(f)
    #     precision, recall, f1 = np.array(precision), np.array(recall), np.array(f1)
        # mean_p, mean_r, mean_f = np.mean(precision), np.mean(recall), np.mean(f1)
        # std_p, std_r, std_f = np.std(precision), np.std(recall), np.std(f1)
    #     print 'Mean of precision %f, recall %f, and f1 %f' % (mean_p, mean_r, mean_f)
    #     print 'Std of precision %f, recall %f, and f1 %f' % (std_p, std_r, std_f)
    ####################################################################################
    ####################################################################################
    # with open('./top_program_25.txt') as f:
    #     folders_ = f.read().splitlines()

    with open('./top_program_v2.txt') as f:
        folders_ = f.read().splitlines()

    for f in folders_:
        ftr_train, ftr_test, lbl_train, lbl_test = load_variables(f, path_)
        tr_cnt_pos, tr_cnt_neg = count_pos_neg(lbl_train)
        te_cnt_pos, te_cnt_neg = count_pos_neg(lbl_test)
        # print 'Positive label of project %s: %f' % (f, tr_cnt_pos / float(tr_cnt_pos + tr_cnt_neg))
        # print 'Number of files in training data: %f' % (len(lbl_train))

        # print '%s density_train %f density_test %f positive_ratio_train %f positive_ratio_test %f train %s test %s' % (
        #     f, density_matrix(ftr_train), density_matrix(ftr_test),
        #     tr_cnt_pos / float(tr_cnt_pos + tr_cnt_neg),
        #     te_cnt_pos / float(te_cnt_pos + te_cnt_neg), str(ftr_train.shape), str(ftr_test.shape))

        print f, tr_cnt_pos, tr_cnt_neg, te_cnt_pos, te_cnt_neg

    ####################################################################################
    ####################################################################################
    # with open('./top_program_25.txt') as f:
    #     folders_ = f.read().splitlines()

    # with open('./top_program_v2.txt') as f:
    #     folders_ = f.read().splitlines()
    #
    # with open('./all_projects_ftr.txt') as f:
    #     ftrs = f.read().splitlines()
    # print len(folders_), len(ftrs)
    #
    # df_train = pd.read_csv('./all_projects_train.csv', sep=',')
    # df_test = pd.read_csv('./all_projects_test.csv', sep=',')
    # print df_train.shape, df_test.shape
    # clfs = ['LR', 'NB', 'DT']
    # for clf in clfs:
    #     precision, recall, f1 = [], [], []
    #     for f in folders_:
    #         p, r, f = clf_codeFtr(f, clf, ftrs, df_train, df_test)
    #         precision.append(p), recall.append(r), f1.append(f)
    #
    #     precision, recall, f1 = np.array(precision), np.array(recall), np.array(f1)
    #     mean_p, mean_r, mean_f = np.mean(precision), np.mean(recall), np.mean(f1)
    #     std_p, std_r, std_f = np.std(precision), np.std(recall), np.std(f1)
    #     print 'Mean of precision %f, recall %f, and f1 %f' % (mean_p, mean_r, mean_f)
    #     print 'Std of precision %f, recall %f, and f1 %f' % (std_p, std_r, std_f)
