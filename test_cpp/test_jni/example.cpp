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
