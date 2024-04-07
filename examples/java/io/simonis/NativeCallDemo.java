package io.simonis;

public class NativeCallDemo {

  public static native void sayHello(String s);

  static {
    System.loadLibrary("NativeCallDemo");
  }

  public static void main(String[] args) throws Exception {
    for (int i = 0; i < 15_000; i++) {
      sayHello("JavaLand");
    }
  }
}
