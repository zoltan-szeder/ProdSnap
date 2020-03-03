package org.szederz.prodsnap.services.formatters.barcode;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.EAN13Writer;

import java.util.Arrays;

public class QuickBarcodeBitmapGenerator implements BarcodeBitmapGenerator {
    private final int width;
    private final int height;

    public QuickBarcodeBitmapGenerator(){
        this(500, 150);
    }

    public QuickBarcodeBitmapGenerator(int width, int height) {
        this.width = width;
        this.height = height;
    }


    public Bitmap createBitmap(String value) {
        BitMatrix matrix = createOneLineBarcode(value);
        return addHeightToBarcode(matrix);
    }

    private BitMatrix createOneLineBarcode(String value) {
        try {
            EAN13Writer barcodeWriter = new EAN13Writer();
            return barcodeWriter.encode(
                    value,
                    BarcodeFormat.EAN_13,
                    width,
                    1
            );
        } catch (WriterException e) {
            throw new RuntimeException(e);
        }
    }

    private Bitmap addHeightToBarcode(BitMatrix matrix) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        for (int i = 0; i < width; i++) {
            int[] column = new int[height];
            Arrays.fill(column, matrix.get(i, 0) ? Color.BLACK : Color.WHITE);
            bitmap.setPixels(column, 0, 1, i, 0, 1, height);
        }

        return bitmap;
    }
}
