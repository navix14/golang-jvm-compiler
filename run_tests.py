import os
import subprocess

cwd = os.getcwd()

tests_dir = os.path.join(cwd, "tests")

for test_file in os.listdir(tests_dir):
    if test_file.endswith(".go"):
        output = subprocess.check_output(f"java -cp .;../lib/antlr-4.11.1-complete.jar StupsCompiler -compile ../tests/{test_file}", cwd="build").decode()
        print(output)