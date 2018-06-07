import javalang as jl
from ultis import load_javafile

# path_file = "./java_files/Main_2.java"
path_file = "./java_files/OakOSGiRepositoryFactory.java"
str_file = load_javafile(file=path_file)
# tree = jl.parse.parse(
#     "package javalang.brewtab.com; public class Test {public static void main (String[] args){}}")
tree = jl.parse.parse(str_file)
for path, node in tree:
    if str(node) == "MethodDeclaration":
        print node, node.name
        exit()
    print node
