#include <jni.h>
#include "io_simonis_NativeCall.h"

extern "C" JNIEXPORT
void JNICALL Java_io_simonis_NativeCall_staticEmpty(JNIEnv*, jclass) {}

extern "C" JNIEXPORT
void JNICALL Java_io_simonis_NativeCall_jniCriticalEmpty(JNIEnv*, jclass) {}
extern "C" JNIEXPORT
void JNICALL JavaCritical_io_simonis_NativeCall_jniCriticalEmpty() {}

extern "C" JNIEXPORT
void JNICALL staticEmpty() {}

extern "C" JNIEXPORT
long JNICALL Java_io_simonis_NativeCall_staticArray(JNIEnv* env, jclass cls, jbyteArray b) {
  jbyte* jb = (jbyte*)env->GetPrimitiveArrayCritical(b, 0);
  jbyte ret = *jb;
  env->ReleasePrimitiveArrayCritical(b, jb, JNI_ABORT);
  return ret;
}

extern "C" JNIEXPORT
long JNICALL Java_io_simonis_NativeCall_jniCriticalArray(JNIEnv* env, jclass cls, jbyteArray b) {
  jbyte* jb = (jbyte*)env->GetPrimitiveArrayCritical(b, 0);
  jbyte ret = *jb;
  env->ReleasePrimitiveArrayCritical(b, jb, JNI_ABORT);
  return ret;
}
extern "C" JNIEXPORT
long JNICALL JavaCritical_io_simonis_NativeCall_jniCriticalArray(jint length, jbyte* b) {
  return *b;
}

extern "C" JNIEXPORT
jlong JNICALL staticArray(jbyte* b) {
  return *b;
}

extern "C" JNIEXPORT
long JNICALL Java_io_simonis_NativeCall_staticNativeMethodWithManyArgs(JNIEnv*, jclass, long, long, long, long, long, double) { return 42; }

extern "C" JNIEXPORT
void JNICALL Java_io_simonis_NativeCall_emptyNative(JNIEnv*, jobject) {}

extern "C" JNIEXPORT
long JNICALL Java_io_simonis_NativeCall_nativeMethod(JNIEnv*, jobject, long, double) { return 42; }

extern "C" JNIEXPORT
long JNICALL Java_io_simonis_NativeCall_nativeMethodWithManyArgs(JNIEnv*, jobject, long, long, long, long, long, double) { return 42; }

