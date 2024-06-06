// Example.java
public class Example {

    static {
        // in macos, g++ -shared -fPIC -I"${JAVA_HOME}/include" -I"${JAVA_HOME}/include/linux" example.cpp -o libexample.dylib
        // in linux, g++ -shared -fPIC -I"${JAVA_HOME}/include" -I"${JAVA_HOME}/include/linux" example.cpp -o libexample.so
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
