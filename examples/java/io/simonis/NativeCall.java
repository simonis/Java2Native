package io.simonis;

public class NativeCall {

  String msg = "Hello ";
  public static native int simple();
  public native void hello(String s);

  static {
    // Load libNativeCall.so from LD_LIBRARY_PATH / -Djava.library.path
    System.loadLibrary("NativeCall");
  }

  public static void main(String[] args) {
    System.out.println(simple());
    new NativeCall().hello("world");
  }
}
