package org.szederz.prodsnap.utils;

import org.szederz.prodsnap.utils.function.Consumer;
import org.szederz.prodsnap.utils.function.Supplier;

public class Container<T> implements Consumer<T>, Supplier<T> {
  private T t;

  public Container() {
  }

  public Container(T t) {
    this.t = t;
  }

  @Override
  public void accept(T t) {
    this.t = t;
  }

  @Override
  public T get() {
    return t;
  }
}
