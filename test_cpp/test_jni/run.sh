


dir=`cd "$(dirname "$0")" && pwd`
cd $dir
echo "working dir: ${dir}"

# step1: create java class and generate java Exmpale.h
javac Example.java
javah -jni Example

# step2: write cpp classs and implements Java_Example_nativeMethod
g++ -shared -fPIC -I"${JAVA_HOME}/include" -I"${JAVA_HOME}/include/linux" example.cpp -o libexample.so
g++ -shared -fPIC -I"${JAVA_HOME}/include" -I"${JAVA_HOME}/include/linux" example.cpp -o libexample.dylib

# step3: run java code
java -Djava.library.path=. Example



