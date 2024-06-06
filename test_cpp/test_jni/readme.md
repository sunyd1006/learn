创建Java和C++之间的互相调用涉及到JNI（Java Native Interface）的使用。这里将展示一个简单的例子，其中Java调用一个C++的函数，然后C++再回调一个Java的方法。

### 步骤1: 创建Java类

首先，我们创建一个Java类，它声明了一个native方法和一个回调方法。

```
// Example.java
public class Example {

    static {
        System.loadLibrary("example"); // 加载本地库
    }

    public static void main(String[] args) {
        new Example().nativeMethod(); // 调用本地方法
    }

    private native void nativeMethod(); // 声明本地方法

    public void callback() {
        System.out.println("This is a message from Java callback!"); // 被C++调用的方法
    }
}

```

### 步骤2: 通过javac编译Java代码

在命令行运行以下命令来编译Java代码，并生成头文件（确保你已经将JDK的 `bin`目录添加到了PATH环境变量）：

```
javac Example.java
javah -jni Example

```

这将生成一个头文件 `Example.h`。

### 步骤3: 实现C++代码

然后，我们创建一个C++源文件来实现这些方法。

```
// example.cpp
#include <jni.h>
#include <iostream>
#include "Example.h"

using namespace std;

JNIEXPORT void JNICALL Java_Example_nativeMethod(JNIEnv *env, jobject obj) {
    cout << "This is a message from C++!" << endl;

    // 获取对象的类
    jclass cls = env->GetObjectClass(obj);

    // 查找回调方法的ID
    jmethodID mid = env->GetMethodID(cls, "callback", "()V");
    if(mid == nullptr) return; // 方法未找到

    // 调用Java回调方法
    env->CallVoidMethod(obj, mid);
}

```

### 步骤4: 编译C++代码并生成共享库

编译C++代码并生成JNI需要的共享库。这个步骤和你所使用的系统相关。

在Macos上，使用g++可以这样做：

```
g++ -shared -fPIC -I"${JAVA_HOME}/include" -I"${JAVA_HOME}/include/linux" example.cpp -o libexample.dylib
```

在Linux上，使用g++可以这样做：

```
g++ -shared -fPIC -I"${JAVA_HOME}/include" -I"${JAVA_HOME}/include/linux" example.cpp -o libexample.so
```

### 步骤5: 运行Java程序

将生成的共享库（`libexample.so`或 `example.dll`）放在Java可识别的路径下，例如在当前目录或者通过 `-Djava.library.path`指定的目录中。在命令行执行Java程序：

```
java -Djava.library.path =. Example
```

如果一切顺利，你会看到C++函数打印的消息，然后是Java回调方法打印的消息。

请注意，对于生产用途的程序，你会需要更细致地处理JNI中的错误和异常。 JNI是一个强大但复杂的接口，使用不当可能导致程序稳定性问题。以上只是一个简单的示例，用于基础了解如何在Java和C++之间进行互相调用。
