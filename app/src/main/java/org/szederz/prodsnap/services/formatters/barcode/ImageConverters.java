package org.szederz.prodsnap.services.formatters.barcode;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

public class ImageConverters {
  public static byte[] toPNG(Bitmap image) {
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    image.compress(Bitmap.CompressFormat.PNG, 100, os);
    return os.toByteArray();
  }
}
