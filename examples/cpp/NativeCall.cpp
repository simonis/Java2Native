#include <jni.h>
#include "io_simonis_NativeCall.h"

extern "C" JNIEXPORT
jint JNICALL Java_io_simonis_NativeCall_simple(JNIEnv* env, jclass cls) {
  return 42;
}

extern "C" JNIEXPORT
void JNICALL Java_io_simonis_NativeCall_hello(JNIEnv* env, jobject self, jstring s) {
  jclass myClass = env->GetObjectClass(self);
  jfieldID msgID = env->GetFieldID(myClass, "msg", "Ljava/lang/String;");
  jobject msg = env->GetObjectField(self, msgID);
  jclass system = env->FindClass("Ljava/lang/System;");
  jfieldID outID = env->GetStaticFieldID(system, "out", "Ljava/io/PrintStream;");
  jobject out = env->GetStaticObjectField(system, outID);
  jclass printStream = env->FindClass("Ljava/io/PrintStream;");
  jmethodID print = env->GetMethodID(printStream, "print", "(Ljava/lang/String;)V");
  jmethodID println = env->GetMethodID(printStream, "println", "()V");
  env->CallVoidMethod(out, print, msg);
  env->CallVoidMethod(out, print, s);
  env->CallVoidMethod(out, println);
}
