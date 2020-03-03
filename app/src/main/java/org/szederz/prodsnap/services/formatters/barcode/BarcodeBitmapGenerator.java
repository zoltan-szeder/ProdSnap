package org.szederz.prodsnap.services.formatters.barcode;

import android.graphics.Bitmap;

public interface BarcodeBitmapGenerator {
    Bitmap createBitmap(String value);
}
