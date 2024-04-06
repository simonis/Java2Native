#include <jni.h>
#include "io_simonis_NativeCall.h"

extern "C" JNIEXPORT
void JNICALL Java_io_simonis_NativeCall_emptyStaticNativeMethod(JNIEnv*, jclass) {}

extern "C" JNIEXPORT
void JNICALL Java_io_simonis_NativeCall_emptyStaticNativeCriticalMethod(JNIEnv*, jclass) {}
extern "C" JNIEXPORT
void JNICALL JavaCritical_io_simonis_NativeCall_emptyStaticNativeCriticalMethod() {}


extern "C" JNIEXPORT
long JNICALL Java_io_simonis_NativeCall_staticNativeMethod(JNIEnv* env, jclass cls, jbyteArray b) {
  jbyte* jb = (jbyte*)env->GetPrimitiveArrayCritical(b, 0);
  jbyte ret = *jb;
  env->ReleasePrimitiveArrayCritical(b, jb, JNI_ABORT);
  return ret;
}
extern "C" JNIEXPORT
long JNICALL Java_io_simonis_NativeCall_staticNativeCriticalMethod(JNIEnv* env, jclass cls, jbyteArray b) {
  jbyte* jb = (jbyte*)env->GetPrimitiveArrayCritical(b, 0);
  jbyte ret = *jb;
  env->ReleasePrimitiveArrayCritical(b, jb, JNI_ABORT);
  return ret;
}
extern "C" JNIEXPORT
long JNICALL JavaCritical_io_simonis_NativeCall_staticNativeCriticalMethod(jint length, jbyte* b) {
  return *b;
}

extern "C" JNIEXPORT
long JNICALL Java_io_simonis_NativeCall_staticNativeMethodWithManyArgs(JNIEnv*, jclass, long, long, long, long, long, double) { return 42; }

extern "C" JNIEXPORT
void JNICALL Java_io_simonis_NativeCall_emptyNativeMethod(JNIEnv*, jobject) {}

extern "C" JNIEXPORT
long JNICALL Java_io_simonis_NativeCall_nativeMethod(JNIEnv*, jobject, long, double) { return 42; }

extern "C" JNIEXPORT
long JNICALL Java_io_simonis_NativeCall_nativeMethodWithManyArgs(JNIEnv*, jobject, long, long, long, long, long, double) { return 42; }

