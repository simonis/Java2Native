#include <stdio.h>
#include "jni.h"

int main(int argc, char** argv) {
  JavaVM *jvm;
  JNIEnv *env;
  JavaVMInitArgs vm_args;
  JavaVMOption* options = new JavaVMOption[0];
  vm_args.version = JNI_VERSION_1_8;
  vm_args.nOptions = 0;
  vm_args.options = options;
  int ret = JNI_CreateJavaVM(&jvm, (void**)&env, &vm_args);
  jclass cls = env->FindClass("io/simonis/NativeCall");
  jmethodID simple = env->GetStaticMethodID(cls, "simple", "()I");
  printf("%d\n", env->CallStaticIntMethod(cls, simple));
  jvm->DestroyJavaVM();
}
