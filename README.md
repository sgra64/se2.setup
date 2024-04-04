## Content
1. [Getting Started](#1-getting-started)                        (2 Pts)
2. [Sourcing the Project](#2-sourcing-the-project)              (1 Pt)
3. [Building the Application](#3-building-the-application)      (1 Pt)
4. [Running the Application](#4-running-the-application)        (1 Pt)
5. [Completing the Application](#5-completing-the-application)  (3 Pts)
6. [Testing the Application](#6-testing-the-application)        (1 Pts)
7. [Generating Javadoc](#7-generating-javadoc)                  (1 Pt)
8. [Packaging the Application](#8-packaging-the-application)    (1 Pt)
9. [Checking the Project into Git](#9-checking-into-git)        (1 Pt)


&nbsp;
## 1. Getting started

Make sure to have the [Java-JDK](https://www.oracle.com/java/technologies/downloads/)
(version 17 or higher) installed and all tools show the same version.

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
Unit tests are in dirctory called *tests*.

```sh
--<se1.play>:
 |
 +-- README.md                          # project markup file (this file)
 |
 | # directory to source the project
 +--<.env>
 |   +-- project.sh, init.classpath, init.project, init.gitignore
 |
 | # VSCode project configurations
 +--<.vscode>
 |   +-- settings.json                  # project-specific VSCode settings
 |   +-- launch.json                    # Java/Debug launch configurtions
 |   +-- launch_terminal.sh             # terminal launch configurtions
 |
 | # local git repository
 +--<.git>
 |   +-- config, HEAD                   # git config file, HEAD pointer file
 |   +--<objects>                       # git object store
 |   +--<refs>                          # git references store
 +-- .gitignore                         # files with patterns to ignore by git
 |
 | # libraries/modules needed by the project:
 +--<libs>
 |   +--<junit>                         # JUnit .jar files
 |   |   +-- apiguardian-api-1.1.2.jar, junit-platform-commons-1.9.3.jar,
 |   |   +-- junit-jupiter-api-5.9.3.jar, opentest4j-1.2.0.jar
 |   +--<jacoco>                        # Code coverage .jar files
 |   |   +-- jacocoagent.jar  jacococli.jar
 |   +--<jackson>                       # JSON library for Java
 |   |   +-- jackson-annotations-2.13.0.jar, jackson-databind-2.13.0.jar,
 |   |       jackson-core-2.13.0.jar
 |   +-- junit-platform-console-standalone-1.9.2.jar    # JUnit runtime
 |
 | # source code:
 +--<src>
 |   +--<main>                      # Java source code with packages
 |   |   +-- module-info.java           # module defintion file
 |   |   +--<application>               # Java package 'application'
 |   |       +-- package-info.java      # 'application'-package defintion
 |   |       +-- Application.java       # main application program with main()
 |   |       +-- Factorizer.java        # class with factorize() method
 |   |
 |   +--<resources>                 # non-Java source code, mainly configuration
 |   |   +-- application.properties     # properties file for running the application
 |   |   +-- logging.properties         # logging properties
 |   |   +--<META-INF>                  # jar-packaging information
 |   |       +-- MANIFEST.MF            # jar-manifest file with main class
 |   |
 |   +--<tests>                     # Unit-test source code separated from src/main
 |       +--<application>               # mirrored package structure
 |           +-- Application_0_always_pass_Tests.java   # initial JUnit-test
 |           +-- Factorizer_Tests.java  # JUnit-test class
 |
 | # generated artefacts, compiled classes:
 +--<target>
 |   +-- application-1.0.0-SNAPSHOT.jar # executable .jar file (main artefact)
 |   +--<classes>                       # compiled Java classes (.class files)
 |   |   +-- module-info.class          # compiled module-info class
 |   |   +--<application>               # compiled 'application' package
 |   |       +-- package-info.class
 |   |       +-- Application.class
 |   |       +-- Factorizer.class
 |   |
 |   +--<test-classes>              # compiled test classes
 |       +--<application>
 |           +-- Application_0_always_pass_Tests.class
 |           +-- Factorizer_Tests.class
 |
```


&nbsp;
## 2. Sourcing the Project

Sourcing the project means to set up the environment:

```sh
source .env/project.sh
```

Executing script
[project.sh](https://gitlab.bht-berlin.de/sgraupner/setup.se2/-/blob/main/.env/project.sh?ref_type=heads)
sets environment variables and creates local project files for
*VS Code* and *eclipse* IDE:

```
setting the project environment
 - environment variables:
    - CLASSPATH
    - MODULEPATH
    - JDK_JAVAC_OPTIONS
    - JDK_JAVADOC_OPTIONS
    - JUNIT_OPTIONS
 - files created:
    - .env/.classpath
    - .classpath
    - .project
    - .gitignore
project environment is set (use 'wipe' to reset)
```

All files and environment variables can be reset with the `wipe` command
(and rebuild with `source .env/project.sh`).

Make sure files are in the proper place. Open a terminal and navigate
to the project directory:

```sh
find src
```

Output:

```
src
src/main
src/main/application
src/main/application/Application.java
src/main/application/Factorizer.java
src/main/application/Factorizer_2.java
src/main/application/package-info.java
src/main/application/RunPriority.java
src/main/module-info.java
src/resources
src/resources/application.properties
src/resources/logging.properties
src/resources/META-INF
src/resources/META-INF/MANIFEST.MF
src/tests
src/tests/application
src/tests/application/Application_0_always_pass_Tests.java
src/tests/application/Factorizer_Tests.java
```


&nbsp;
## 3. Building the Application

The *Build-Process* comprises operations such as:

 - compile source code

 - compile tests

 - build javadocs

 - package the application as '.jar'

Command `show` shows operations that are available for the *Build-Process*:

```sh
show
```

```
source | project:
  source .env/project.sh

classpath:
  echo $CLASSPATH | tr "[;:]" "\n"

compile:
  javac $(find src/main -name '*.java') -d target/classes; \
  copy src/resources target/resources

compile-tests:
  javac $(find src/tests -name '*.java') -d target/test-classes; \
  copy src/resources target/resources

resources:
  copy src/resources target/resources

run:
  java application.Application

run-tests:
  java -jar libs/junit-platform-console-standalone-1.9.2.jar \
       $(eval echo $JUNIT_OPTIONS) --scan-class-path

javadoc:
  javadoc -d docs $(eval echo $JDK_JAVADOC_OPTIONS)

clean:
  rm -rf target logs docs

wipe:
```

Execute build steps with the `build` or `mk` (make) command:

```sh
mk compile                        # compile source code
mk compile-tests                  # compile test code

mk clean compile compile-tests    # execute all commands in order
```

The last command is called a *clean build*. It clears the `target` directory
and re-compiles all source code.

The result is in the `target` directory:

```sh
find target
```

Output

```
target
target/classes
target/classes/application
target/classes/application/Application.class
target/classes/application/Factorizer.class
target/classes/application/Factorizer_2.class
target/classes/application/package_info.class
target/classes/application/RunPriority.class
target/classes/module-info.class
target/resources
target/resources/application.properties
target/resources/logging.properties
target/test-classes
target/test-classes/application
target/test-classes/application/Application_0_always_pass_Tests.class
target/test-classes/application/Factorizer_Tests.class
```


&nbsp;
## 4. Running the Application

After building the application, it can be run using the `run` command
and passing a number `n` to factorize.

```sh
mk run n=36
```

Output:

```
java application.Application n=36
Hello, Factorizer
n=36
 - factorized: [2, 2, 3, 3]
done.
```


&nbsp;
## 5. Completing the Application

Class
[Factorizer.java](https://gitlab.bht-berlin.de/sgraupner/setup.se2/-/blob/main/src/main/application/Factorizer.java/?ref_type=heads)
hardwires the output for *n=36*.

Implement the method:

```java
    /**
     * Factorize a number into smallest prime factors.
     * <p>
     * Examples:
     * <pre>
     * n=27 -> [3, 3, 3]
     * n=1092 -> [2, 2, 3, 7, 13]
     * n=10952347 -> [7, 23, 59, 1153]
     * </pre>
     * @param n number to factorize
     * @return list of factors
     */
    @Override
    public List<Integer> factorize(int n) {
      ...
    }
```

to work for other numbers as well. 

```sh
mk run n=36 n=9 n=109237
```

Output:

```
java application.Application n=36 n=9 n=109237
Hello, Factorizer
n=36
 - factorized: [2, 2, 3, 3]
n=9
 - factorized: [3, 3]
n=109237
 - factorized: [313, 349]
done.
```

Try larger numbers:

```sh
mk run n=59534165 n=59534166 n=59534167
```

Output:

```
Hello, Factorizer
n=59534165
 - factorized: [5, 109, 313, 349]
n=59534166
 - factorized: [2, 3, 23, 151, 2857]
n=59534167
 - factorized: [7, 7, 7, 11, 31, 509]
done.
```


&nbsp;
## 6. Testing the Application

Run JUnit-Tests in the IDE and with:

```sh
mk compile-tests run-tests
```

Output:

```
├─ JUnit Jupiter ✔
│  ├─ Application_0_always_pass_Tests ✔
│  │  ├─ test_001_always_pass() ✔
│  │  └─ test_002_always_pass() ✔
│  └─ Factorizer_Tests ✔
│     ├─ test0003_FactorizeExceptionCases() ✔
│     ├─ test0001_FactorizeRegularCases() ✔
│     └─ test0002_FactorizeCornerCases() ✔
├─ JUnit Vintage ✔
└─ JUnit Platform Suite ✔

Test run finished after 197 ms
[         5 containers found      ]
[         0 containers skipped    ]
[         5 containers started    ]
[         0 containers aborted    ]
[         5 containers successful ]
[         0 containers failed     ]
[         5 tests found           ]
[         0 tests skipped         ]
[         5 tests started         ]
[         0 tests aborted         ]
[         5 tests successful      ]   <-- 5 tests successful
[         0 tests failed          ]   <-- 0 tests failed
done.
```


&nbsp;
## 7. Generating Javadoc

Build the javadoc for the project. Customize your name as author in
[package-info.java](https://gitlab.bht-berlin.de/sgraupner/setup.se2/-/blob/main/src/main/application/package-info.java?ref_type=heads).

```sh
mk javadoc
```

Output:

```
javadoc -d docs $(eval echo $JDK_JAVADOC_OPTIONS)
Loading source files for package application...
Constructing Javadoc information...
Building index for all the packages and classes...
Standard Doclet version 21+35-LTS-2513
Building tree for all the packages and classes...
Generating docs\se1_play\application\Application.html...
Generating docs\se1_play\application\Factorizer.html...
Generating docs\se1_play\application\Factorizer_2.html...
Generating docs\se1_play\application\RunPriority.html...
Generating docs\se1_play\application\package-summary.html...
Generating docs\se1_play\application\package-tree.html...
Generating docs\se1_play\module-summary.html...
Generating docs\overview-tree.html...
Building index for all classes...
Generating docs\allclasses-index.html...
Generating docs\allpackages-index.html...
Generating docs\index-all.html...
Generating docs\search.html...
Generating docs\index.html...
Generating docs\help-doc.html...
done.
```

Open `docs/index.html` in a browser.


&nbsp;
## 8. Packaging the Application

*Packaging* is part of the *Build-Process* in which a `.jar` file (jar: Java archive)
is created that contains all compiled classes and a
[MANIFEST.MF](https://gitlab.bht-berlin.de/sgraupner/setup.se2/-/blob/main/src/resources/META-INF/MANIFEST.MF?ref_type=heads) - file
that describes the class to execute (Main-Class: application.Application).

```sh
mk jar
```

packages class files and creates the resulting `application-1.0.0-SNAPSHOT.jar`
in the `target` directory.

Test the jar-file with:

```sh
mk run-jar n=100 n=1000
```

Output:

```
java -jar target/application-1.0.0-SNAPSHOT.jar n=100 n=1000
Hello, Factorizer_2
n=100
 - factorized: [2, 2, 5, 5]
n=1000
 - factorized: [2, 2, 2, 5, 5, 5]
done.
```

The packaged .jar file can now be distributed.


&nbsp;
## 9. Checking into Git

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
