# Simple Go Compiler #

## Directory Structure ##
- The grammar file for the Go (subset) language is located in the `grammar` directory.
- The necessary runtime library for ANTLR4 is in the `lib` directory
- All the source files are contained in `src`
  * `ast`: AST visitor & AST printer  
  * `commandlineparser`: Simple command line parser
  * `common`: Cross-package classes
  * `errorlisteners`: Custom ANTLR error listeners to generate nicer error messages
  * `functionresolver`: First tree walk pass to collect function information
  * `generated`: Contains generated ANTLR classes
  * `scopes`: Environment class to facilitate scope-based symbol lookup
  * `typechecking`: Typechecking visitor
- Some example test cases for the compiler are in `tests`
  * All test cases can be run using the `run_tests.py` script
- The `build.py` script can be used to build the project
- The `cleanup.py` script is used to delete all `.class` files generated after compilation using `javac`

## Dependencies
This project is built and executed using **OpenJDK 16.0.2**
```bash
λ java -version
openjdk version "16.0.2" 2021-07-20
OpenJDK Runtime Environment Temurin-16.0.2+7 (build 16.0.2+7)
OpenJDK 64-Bit Server VM Temurin-16.0.2+7 (build 16.0.2+7, mixed mode, sharing)
```

```bash
λ javac -version
javac 16.0.2
```

Also, **ANTLR version 4.11.1** was used.

## Building and running ##
**NOTE:**
*If you do not want to execute all these steps, there is a simple <code>build.py</code> script that automates everything.*

1. It is first needed to generate the ANTLR base classes from the grammar file `SimpleGo.g4`

    `SimpleGoCompiler` being the current working directory, execute the following command:
    ```bash
    antlr4 -o src/generated -no-listener -visitor -package generated -Xexact-output-dir grammar/SimpleGo.g4
    ```

    As a result, a `generated` directory should be created inside of the `src` directory, containing all required ANTLR API base classes.

    **NOTE:** In case this command yields an error, make sure you have the ANTLR toolchain installed:
    ```bash
    pip install antlr4-tools
    ```


2. To build the actual project, execute the following commands:
    ```bash
    # Windows
    cd src
    javac -cp .;../lib/antlr-4.11.1-complete.jar SimpleGoCompiler.java -d ../build

    # Alternatively
    cd src
    set classpath=.;../lib/antlr-4.11.1-complete.jar
    javac SimpleGoCompiler.java -d ../build
    ```

    This will place all class files into the `build` directory.


3. Running the program is then simply done using:
    ```bash
    cd ../build
    java -cp .;../lib/antlr-4.11.1-complete.jar SimpleGoCompiler -compile <Test.go>
    ```

    Or if the classpath has been set temporarily using `set`:
    ```bash
    cd ../build
    java SimpleGoCompiler -compile <Test.go>
    ```

    **Example:**
    ```bash
    java SimpleGoCompiler -compile ../tests/test_fibonacci.go
    ```

## Command line options ##
```bash
java SimpleGoCompiler [-compile | -liveness | -printast <true | false>] <Filename.go>

Options:
  -compile: Typechecks and compiles an input .go file into Jasmin bytecode.
  -liveness: Prints out the required number of registers to run program.
  -printast: Prints the AST. When given 'true', all node attributes are also printed.
```

<br>

## Cleanup ##
The included `cleanup.py` script can be used to quickly get rid of `.class` files and the generated ANTLR files:
```bash
python cleanup.py [--clean-generated]
```
