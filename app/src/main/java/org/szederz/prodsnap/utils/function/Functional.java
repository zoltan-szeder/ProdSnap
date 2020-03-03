package org.szederz.prodsnap.utils.function;

public class Functional {
  public static <U> Supplier<U> supplierOf(final U u) {
    return new Supplier<U>() {
      @Override
      public U get() {
        return u;
      }
    };
  }

  public static <T, R> Function<T, R> functionOf(Class<T> tClass, final R r) {
    return new Function<T, R>() {
      @Override
      public R apply(T t) {
        return r;
      }
    };
  }
}
