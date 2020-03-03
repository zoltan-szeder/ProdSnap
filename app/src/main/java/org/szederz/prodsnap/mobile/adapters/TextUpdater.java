package org.szederz.prodsnap.mobile.adapters;

import android.widget.TextView;

public class TextUpdater implements Runnable {
  private final TextView view;
  private final String text;


  public static void update(TextView view, String text) {
    view.post(new TextUpdater(view, text));
  }

  public TextUpdater(TextView view, String text) {
    this.view = view;
    this.text = text;
  }

  @Override
  public void run() {
    view.setText(text);
  }
}
