
## Content
1. [Getting Started](#1-getting-started)                        (2 Pt)
2. [Running the Application](#2-running-the-application)        (1 Pt)
3. [Building the Application](#3-building-the-application)      (1 Pt)
4. [Completing the Application](#4-completing-the-application)  (2 Pts)
5. [Testing the Application](#5-testing-the-application)        (1 Pts)
6. [Generating Javadoc](#6-generating-javadoc)                  (1 Pt)
7. [Packaging the Application](#7-packaging-the-application)    (1 Pt)
8. [Checking the Project into Git](#8-checking-into-git)        (1 Pt)


&nbsp;
## 1. Getting started

Make sure to have the [Java-JDK](https://www.oracle.com/java/technologies/downloads/)
(version 17 or higher) installed and all tools show the same version (if different
versions, check `$PATH`).

Open a terminal and run:

```
java --version          ; the Java VM
javac --version         ; the Java Compiler
javadoc --version       ; the Javadoc processor
jar --version           ; the Java archiver to package .jar files
```

Same version for all Java tools, 17 or higher:

```
java 21 2023-09-19 LTS      ; Java VM
...
javac 21
javadoc 21
jar 21
```

Create a Java project with following structure.
Source code is in a dirctory called *src*.
Unit tests are in dirctory called *test* at the same level as *src*.

```
-<setup.se2>                ; project directory
  |
  +--<src>                  ; source code
  |   |
  |   +--<application>      ; package directory 'application'
  |       +-- App.java      ; class: application.App
  |
  +--<test>                 ; test code
  |   |
  |   +--<application>
  |       +-- AppTest.java  ; JUnit test class: application.AppTest
  |
  +--<lib>                  ; libraries (JUnit runner, JUnit jars)
  |   +-- junit-platform-console-standalone-1.9.2.jar
  |   +-- org.junit.<...>.jar
  |
  +--<resources>            ; scripts to compile and run code outside IDE
  |   +-- javac-options.opt         ; compile code (javac)
  |   +-- jdoc-options.opt          ; create javadoc
  |   +-- junit-options.opt         ; run JUnit tests
  |   +-- java-options.opt          ; run java JVM
  |
  +--<doc>                  ; directory where javadocs are created
  |
  +--<target>               ; directory for compiled code from src and test
      |
      +--<application>      ; package directory of compiled code
          +-- App.class, AppTest.class      ; compiled classes
```

Configure your IDE such that it produces compiled code (.class files) in a
dirctory called *target*.

- In *eclipse*, add *test* as second source directory in Build Path -> Sources
    and change the default output folder to *target*.

- For *IntelliJ*, see [*Change the output directory?*](https://www.jetbrains.com/help/idea/configure-modules.html#module-compiler-output)

- For *VSCode*, see the project settings file:
[*.vscode/settings.json*](https://gitlab.bht-berlin.de/sgraupner/setup.se2/-/blob/main/.vscode/settings.json).


&nbsp;
## 2. Running the Application

Run `App.java` in the IDE. The program factorizes numbers:

```
Hello, App!
n=36 factorized is: [2, 2, 3, 3]
```

Make sure files are in the proper place. Open a terminal and navigate
to the project directory:

```
cd .../setup.se2                            ; navigate to the project directory

find src test resources target out bin      ; list files
```

Output:

```
src
src/application
src/application/App.java
test
test/application
test/application/AppTest.java
resources
resources/java-options.opt
resources/javac-options.opt
resources/jdoc-options.opt
resources/junit-options.opt
target/                                     ; compiled classes are in 'target'
target/application
target/application/App.class
target/application/AppTest.class

find: 'out': No such file or directory      ; no 'out' or 'bin' directory as
find: 'bin': No such file or directory      ; used by eclipse or IntelliJ
```

Run the application outside the IDE from the terminal (in the project directory).
Classpath tells the JVM where to find compiled classes:

```
java --class-path=target application.App
```

Output:

```
Hello, App!
n=36 factorized is: [2, 2, 3, 3]
```

So-called `opt`-files can be used to summarize command line parameters.
Run the application using the `java-options.opt` file from the resources
directory, which includes the classpath parameter:

```
cat resources/java-options.opt              ; show opt file for java (JVM)
```

Content of resources/java-options.opt (most lines are #comments):

```
# Options for program execution for JavaVM
# --module-path <path>  - where java looks for modules
# --add-modules <modules> - modules to include in launch
#
# use:
#  java @resources/java-options.opt application.App
#
# --module-path="bin:lib" --add-modules="se1.bestellsystem"
# --enable-preview --class-path=target

--class-path=target
```

Run the application using parameters from the opt file:

```
java @resources/java-options.opt application.App    ; run with opt file
```

```
Hello, App!
n=36 factorized is: [2, 2, 3, 3]
```


&nbsp;
## 3. Building the Application

Building the application means running the compiler (*javac*) and generating all
compiled files from sources, here: *src* and tests from *test*.

Clear the target directory and *"rebuild"" the project.

```
rm -rf target/*                     ; remove all compiled files from target

cat resources/javac-options.opt     ; show the opt-file for the Java compiler (javac)
```

The command line passed to the java compiler includes the `--class-path` with the
JUnit jar files needed to compile the test classes. It also has the parameter to
tell the compiler to output compiled code to the target directory `-d target`.

For the  classpath it is **important** that Windows uses `";"` as separaor
while other systems Mac and Linux use `":"`.

The script `javac-options.opt` uses `";"` (for Windows) by default.
**For Mac and Linux**, adjust `javac-options.opt` to use `":"` as separator.

Compile source code from *src* and *test* :

```
javac @resources/javac-options.opt src/application/App.java
javac @resources/javac-options.opt test/application/AppTest.java
```


Verify the project has been rebuild and compiled classes are in the *target* directory:

```
find target                         ; list content of target directory
```

Output:

```
target                              ; content of target has been rebuilt
target/application
target/application/App.class
target/application/AppTest.class
```


&nbsp;
## 4. Completing the Application

In order for the program to factorize arbitrary numbers *n*, the method
`List<Integer> factorize(int n)` must be completed.

Complete this function e.g. using the
[prime-factor algorithm](https://www.geeksforgeeks.org/prime-factor/) and
run the application again with a new *n*=1092 as argument:

```
java @resources/java-options.opt application.App 1092   ; run with opt file or without

java --class-path=target application.App 1092           ; pass 1092 as parameter
```

Output: *n*=1092 = 2 * 2 * 3 * 7 * 13 

```
Hello, App!
n=1092 factorized is: [2, 2, 3, 7, 13]
```

Try also other numbers *n*, e.g.:
- *n*=109237 -> [313, 349] or
- *n*=59,534,165 -> [5, 109, 313, 349],
- *n*=59,534,166 -> [2, 3, 23, 151, 2857]
- *n*=59,534,167 -> [7, 7, 7, 11, 31, 509]


&nbsp;
## 5. Testing the Application

To run JUnit tests, compile tests (if not already):

```
javac @resources/javac-options.opt test/application/AppTest.java
```

Run JUnit tests from `AppTest.java` in your IDE. If tests fail, locate the failing
test and understand why it is failing. Fix your code such that the test passes.
Never "fix" or comment tests.

When tests pass in the IDE, run tests outside the IDE with or without opt file:

```
java @resources/junit-options.opt --scan-class-path     ; running tests with opt file

                                                        ; running tests plain
java -jar lib/junit-platform-console-standalone-1.9.2.jar --class-path target --scan-class-path
```

JUnit tests are run with a test runner that scans output directories for compiled
files with JUnit @Test annotations and executes them.

The test runner in `./lib` that used here is `junit-platform-console-standalone-1.9.2.jar`
from the [maven repository](https://mvnrepository.com/artifact/org.junit.platform/junit-platform-console-standalone).


Output of test runs:

```
Thanks for using JUnit! Support its development at https://junit.org/sponsoring
.
+-- JUnit Jupiter [OK]
| '-- AppTest [OK]
|   +-- test0003_FactorizeExceptionCases() [OK]
|   +-- test0001_FactorizeRegularCases() [OK]
|   '-- test0002_FactorizeCornerCases() [OK]
+-- JUnit Vintage [OK]
'-- JUnit Platform Suite [OK]

Test run finished after 212 ms
[         4 containers found      ]
[         0 containers skipped    ]
[         4 containers started    ]
[         0 containers aborted    ]
[         4 containers successful ]
[         0 containers failed     ]
[         3 tests found           ]     <- 3 tests found
[         0 tests skipped         ]
[         3 tests started         ]
[         0 tests aborted         ]
[         3 tests successful      ]     <- 3 tests successful
[         0 tests failed          ]     <- 0 tests failed
```


&nbsp;
## 6. Generating Javadoc

Javadoc can be generated with the javadoc opt file:

```
javadoc @resources/jdoc-options.opt application
```
Output of the javadoc generation process:
```
Loading source files for package application...
Constructing Javadoc information...
Building index for all the packages and classes...
Standard Doclet version 21+35-LTS-2513
Building tree for all the packages and classes...
Generating doc\application\App.html...
Generating doc\application\package-summary.html...
Generating doc\application\package-tree.html...
Generating doc\overview-tree.html...
Building index for all classes...
Generating doc\allclasses-index.html...
Generating doc\allpackages-index.html...
Generating doc\index-all.html...
Generating doc\search.html...
Generating doc\index.html...
Generating doc\help-doc.html...
```
The final result is in the *doc* directory.
```
find doc
```
Output of the javadoc generation process:
```
doc
doc/allclasses-index.html
doc/allpackages-index.html
doc/application
doc/application/App.html
doc/application/package-summary.html
doc/application/package-tree.html
doc/copy.svg
doc/element-list
doc/help-doc.html
doc/index-all.html
doc/index.html                  <-- entry file for javadoc
doc/legal
doc/legal/COPYRIGHT
doc/legal/jquery.md
doc/legal/jqueryUI.md
doc/legal/LICENSE
doc/link.svg
doc/member-search-index.js
doc/module-search-index.js
doc/overview-tree.html
doc/package-search-index.js
doc/resources
doc/resources/glass.png
doc/resources/x.png
doc/script-dir
doc/script-dir/jquery-3.6.1.min.js
doc/script-dir/jquery-ui.min.css
doc/script-dir/jquery-ui.min.js
doc/script.js
doc/search-page.js
doc/search.html
doc/search.js
doc/stylesheet.css
doc/tag-search-index.js
doc/type-search-index.js
```

Open `doc/index.html` in a browser.


&nbsp;
## 7. Packaging the Application

Finally, generated artefacts (e.g. compiled classed in *target*)
need to be packaged for delivery.

Java uses the Java-Archive (JAR) format generated with the
[jar](https://docs.oracle.com/en/java/javase/21/docs/specs/man/jar.html)
command for packaging.

```
jar --help                              ; Anzeige der jar - Argumente
jar cvf app.jar -C target application   ; Erzeugen des Hello.jar files
ls -la app.jar                          ; Anzeige des erzeugten .jar – Files

-rwxr-xr-x  1 svgr2 Kein  3056 Oct 10 20:01 app.jar
```

Anzeige des Inhalts des erzeugten .jar – Files:
```
jar tvf app.jar

    0 Tue Oct 10 20:01:32 CEST 2023 META-INF/
  62 Tue Oct 10 20:01:32 CEST 2023 META-INF/MANIFEST.MF
    0 Tue Oct 10 19:44:08 CEST 2023 application/
1781 Tue Oct 10 19:45:18 CEST 2023 application/App.class
2992 Tue Oct 10 19:45:18 CEST 2023 application/AppTest.class
```

Ausführung des .jar – Files:
```
java --class-path=app.jar application.App 777

Hello, App!
n=777 factorized is: [3, 7, 37]
```

In order to directly execute, an *executable .jar* file must be
created, which includes the definition of the *main()* function in
META-INF/[MANIFEST.MF]().

The `-e` argument creates this entry in MANIFEST.MF for in `app_executable.jar`:
```
jar -c -v -f app_executable.jar -e application.App -C target application
```

Extract MANIFEST.MF from the .jar to the target directory:
```
jar xvf app_executable.jar META-INF
mv META-INF target
```

Show MANIFEST.MF:
```
cat target/META-INF/MANIFEST.MF

Manifest-Version: 1.0
Created-By: 21 (Oracle Corporation)
Main-Class: application.App       <-- class with main() function to launch
```

Run the packaged .jar file as final test:
```
java -jar app_executable.jar 777

Hello, App!
n=777 factorized is: [3, 7, 37]
```

The packaged .jar file can now be distributed.


&nbsp;
## 8. Checking into Git

To complete the project, check the project into a local Git repository.

Make sure to **not check-in** files (use *.gitignore* ):

- generated or compiled files (from *target* or *doc* ),

- not sharable project files, e.g. *.classpath* and *.project*,

- junk files, e.g. *.DS_Store* from Mac.

Check-in files:

- source files including JUnit test sources.

- opt files from the *./resources* directory.

- *.gitignore*, *README.md* .

Show the commit history:

```
git log
git log --oneline
```

Output (example):

```
644d3a5 (HEAD -> main) add .gitignore
615908c (origin/main, origin/HEAD) Initial commit
```

Show all committed files:

```
git ls-files --recurse-submodules
```

Output (may also include ./lib if you checked out the repository):

```
.gitignore
.vscode/settings.json           ; optional commit
README.md
lib/...                         ; may be committed
resources/java-options.opt
resources/javac-options.opt
resources/jdoc-options.opt
resources/junit-options.opt
src/application/App.java
test/application/AppTest.java
```
