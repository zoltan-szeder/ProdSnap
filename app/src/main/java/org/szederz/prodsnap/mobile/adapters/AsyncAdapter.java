package org.szederz.prodsnap.mobile.adapters;

import android.view.View;

import org.szederz.prodsnap.utils.function.Consumer;
import org.szederz.prodsnap.utils.function.Supplier;

public class AsyncAdapter {
  View view;

  public AsyncAdapter(View view) {
    this.view = view;
  }

  public <R> void run(final Runnable runnable, final Runnable successHandler, final Consumer<Exception> failureHandler) {
    new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          runnable.run();
          view.post(successHandler);
        } catch (final Exception e) {
          handleError(failureHandler, e);
        }
      }
    }).start();
  }

  public <R> void run(final Supplier<R> function, final Consumer<R> successHandler, final Consumer<Exception> failureHandler) {
    new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          handleSuccess(successHandler, function.get());
        } catch (final Exception e) {
          handleError(failureHandler, e);
        }
      }
    }).start();
  }

  private <R> void handleSuccess(final Consumer<R> successHandler, final R r) {
    view.post(new Runnable() {
      @Override
      public void run() {
        successHandler.accept(r);
      }
    });
  }

  private void handleError(final Consumer<Exception> failureHandler, final Exception e) {
    view.post(new Runnable() {
      @Override
      public void run() {
        failureHandler.accept(e);
      }
    });
  }
}
