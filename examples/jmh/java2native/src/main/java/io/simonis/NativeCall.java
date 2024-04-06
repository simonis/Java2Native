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

@State(Scope.Benchmark)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(value = 1,
      jvmArgsPrepend = { //"-Djava.library.path=target/native",
                 "-XX:+UnlockExperimentalVMOptions",
                 "-XX:+UseEpsilonGC",
                 "-XX:+AlwaysPreTouch",
                 "-Xms512m", "-Xmx512m"})
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class NativeCall {

  static {
    System.loadLibrary("NativeCall");
  }

  byte[] array;

  @Param({"1"})
  int length;

  @Setup
  public void setup() {
    array = new byte[length];
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
  public void emptyMethod() {
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

  public static native void emptyStaticNativeMethod();
  public static native void emptyStaticNativeCriticalMethod();
  public static native long staticNativeMethod(byte[] b);
  public static native long staticNativeCriticalMethod(byte[] b);
  @CompilerControl(CompilerControl.Mode.DONT_INLINE)
  public static long staticNativeMethodWithManyArgsHelper(long l1, long l2, long l3, long l4, long l5, double d) {
    return staticNativeMethodWithManyArgs(l1, l2, l3, l4, l5, d);
  }
  public static native long staticNativeMethodWithManyArgs(long l1, long l2, long l3, long l4, long l5, double d);
  public native void emptyNativeMethod();
  public native long nativeMethod(long l, double d);
  @CompilerControl(CompilerControl.Mode.DONT_INLINE)
  public long nativeMethodWithManyArgsHelper(long l1, long l2, long l3, long l4, long l5, double d) {
    return nativeMethodWithManyArgs(l1, l2, l3, l4, l5, d);
  }
  public native long nativeMethodWithManyArgs(long l1, long l2, long l3, long l4, long l5, double d);

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
  public static void staticMethodCallingStaticNative() {
    emptyStaticNativeMethod();
  }

  @Benchmark
  public static void staticMethodCallingStaticNativeCritical() {
    emptyStaticNativeCriticalMethod();
  }

  @Benchmark
  public void methodCallingStaticNative() {
    staticNativeMethod(array);
  }

  @Benchmark
  public void methodCallingStaticNativeCritical() {
    staticNativeCriticalMethod(array);
  }

  @Benchmark
  public static void staticMethodCallingStaticNativeWithManyArgs() {
    staticNativeMethodWithManyArgsHelper(42l, 42l, 42l, 42l, 42l, 42.0d);
  }

  @Benchmark
  public void methodCalling() {
    emptyMethod();
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
  public void methodCallingNative() {
    emptyNativeMethod();
  }

  @Benchmark
  public void methodCallingNativeWithArgs() {
    nativeMethod(42l, 42.0d);
  }

  @Benchmark
  public void methodCallingNativeWithManyArgs() {
    nativeMethodWithManyArgsHelper(42l, 42l, 42l, 42l, 42l, 42.0d);
  }

  @Benchmark
  @Fork(jvmArgsAppend = "-XX:-TieredCompilation")
  public static void staticMethodCallingStaticNativeNoTiered() {
    emptyStaticNativeMethod();
  }

  @Benchmark
  @Fork(jvmArgsAppend = "-XX:+PreferInterpreterNativeStubs")
  public static void staticMethodCallingStaticNativeIntStub() {
    emptyStaticNativeMethod();
  }

  public static void main(String[] args) {
    for (int i = 0; i < 100_000; i++) {
      staticMethodCallingStaticNative();
    }
  }
}
