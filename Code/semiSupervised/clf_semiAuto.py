import cPickle as pickle
import sys
sys.path.append('../services/')
from preprocessing import load_variables
sys.path.append('../dbnTF_code/')
from clf_model import convert_label, clf_algorithm


def load_semiAutoFtr(nameFile, folder, path):
    x_train, x_test, y_train, y_test = load_variables(folder, path)
    train = pickle.load(open('./' + nameFile + '/' + folder + '_ftr_semiAuto_train.p', 'rb'))
    test = pickle.load(open('./' + nameFile + '/' + folder + '_ftr_semiAuto_test.p', 'rb'))
    y_train, y_test = convert_label(y_train), convert_label(y_test)
    return train, test, y_train, y_test


if __name__ == '__main__':
    nameFile = 'top_program_v2'
    with open('../dbnTF_code/' + nameFile + '.txt') as f:
        folders_ = f.read().splitlines()

    path_ = '../SemanticFeaturesBugCount/'
    clfs = ['LR', 'SVM', 'NB', 'DT']
    for f in folders_:
        x_train, x_test, y_train, y_test = load_semiAutoFtr(nameFile, f, path_)
        clf_algorithm(f, 'RF', x_train, y_train, x_test, y_test)
