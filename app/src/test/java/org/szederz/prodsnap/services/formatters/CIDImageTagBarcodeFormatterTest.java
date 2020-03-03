package org.szederz.prodsnap.services.formatters;

import org.junit.Test;
import org.szederz.prodsnap.services.formatters.html.CIDImageTagBarcodeFormatter;

import static org.junit.Assert.*;

public class CIDImageTagBarcodeFormatterTest {
  private CIDImageTagBarcodeFormatter formatter = new CIDImageTagBarcodeFormatter();

  @Test
  public void testBarcodeFormat() {
    assertEquals(
      "<img src=\"cid:0123456789012\" alt=\"0123456789012\" />" +
        "<h2>0 12345 678901 2</h2>",
      formatter.format("0123456789012")
    );
  }
}