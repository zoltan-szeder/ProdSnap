package org.szederz.prodsnap.utils.net.http.client;

import org.szederz.prodsnap.utils.function.Function;

import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class UnsafeSSLConnectionBuilder implements Function<String, HttpURLConnection> {
  @Override
  public HttpURLConnection apply(String url) {
    try {
      SSLContext context = SSLContext.getInstance("SSL");
      context.init(null, new TrustManager[]{new TrustAllManager()}, new SecureRandom());
      HttpsURLConnection conn = (HttpsURLConnection) new URL(url).openConnection();
      conn.setSSLSocketFactory(context.getSocketFactory());
      conn.setHostnameVerifier(new AllHostsValidVerifier());
      return conn;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static class TrustAllManager implements X509TrustManager {
    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
      return null;
    }

    public void checkClientTrusted(
      java.security.cert.X509Certificate[] certs, String authType) {
    }

    public void checkServerTrusted(
      java.security.cert.X509Certificate[] certs, String authType) {
    }
  }

  public static class AllHostsValidVerifier implements HostnameVerifier{
    @Override
    public boolean verify(String hostname, SSLSession session) {
      return true;
    }
  }
}
