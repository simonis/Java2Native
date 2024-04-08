package io.simonis;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;

public class NativeCallJNA {

  public interface CLibrary extends Library {
    void sayHello(String s);
    String getLine();
  }

  public static native void sayHello(String s);
  public static native String getLine();
  static {
    Native.register("NativeCallJNA");
  }

  public static void main(String[] args) {
    CLibrary c = Native.load("NativeCallJNA", CLibrary.class);
    c.sayHello("JavaLand");
    System.out.println(c.getLine());
    sayHello("JavaLand DIRECT");
    System.out.println(getLine());
  }
}
