package org.szederz.prodsnap.services.formatters.barcode;

import android.graphics.Bitmap;
import android.util.Base64;

import static org.szederz.prodsnap.services.formatters.barcode.ImageConverters.toPNG;

public class HtmlBarcodeSerializer implements BarcodeSerializer {
    private final BarcodeBitmapGenerator barcodeBitmapGenerator;

    public HtmlBarcodeSerializer() {
        this(new QuickBarcodeBitmapGenerator());
    }

    public HtmlBarcodeSerializer(BarcodeBitmapGenerator barcodeBitmapGenerator) {
        this.barcodeBitmapGenerator = barcodeBitmapGenerator;
    }

    @Override
    public String serialize(String barcode) {
        Bitmap image = barcodeBitmapGenerator.createBitmap(barcode);
        return "data:image/png;base64," + Base64.encodeToString(toPNG(image), Base64.DEFAULT)
                .replace("\n", "");
    }
}
