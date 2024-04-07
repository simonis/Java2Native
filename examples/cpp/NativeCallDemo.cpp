#include <jni.h>

void sayHello(JNIEnv* env, jclass cls, jstring s) {
  static int counter = 0;
  if (counter++ % 10000 == 0) {
    printf("Hello %s\n", env->GetStringUTFChars(s, 0));
  }
}

jint JNI_OnLoad(JavaVM *vm, void *reserved) {
  JNIEnv* env;
  vm->GetEnv((void**)&env, JNI_VERSION_1_8);
  jclass cls = env->FindClass("io/simonis/NativeCallDemo");
  static JNINativeMethod methods[] = {
    {"sayHello", "(Ljava/lang/String;)V", (void*)&sayHello},
  };
  env->RegisterNatives(cls, methods, 1);
  printf("sayHello() address = %p\n", &sayHello);
  return JNI_VERSION_1_8;
}
