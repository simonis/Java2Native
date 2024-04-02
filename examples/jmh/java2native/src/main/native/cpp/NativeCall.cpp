#include <jni.h>
#include "io_simonis_NativeCall.h"

extern "C" JNIEXPORT
void JNICALL Java_io_simonis_NativeCall_emptyStaticNativeMethod(JNIEnv*, jclass) {}

extern "C" JNIEXPORT
long JNICALL Java_io_simonis_NativeCall_staticNativeMethod(JNIEnv*, jclass, long, double) { return 42; }

extern "C" JNIEXPORT
long JNICALL Java_io_simonis_NativeCall_staticNativeMethodWithManyArgs(JNIEnv*, jclass, long, long, long, long, long, double) { return 42; }

extern "C" JNIEXPORT
void JNICALL Java_io_simonis_NativeCall_emptyNativeMethod(JNIEnv*, jobject) {}

extern "C" JNIEXPORT
long JNICALL Java_io_simonis_NativeCall_nativeMethod(JNIEnv*, jobject, long, double) { return 42; }

extern "C" JNIEXPORT
long JNICALL Java_io_simonis_NativeCall_nativeMethodWithManyArgs(JNIEnv*, jobject, long, long, long, long, long, double) { return 42; }

