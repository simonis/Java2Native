#include <jni.h>
#include <unistd.h>

void block(JNIEnv* env, jclass cls, jint s) {
  sleep(s);
}

void elements(JNIEnv* env, jclass cls, jbyteArray b, jint s) {
  jboolean copy;
  jbyte* jb = (jbyte*)env->GetByteArrayElements(b, &copy);
  printf("GetByteArrayElements() returned %s\n", copy ? "copy":"original");
  sleep(s);
  env->ReleaseByteArrayElements(b, jb, copy ? JNI_ABORT : 0);
}

void elementsCritical(JNIEnv* env, jclass cls, jbyteArray b, jint s) {
  jboolean copy;
  jbyte* jb = (jbyte*)env->GetPrimitiveArrayCritical(b, &copy);
  printf("GetPrimitiveArrayCritical() returned %s\n", copy ? "copy":"original");
  sleep(s);
  env->ReleasePrimitiveArrayCritical(b, jb, copy ? JNI_ABORT : 0);
}

void elementsCriticalJNI(JNIEnv* env, jclass cls, jbyteArray b, jint s) {
  static jboolean print = true;
  if (print) {
    print = false;
    printf("Executing elementsCriticalJNI()\n");
  }
}

extern "C" JNIEXPORT
void JavaCritical_io_simonis_NativeCallGC_elementsCriticalJNI(jint length, jbyte* b, jint s) {
  static jboolean print = true;
  if (print) {
    print = false;
    printf("Executing JavaCritical_elementsCriticalJNI()\n");
  }
  sleep(s);
}

jint JNI_OnLoad(JavaVM *vm, void *reserved) {
  JNIEnv* env;
  vm->GetEnv((void**)&env, JNI_VERSION_1_8);
  jclass cls = env->FindClass("io/simonis/NativeCallGC");
  static JNINativeMethod methods[] = {
    {"block", "(I)V", (void*)&block},
    {"elements", "([BI)V", (void*)&elements},
    {"elementsCritical", "([BI)V", (void*)&elementsCritical},
    {"elementsCriticalJNI", "([BI)V", (void*)&elementsCriticalJNI},
  };
  env->RegisterNatives(cls, methods, 4);
  return JNI_VERSION_1_8;
}
