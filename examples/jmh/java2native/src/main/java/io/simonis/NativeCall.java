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
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

@State(Scope.Thread)
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
  native void nMethod();
  native int nativeMethod(int i, String s, byte[] b);
  static native int nativeMethod(double d, Object o);

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
  public static native long staticNativeMethod(long l, double d);
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
  public static void staticMethodCallingStaticNativeWithArgs() {
    staticNativeMethod(42l, 42.0d);
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