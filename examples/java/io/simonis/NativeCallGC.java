package io.simonis;

public class NativeCallGC {

  public static native void block(int sec);
  public static native void elements(byte[] b, int sec);
  public static native void elementsCritical(byte[] b, int sec);

  static {
    // Load libNativeCall.so from LD_LIBRARY_PATH / -Djava.library.path
    System.loadLibrary("NativeCallGC");
  }

  public static void main(String[] args) throws Exception {
    new Thread() {
      public void run() {
        while(true) {
          try { Thread.sleep(1_000); } catch (InterruptedException e) {}
          System.gc();
        }
      }
    }.start();
    int sec = Integer.parseInt(args[1]);
    byte[] b = new byte[42];
    switch (args[0]) {
      case "block" : block(sec); break;
      case "elements" : elements(b, sec); break;
      case "elementsCritical" : elementsCritical(b, sec); break;
    }
    System.exit(0);
  }
}
