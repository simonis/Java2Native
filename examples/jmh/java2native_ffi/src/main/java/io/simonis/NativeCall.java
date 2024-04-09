package io.simonis;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.CompilerControl;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.Linker;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SymbolLookup;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;

@State(Scope.Benchmark)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(value = 1,
      jvmArgsPrepend = { //"-Djava.library.path=target/native",
                 "-XX:+UnlockExperimentalVMOptions",
                 "-XX:+UseSerialGC",
                 "-XX:+AlwaysPreTouch",
                 "-Xms512m", "-Xmx512m"})
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class NativeCall {

  byte[] array;
  MethodHandle ffiEmptyHandle, ffiEmptyCriticalHandle;
  MethodHandle ffiArrayHandle, ffiArrayCriticalHandle, ffiArrayCriticalHeapAccessHandle;
  JnaInterface jnaInterface;

  @Param({"1"})
  int length;

  @Setup
  public void setup() {
    array = new byte[length];
    System.loadLibrary("NativeCall");
    MemorySegment addr = SymbolLookup.loaderLookup().find("staticEmpty").orElseThrow();
    FunctionDescriptor fd = FunctionDescriptor.ofVoid();
    ffiEmptyHandle = Linker.nativeLinker().downcallHandle(addr, fd);
    ffiEmptyCriticalHandle = Linker.nativeLinker().downcallHandle(addr, fd, Linker.Option.critical(false));
    addr = SymbolLookup.loaderLookup().find("staticArray").orElseThrow();
    fd = FunctionDescriptor.of(ValueLayout.JAVA_LONG, ValueLayout.ADDRESS, ValueLayout.JAVA_INT);
    ffiArrayHandle = Linker.nativeLinker().downcallHandle(addr, fd);
    ffiArrayCriticalHandle = Linker.nativeLinker().downcallHandle(addr, fd, Linker.Option.critical(false));
    ffiArrayCriticalHeapAccessHandle = Linker.nativeLinker().downcallHandle(addr, fd, Linker.Option.critical(true));

    jnaInterface = Native.load("NativeCall", JnaInterface.class);
  }

  @CompilerControl(CompilerControl.Mode.DONT_INLINE)
  public static void emptyStaticMethod() {
  }
  @CompilerControl(CompilerControl.Mode.DONT_INLINE)
  public static long staticMethod(long l, double d) {
    return 42;
  }
  @CompilerControl(CompilerControl.Mode.DONT_INLINE)
  public static long staticMethodWithManyArgsHelper(long l1, long l2, long l3, long l4, long l5, double d) {
    return staticMethodWithManyArgs(l1, l2, l3, l4, l5, d);
  }
  @CompilerControl(CompilerControl.Mode.DONT_INLINE)
  public static long staticMethodWithManyArgs(long l1, long l2, long l3, long l4, long l5, double d) {
    return 42;
  }
  @CompilerControl(CompilerControl.Mode.DONT_INLINE)
  public void empty() {
  }
  @CompilerControl(CompilerControl.Mode.DONT_INLINE)
  public long method(long l, double d) {
    return 42;
  }
  @CompilerControl(CompilerControl.Mode.DONT_INLINE)
  public long methodWithManyArgsHelper(long l1, long l2, long l3, long l4, long l5, double d) {
    return methodWithManyArgs(l1, l2, l3, l4, l5, d);
  }
  @CompilerControl(CompilerControl.Mode.DONT_INLINE)
  public long methodWithManyArgs(long l1, long l2, long l3, long l4, long l5, double d) {
    return 42;
  }

  public static native void staticEmpty();

  public static native long staticArray(byte[] b);

  static class JNA {
    public static native void staticEmpty();
    public static native long staticArray(byte[] b);
  }
  public interface JnaInterface extends Library {
    void staticEmpty();
    long staticArray(byte[] b);
  }
  static {
    Native.register(NativeCall.JNA.class, "NativeCall");
  }

  @Benchmark
  public static void baseline() {
  }

  @Benchmark
  public static void staticMethodCallingStatic() {
    emptyStaticMethod();
  }

  @Benchmark
  public static void staticMethodCallingStaticWithArgs() {
    staticMethod(42l, 42.0d);
  }

  @Benchmark
  public static void staticMethodCallingStaticWithManyArgs() {
    staticMethodWithManyArgsHelper(42l, 42l, 42l, 42l, 42l, 42.0d);
  }

  @Benchmark
  public void callingStaticEmpty() {
    staticEmpty();
  }

  @Benchmark
  public void callingJnaDirectEmpty() {
    JNA.staticEmpty();
  }

  @Benchmark
  public void callingJnaEmpty() {
    jnaInterface.staticEmpty();
  }

  @Benchmark
  public void callingFfiEmpty() throws Throwable {
    ffiEmptyHandle.invokeExact();
  }

  @Benchmark
  public void callingFfiCriticalEmpty() throws Throwable {
    ffiEmptyCriticalHandle.invokeExact();
  }

  @Benchmark
  public void callingStaticArray() {
    staticArray(array);
  }

  @Benchmark
  public void callingJnaDirectArray() {
    JNA.staticArray(array);
  }

  @Benchmark
  public void callingJnaArray() {
    jnaInterface.staticArray(array);
  }

  @Benchmark
  public void callingFfiArray() throws Throwable {
    try (Arena arena = Arena.ofConfined()) {
      long l = (long)ffiArrayHandle.invokeExact(arena.allocateFrom(ValueLayout.JAVA_BYTE, array), array.length);
    }
  }

  @Benchmark
  public void callingFfiCriticalArray() throws Throwable {
    try (Arena arena = Arena.ofConfined()) {
      long l = (long)ffiArrayCriticalHandle.invokeExact(arena.allocateFrom(ValueLayout.JAVA_BYTE, array), array.length);
    }
  }

  @Benchmark
  public void callingFfiCriticalHeapAccessArray() throws Throwable {
    long l = (long)ffiArrayCriticalHeapAccessHandle.invokeExact(MemorySegment.ofArray(array), array.length);
  }

  @Benchmark
  public void callingEmpty() {
    empty();
  }

  @Benchmark
  public void methodCallingWithArgs() {
    method(42l, 42.0d);
  }

  @Benchmark
  public void methodCallingWithManyArgs() {
    methodWithManyArgsHelper(42l, 42l, 42l, 42l, 42l, 42.0d);
  }

  @Benchmark
  @Fork(jvmArgsAppend = "-XX:-TieredCompilation")
  public static void staticMethodCallingStaticNativeNoTiered() {
    staticEmpty();
  }

  @Benchmark
  @Fork(jvmArgsAppend = "-XX:+PreferInterpreterNativeStubs")
  public static void staticMethodCallingStaticNativeIntStub() {
    staticEmpty();
  }

  public static void main(String[] args) {
    NativeCall nc = new NativeCall();
    for (int i = 0; i < 100_000; i++) {
      nc.callingStaticEmpty();
    }
  }
}
