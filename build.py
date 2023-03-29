import os
import sys
import subprocess

def check_cwd():
    cwd = os.getcwd()
    subdirs = ["grammar", "lib", "src"]
    
    for dir in subdirs:
        if not os.path.isdir(os.path.join(cwd, dir)):
            print("The current working directory is wrong. Please `cd` into the base directory and then execute this script.")
            return False
        
    return True

def generate_antlr_files():
    output = subprocess.check_output(["antlr4", "-o", "src/generated", "-no-listener", "-visitor", "-package", "generated", "-Xexact-output-dir", "grammar/SimpleGo.g4"])
    
    if output:
        print("Failed to build the grammar. Aborting.")
        sys.exit()
    
def build_java_project():
    output = subprocess.check_output("javac -cp .;../lib/antlr-4.11.1-complete.jar StupsCompiler.java -d ../build", cwd="src").decode()
    
    if ("errors" in output):
        print("Failed to build Java project. Aborting")
        sys.exit()
    
    
if check_cwd():
    generate_antlr_files()
    build_java_project() 
    print("Successfully built the project.")
    print("Now `cd` into `build` and run `java StupsCompiler -compile <Testfile.go>`")