public class NativeCall {

  public native void nMethod();
  public native int nativeMethod(int i, String s, byte[] b);
  public static native int nativeMethod(double d, Object o);

  static {
    // Load libNativeCall.so from LD_LIBRARY_PATH / -Djava.library.path
    System.loadLibrary("NativeCall");
  }
}
