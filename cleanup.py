import os
import sys
import shutil

current_dir = os.getcwd()

if "--clean-generated" in sys.argv:
    generated_dir = os.path.join(current_dir, "src", "generated")
    if os.path.isdir(generated_dir):
        shutil.rmtree(generated_dir)

for root, dirs, files in os.walk(current_dir):
    for file in files:
        if file.endswith(".class"):
            os.remove(os.path.join(root, file))
            
if os.path.isdir(os.path.join(current_dir, "build")):
    shutil.rmtree("build")