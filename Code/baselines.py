from os import listdir
from os.path import isfile, join
import pandas as pd
import numpy as np
from sklearn.feature_extraction.text import CountVectorizer
from sklearn.linear_model import LogisticRegression
from sklearn.tree import DecisionTreeClassifier
from sklearn.svm import LinearSVC
from sklearn.metrics import accuracy_score, f1_score, precision_score, recall_score
from sklearn.naive_bayes import GaussianNB


def load_files(path_file):
    onlyfiles = [f for f in listdir(path_file) if isfile(join(path_file, f))]
    return onlyfiles


def load_data(path_file):
    lines = list(open(path_file, "r").readlines())
    return lines


def load_project_data(path_file):
    dataset = pd.read_csv(path_file, sep=';')
    col_ftr = "wmc;dit;noc;cbo;rfc;lcom;ca;ce;npm;lcom3;loc;dam;moa;mfa;cam;ic;cbm;amc;nr;ndc;nml;isOld;ndpv;ner".split(
        ";")
    col_labels = ["bugs"]
    ftr, labels = dataset[col_ftr], dataset[col_labels]
    ftr = ftr.replace("-", 0)
    try:
        labels["bugs"] = (labels["bugs"] >= 1).astype(int)
        ftr, labels = ftr.as_matrix(), np.ravel(labels.new_bugs.as_matrix())
    except:
        print ""
    print ftr.shape, labels.shape
    return ftr, labels


def change_col_name(path_write, name, path_file):
    lines = load_data(path_file)
    new_lines = [lines[i] for i in xrange(1, len(lines))]
    col_names = [
        "Project;Version;Class;wmc;dit;noc;cbo;rfc;lcom;ca;ce;npm;lcom3;loc;dam;moa;mfa;cam;ic;cbm;amc;nr;ndc;nml;isOld;ndpv;ner;bugs"
        + "\n"]
    new_lines = col_names + new_lines
    file = open(path_write + "/" + name, 'w')
    for item in new_lines:
        file.write(item)
    file.close()


def make_path(path_file, names):
    path_files = [path_file + "/" + n for n in names]
    return path_files


def find_path_files(name, path_files):
    for p in path_files:
        if name in p:
            return p
    print "You need to give correct name file"
    exit()


def load_train_test(train_path, test_path, path_files):
    source, target = find_path_files(name=train_path, path_files=path_files), \
                     find_path_files(name=test_path, path_files=path_files)
    return source, target


def baselines_algorithm(X_train, X_test, y_train, y_test, algorithm):
    if algorithm == "svm":
        clf = LinearSVC(random_state=0)
    elif algorithm == "lr":
        clf = LogisticRegression()
    elif algorithm == "dt":
        clf = DecisionTreeClassifier()
    elif algorithm == "nb":
        clf = GaussianNB()
    else:
        print "Wrong algorithm name -- please retype again"
        exit()

    clf.fit(X=X_train, y=y_train)
    print "acc", accuracy_score(y_true=y_test, y_pred=clf.predict(X_test))
    print "prc", precision_score(y_true=y_test, y_pred=clf.predict(X_test))
    print "recall", recall_score(y_true=y_test, y_pred=clf.predict(X_test))
    print "f1", f1_score(y_true=y_test, y_pred=clf.predict(X_test))


def loading_project_pairs(name):
    trains, tests = list(), list()
    if name == "within-project":
        trains.append("ant-1.5.csv"), tests.append("ant-1.6.csv")
        trains.append("ant-1.6.csv"), tests.append("ant-1.7.csv")
        trains.append("camel-1.2.csv"), tests.append("camel-1.4.csv")
        trains.append("camel-1.4.csv"), tests.append("camel-1.6.csv")
        trains.append("log4j-1.0.csv"), tests.append("log4j-1.1.csv")
        trains.append("lucene-2.0.csv"), tests.append("lucene-2.2.csv")
        trains.append("lucene-2.2.csv"), tests.append("lucene-2.4.csv")
        trains.append("xalan-2.4.csv"), tests.append("xalan-2.5.csv")
        trains.append("xerces-1.2.csv"), tests.append("xerces-1.3.csv")
        trains.append("ivy-1.4.csv"), tests.append("ivy-2.0.csv")
        trains.append("synapse-1.0.csv"), tests.append("synapse-1.1.csv")
        trains.append("synapse-1.1.csv"), tests.append("synapse-1.2.csv")
        trains.append("poi-1.5.csv"), tests.append("poi-2.5.csv")
        trains.append("poi-2.5.csv"), tests.append("poi-3.0.csv")
    else:
        print "wrong names"
        exit()
    return trains, tests


def make_project_pairs(names):
    trains, tests = [], []
    for i in xrange(0, len(names) - 1):
        for j in xrange(i + 1, len(names)):
            trains.append(names[i]), tests.append(names[j])
    return trains, tests


if __name__ == "__main__":
    # path_read, path_write = "../PROMISE/data", "../PROMISE/data_renameCol"
    # files = load_files(path_file=path_read)
    # path_files_ = make_path(path_file=path_read, names=files)
    # for i in xrange(len(files)):
    #     print files[i], path_files_[i]
    #     change_col_name(path_write=path_write, name=files[i], path_file=path_files_[i])

    path_ = "../PROMISE/data_renameCol"
    files = sorted(load_files(path_file=path_))
    path_files_ = make_path(path_file=path_, names=files)
    trains, tests = make_project_pairs(names=files)
    for train, test in zip(trains, tests):
        train_, test_ = load_train_test(train_path=train, test_path=test, path_files=path_files_)
        train_ftr, train_labels = load_project_data(path_file=train_)
        test_ftr, test_labels = load_project_data(path_file=test_)
        print train, test
        algorithm_name = "nb"
        print algorithm_name
        baselines_algorithm(X_train=train_ftr, X_test=test_ftr, y_train=train_labels,
                            y_test=test_labels, algorithm=algorithm_name)
        algorithm_name = "lr"
        print algorithm_name
        baselines_algorithm(X_train=train_ftr, X_test=test_ftr, y_train=train_labels,
                            y_test=test_labels,
                            algorithm=algorithm_name)
        algorithm_name = "dt"
        print algorithm_name
        baselines_algorithm(X_train=train_ftr, X_test=test_ftr, y_train=train_labels,
                            y_test=test_labels,
                            algorithm=algorithm_name)
