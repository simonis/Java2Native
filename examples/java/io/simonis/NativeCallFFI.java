package io.simonis;

import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.Linker;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SymbolLookup;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;

public class NativeCallFFI {

  public static void main(String[] args) throws Throwable {
    // Provides 'void sayHello(char*)' and 'char* getLine()'
    System.loadLibrary("NativeCallJNA");

    MemorySegment addr = SymbolLookup.loaderLookup()
      .find("sayHello").orElseThrow();
    FunctionDescriptor fd = FunctionDescriptor.ofVoid(ValueLayout.ADDRESS);
    MethodHandle sayHello = Linker.nativeLinker()
      .downcallHandle(addr, fd/*, Linker.Option.critical(false)*/);
    try (Arena arena = Arena.ofConfined()) {
      // 'arena.allocateFrom()' corresponds to 'env->GetStringUTFChars()'
      sayHello.invokeExact(arena.allocateFrom("JavaLand"));
    }

    addr = SymbolLookup.loaderLookup()
      .find("getLine").orElseThrow();
    fd = FunctionDescriptor.of(ValueLayout.ADDRESS);
    MethodHandle getLine = Linker.nativeLinker()
      .downcallHandle(addr, fd, args.length > 0 ? new Linker.Option[]{Linker.Option.critical(false)} : new Linker.Option[]{});
    String s = ((MemorySegment) getLine.invokeExact())
      // 'MemorySegment.getString()' corresponds to 'env->NewStringUTF()'
      .reinterpret(16 /* unsafe !!! */).getString(0);
    System.out.println(s);
  }
}
