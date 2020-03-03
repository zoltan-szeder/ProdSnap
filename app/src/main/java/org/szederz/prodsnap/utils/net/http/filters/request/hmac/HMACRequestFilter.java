package org.szederz.prodsnap.utils.net.http.filters.request.hmac;

import org.szederz.prodsnap.utils.encoding.Base64Util;
import org.szederz.prodsnap.utils.function.Function;
import org.szederz.prodsnap.utils.function.Supplier;
import org.szederz.prodsnap.utils.net.http.HttpRequestFilter;
import org.szederz.prodsnap.utils.net.http.Request;

import static org.szederz.prodsnap.utils.net.http.RequestFormatter.format;


public class HMACRequestFilter implements HttpRequestFilter {
  private final Supplier<Long> currentTimeSupplier;
  private final Supplier<String> secret;
  private final Function<byte[], String> encoder;

  public HMACRequestFilter(Supplier<String> secret) {
    this(
      new Supplier<Long>() {
        @Override
        public Long get() {
          return System.currentTimeMillis();
        }
      },
      new Function<byte[], String>() {
        @Override
        public String apply(byte[] s) {
          return new Base64Util().encode(s);
        }
      },
      secret
    );
  }

  public HMACRequestFilter(Supplier<Long> currentTimeSupplier, Function<byte[], String> encoder, Supplier<String> secret) {
    this.currentTimeSupplier = currentTimeSupplier;
    this.encoder = encoder;
    this.secret = secret;
  }

  @Override
  public Request apply(Request request) {
    Request ephemeralRequest = Request.builder(request)
      .header("Authorization-Time", String.valueOf(currentTimeSupplier.get()))
      .build();

    return Request.builder(ephemeralRequest)
      .header("Authorization", generateHMACSignature(ephemeralRequest))
      .build();
  }

  private String generateHMACSignature(Request request) {
    return "HMAC " + encoder.apply(
      new HmacSha256Generator(secret.get())
        .generateHMAC(format(request)));
  }
}
