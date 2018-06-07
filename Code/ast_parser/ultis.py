def load_javafile(file):
    lines = list(open(file, "r").readlines())
    string = ""
    for l in lines:
        string += l
    return string


if __name__ == "__main__":
    path_file = "OakOSGiRepositoryFactory.java"
    print load_javafile(file=path_file)
