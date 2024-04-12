# * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
# Setup script to build the project environment with environment variables:
#  - CLASSPATH, MODULEPATH      ; used by Java compiler and JVM
#  - JDK_JAVAC_OPTIONS          ; used by the Java compiler
#  - JDK_JAVADOC_OPTIONS        ; used by the Javadoc compiler
#  - JUNIT_OPTIONS              ; used by the JUnit test runner
# \\
# and generated project files:
#  - .classpath, .project       ; files in project directory used by eclipse
#                               ; IDE and VSCode Java extension
# 
# Script defines executable functions and aliases:
#  - cmd [cmd] [--single-line]  ; output full command for cmd argument in multi-
#                               ; or single-line format (with traling '\')
#  - show [-all] [--single-line] [cmd1, cmd2...] [args]  ; show command sequence
#  - build | make | mk [--show] [--silent|-s] [cmd1, cmd2...] [args]
#                                   ; execute cmd sequence, if not --show
#  - clean                      ; remove artefacts produced by commands
#  - wipe                       ; remove the entire project environment
#  - source .env/project.sh     ; build / re-build the project environment
# * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *

# command aliases
[ -z "$(alias mk 2>/dev/null)" ] && aliases_present=false || aliases_present=true
alias mk="make"
alias build="make"
alias wipe="make wipe --silent"
alias clean="make clean"

# map with project paths and files
declare -gA P=(
    [src]="src/main"
    [tests]="src/tests"
    [res]="src/resources"
    [lib]="libs"
    [target]="target"
    [log]="logs"
    [doc]="docs"
    [env]=".env"
    [url]="https://github.com/sgra64/se1.play"
    [final_jar]="target/application-1.0.0-SNAPSHOT.jar"
)

# list of short commands
cmd_shorts=("source" "project" "classpath" "compile" "compile-tests" "resources"
        "jar" "pack" "package" "pack-libs" "run" "run-jar" "run-tests" "javadoc"
        "clean" "wipe")

# * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
# Main function to setup project environment (transient function).
# Usage: setup
function setup() {
    [ ! -d src ] && \
        echo "must source in project directory" && \
        return

    [ ! -f ${P[env]}/init.classpath ] && \
        echo "need to get all ${P[env]} files from repository: ${P[url]}" \
        return

    # define local variables and fill with values
    local module_dirs=( $(find ${P[lib]}/* -type d 2>/dev/null) )
    local module_jars=( $(find ${P[lib]}/*/ -name '*.jar' 2>/dev/null) )
    local entries=(
        "${P[target]}/classes"
        "${P[target]}/test-classes"
        "${P[target]}/resources"
        ${module_jars[@]}
    )

    # CLASSPATH seperator: Unix/Linux/Mac use ":", Windows uses ";" 
    [ "$(uname | grep 'CYGWIN\|MINGW')" ] && local sep=';' || local sep=':'

    local created=()    # collect environment variables set
    # build CLASSPATH environment variable, if not exists
    if [ -z "$CLASSPATH" ]; then
        export CLASSPATH=""
        for entry in ${entries[@]}; do
            [ ! -z "$CLASSPATH" ] && CLASSPATH+=${sep}
            CLASSPATH+=$entry
        done
        created+=("CLASSPATH")
    fi

    # build MODULEPATH environment variable, if not exists
    if [ -z "$MODULEPATH" ]; then
        export MODULEPATH=""
        for entry in ${module_dirs[@]}; do
            [ ! -z "$MODULEPATH" ] && MODULEPATH+=${sep}
            MODULEPATH+=$entry
        done
        created+=("MODULEPATH")
    fi

    local mp_opt=""
    [ "$MODULEPATH" ] && mp_opt=" --module-path \"$MODULEPATH\""

    # set JDK_JAVAC_OPTIONS used by the javac compiler
    if [ -z "$JDK_JAVAC_OPTIONS" ]; then
        export JDK_JAVAC_OPTIONS="-d ${P[target]}/classes$mp_opt"
        created+=("JDK_JAVAC_OPTIONS")
    fi

    # set JDK_JAVADOC_OPTIONS used by the javadoc compiler
    if [ -z "$JDK_JAVADOC_OPTIONS" ]; then
        local jdoc_opts=(
            "--source-path ${P[src]} -d ${P[doc]}$mp_opt "
            "-version -author -noqualifier \"java.*:application.*\""
        )
        # append packages from src/main later in javadoc command: + "application"
        export JDK_JAVADOC_OPTIONS=${jdoc_opts[@]}
        created+=("JDK_JAVADOC_OPTIONS")
    fi

    # set JUNIT_OPTIONS used by JUnit test runner
    [ -z "$JUNIT_OPTIONS" ] && export JUNIT_OPTIONS=$( \
            [[ $(uname -s) =~ ^MINGW.* ]] && echo -n "--details-theme=ascii " || \
                echo -n "--details-theme=unicode "; \
            [[ "$COLOR" == "off" ]] && echo -n "--disable-ansi-colors "; \
            echo -n "-cp ${P[target]}/classes -cp ${P[target]}/test-classes"; \
        ) \
        && created+=("JUNIT_OPTIONS")

    # report created environment variables
    [ "${created}" ] && echo "setting the project environment" && \
        echo " - environment variables:" && \
        for var in "${created[@]}"; do
            echo "    - $var"
        done

    created=()    # collect created files
    # create .env/.classpath file for VSCode code-runner (settings.json)
    [ ! -f .env/.classpath ] \
        && echo "-cp $CLASSPATH" > .env/.classpath \
        && created+=(".env/.classpath")

    # create .classpath file used by VSCode/Java and eclipse IDE
    [ ! -f .classpath -a -f ${P[env]}/init.classpath ] \
        && eclipse_classpath "${module_jars[@]}" > .classpath \
        && created+=(".classpath")

    # create .project file used by VSCode/Java and eclipse
    [ ! -f .project -a -f ${P[env]}/init.project ] \
        && cp ${P[env]}/init.project .project \
        && created+=(".project")

    # create .gitignore file used by git
    [ ! -f .gitignore -a -f ${P[env]}/init.gitignore ] \
        && cp ${P[env]}/init.gitignore .gitignore \
        && created+=(".gitignore")

    # report created files, if any
    [ "${created}" ] && echo " - files created:" && \
        for file in "${created[@]}"; do
            echo "    - $file"
        done

    # report created aliases and functions
    [ "$aliases_present" = "false" ] && echo " - functions and aliases created:" && \
        echo "    - aliases: mk, build, wipe, clean" && \
        echo "    - functions: make, show, cmd, copy" && echo "||"

    echo "project environment is set (use 'wipe' to reset)"
}

# * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
# Output command for short argument multi- or single-line (no/with traling '\').
# Usage:
#   cmd [cmd] [--single-line]
# @param cmd command short, e.g. 'compile', 'compile-tests', 'run'...
# @param --single-line output in single-line format with trailing '\'
# @output full command for short cmd to stdout
function cmd() {
    local cmd=()
    case "$1" in
    source|project) cmd=("source ${P[env]}/project.sh") ;;
    classpath)  cmd=("echo \$CLASSPATH | tr \"[;:]\" \"\\\n\"") ;;
    compile)    cmd=("javac \$(find ${P[src]} -name '*.java') -d ${P[target]}/classes; \\"
                "$(cmd resources $2)"
                ) ;;
    compile-tests) cmd=("javac \$(find ${P[tests]} -name '*.java') -d ${P[target]}/test-classes; \\"
                "$(cmd resources $2)"
                ) ;;
    resources)  cmd=("copy ${P[res]} ${P[target]}/resources")
                ;;
    jar|pack|package) cmd=("jar -c -v -f ${P[final_jar]} \\"
                "    -m ${P[res]}/META-INF/MANIFEST.MF \\"
                "    -C ${P[target]}/classes . ; \\"
                "jar uvf ${P[final_jar]} -C ${P[target]} resources;")
                ;;
    pack-libs) cmd=("[[ ! -d ${P[target]}/unpacked ]] && mkdir -p ${P[target]}/unpacked && \\"
                "  echo inflating libs to ${P[target]}/unpacked && \\"
                "  (cd ${P[target]}/unpacked; find ../../libs/*/ -type f | xargs -I % jar xf %); \\"
                "jar uvf ${P[final_jar]} -C ${P[target]}/unpacked org")
                ;;
    run)        cmd=("java application.Application") ;;
    run-jar)    cmd=("java -jar ${P[final_jar]}") ;;
    run-tests)  cmd=("java -jar ${P[lib]}/junit-platform-console-standalone-1.9.2.jar \\"
                "  \$(eval echo \$JUNIT_OPTIONS)" "--scan-class-path")
                ;;
    javadoc)    cmd=("javadoc -d ${P[doc]} \$(eval echo \$JDK_JAVADOC_OPTIONS) \\"
                "  \$(cd ${P[src]}; find . -type f | xargs dirname | uniq | cut -c 3-)")
                ;;

    clean)  cmd=("rm -rf ${P[target]} ${P[log]} ${P[doc]}")
            ;;
    wipe)   # reset project settings and clear the project directory of generated files
            local wipe_files=( .env/.classpath .classpath .project .gitignore )
            cmd=("rm -rf ${wipe_files[@]} ; \\"
                 "$(cmd clean $2); \\"
                 "unset P cmd_shorts CLASSPATH MODULEPATH JDK_JAVAC_OPTIONS; \\"
                 "unset JDK_JAVADOC_OPTIONS JUNIT_OPTIONS aliases_present; \\"
                 "unset -f cmd show make copy; \\"
                 "unalias mk build wipe clean"
            ) ;;
    esac
    case "$2" in
    --single-line)  # output in copy&paste form with trailng '\'
            shift; shift; [[ "${cmd[@]}" ]] && echo -en "${cmd[@]} $@" | sed -e 's/\\ /\\\n/g' ;;
    *)      shift; echo -en "${cmd[@]} $@" | sed -e 's/\\ /\n/g' ;;
    esac
    [[ "${cmd[@]}" ]] && echo
}

# * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
# Show full commands for sequence of command shorts (e.g. 'run', 'run-tests').
# Usage:
#   show [--all] [--single-line] [cmd1] [cmd2] [cmd3] [args] ...
# @param --all show all commands (reduced list without)
# @param --single-line output with trailing '\'
# @param cmd command short, e.g. 'compile', 'compile-tests', 'run'...
# @param args arguments passed to last cmd
# @output command list to stdout
function show() {
    local show_all=false
    for arg in "$@"; do
        [[ "$arg" = "--all" ]] && show_all=true && shift && break
    done
    if [[ -z "$@" ]]; then
        local h1=""; local h2="";
        [[ "$COLOR" = "on" ]] && h1="\e[1;37m" && h2="\e[m"
        for short in ${cmd_shorts[@]}; do
            # skip short commands not shown without '--all' option
            [[ "$show_all" = "false" ]] && case "$short" in \
                package|pack-libs|run-jar) continue;
                esac
            # list with highlighted short and indented full command
            case "$short" in
            project|jar|pack) ;;    # skip command aliases (ignore)
            wipe)    echo -e "${h1}wipe:${h2}"; echo ;;
            source)  echo -e "${h1}source | project:${h2}"; echo "  $(cmd $short)"; echo ;;
            package) echo -e "${h1}jar | pack | package:${h2}"; cmd $short --single-line | sed -e 's/^/  /'; echo ;;
            *)       echo -e "${h1}$short:${h2}";               cmd $short --single-line | sed -e 's/^/  /'; echo ;;
            esac
        done
    else
        # show sequence of command shorts passed as args
        make --show $@
    fi
}

# * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
# Execute sequence of command shorts (e.g. 'compile', 'run', 'run-tests'...).
# Usage:
#   make [--show] [-s|--silent] [cmd1] [cmd2] [cmd3] [args] ...
# @param --show show full command, but do not execute
# @param -s|--silent execute command, but don't show (default)
# @param cmd command short, e.g. 'compile', 'compile-tests', 'run', ...
# @param args arguments passed to last cmd
function make() {
    local exec=true
    local silent=false
    local singleline=""
    local arg_cmd_shorts=() # short commands passed as arguments
    local args=()           # other args (none-cmd_shorts)
    for arg in "$@"; do
        local len=${#arg_cmd_shorts[@]}
        for cmd in ${cmd_shorts[@]}; do     # match arg in cmd_shorts[@]
            [[ "$cmd" == "$arg" ]] && arg_cmd_shorts+=($arg) && break
        done
        if [[ ${#arg_cmd_shorts[@]} == $len ]]; then
            case "$arg" in
            --show) exec=false ;;
            -s|--silent) silent=true ;;
            --single-line) singleline="--single-line" ;;
            *) args+=($arg) ;;      # collect other args as last-cmd args
            esac
        fi
    done
    if [[ ! -z "${arg_cmd_shorts[@]}" ]]; then
        for short in "${arg_cmd_shorts[@]}"; do
            [ ! -d src -a "$exec" = "true" ] && \
                echo -e "must execute \047$short\047 in project directory" && \
                return
            # 
            local exec_cmd=""
            local pass_args=""      # pass args to last cmd
            [[ "${arg_cmd_shorts[-1]}" = "$short" ]] && pass_args="${args[@]}"
            # 
            [[ "$silent" = "false" ]] && cmd $short "--single-line" $pass_args
            [[ "$exec" = "true" ]] && eval $(cmd $short $pass_args) && echo "done."
        done
    else
        show --all
    fi
}

# * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
# Copy content of (resource) directory passed as first argument to directory
# passed as second argumet, except 'META-INF' directory.
# Usage:
#   copy [from_dir] [to_dir]
# @param from_dir source directory from which content is recursively copied
# @param to_dir destination directory
function copy() {
    local from="$1"
    local to="$2"
    if [ -z "$from" ] || [ -z "$to" ]; then
        echo "use: copy <from-dir> <to-dir>"
    else
        [[ ! -d "$to" ]] && mkdir -p $to
        # find $from ! -path '*META-INF*' -type f | xargs cp --parent -t $to
        tar -cpf - -C $from --exclude='META-INF' . | tar xpf - -C $to
    fi
}

# * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
# Output eclipse and VSCode(Java) IDE configurations to generate .classpath
# file in the project directory, needed by IDE to understand the project
# structure (transient function).
# Usage:
#   eclipse_classpath [module_jars[@]]
# @param module_jars[@] module_jars array
# @output content of configured .classfile to stdout
function eclipse_classpath() {
    local jars=("$@")
    if [ "${#jars[@]}" -gt "0" ]; then
        # remove closing tag </classpath>
        egrep -v '</classpath>' < ${P[env]}/init.classpath
        # insert <classpathentry> ... </classpathentry>
        for jar in ${jars[@]}; do
            echo '    <classpathentry kind="lib" path="'${jar}'">'
            echo '        <attributes>'
            echo '            <attribute name="module" value="true"/>'
            echo '        </attributes>'
            echo '    </classpathentry>'
        done
        # re-append closing tag </classpath>
        echo '</classpath>'
    else
        cat ${P[env]}/init.classpath
    fi
}

# execute setup function
setup

# remove transient varibales and functions from process
unset -f setup eclipse_classpath
