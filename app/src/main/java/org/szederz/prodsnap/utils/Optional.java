package org.szederz.prodsnap.utils;

import org.szederz.prodsnap.utils.function.Function;

import androidx.annotation.NonNull;

public class Optional<T> {
  private final T t;

  public static <U> Optional<U> of(U t) {
    return new Optional<>(t);
  }

  public static <U> Optional<U> empty() {
    return new Optional<>();
  }

  public static <U> Optional<U> ofNullable(U t) {
    return new Optional<>(t);
  }

  private Optional() {
    t = null;
  }

  private Optional(T t) {
    this.t = t;
  }

  public T get() {
    return t;
  }

  public <R> Optional<R> map(Function<T, R> f) {
    if (isPresent())
      return Optional.of(f.apply(t));

    return Optional.empty();
  }

  public Optional<T> filter(Function<T, Boolean> f) {
    if (f.apply(t))
      return this;

    return Optional.empty();
  }

  public T orElseGet(T t) {
    if (isPresent())
      return this.t;

    return t;
  }

  public T orElseThrow(RuntimeException e) {
    if (isPresent())
      return t;

    throw e;
  }

  public boolean isPresent() {
    return t != null;
  }

  @NonNull
  @Override
  public String toString() {
    if (isPresent())
      return "Optional.of(" + t.toString() + ")";

    return "Optional.empty()";
  }
}
