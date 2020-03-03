package org.szederz.prodsnap.utils.net.http.client;

import org.szederz.prodsnap.utils.StringUtils;
import org.szederz.prodsnap.utils.net.http.Request;
import org.szederz.prodsnap.utils.net.http.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class VolatileHttpClient {
  private final HttpURLConnection conn;

  public VolatileHttpClient(HttpURLConnection conn) {
    this.conn = conn;
  }

  public Response send(Request request) throws IOException {
    conn.setRequestMethod(request.getMethod());

    for (Map.Entry<String, List<String>> entry : request.getHeaders().entrySet()) {
      for (String value : entry.getValue()) {
        conn.setRequestProperty(entry.getKey(), value);
      }
    }

    writeRequest(request);

    return readResponse();
  }

  private void writeRequest(Request request) throws IOException {
    if (!request.getHeaders().containsKey("Content-Length"))
      return;

    conn.setDoOutput(true);
    OutputStream os = conn.getOutputStream();
    os.write(request.getBody().getBytes());
  }

  private Response readResponse() throws IOException {
    int responseCode = conn.getResponseCode();
    if (responseCode >= 400)
      return new Response(
        responseCode,
        conn.getHeaderFields(),
        readAll(conn.getErrorStream()));

    return new Response(
      responseCode,
      conn.getHeaderFields(),
      readAll(conn.getInputStream())
    );
  }

  private String readAll(InputStream is) throws IOException {
    if (!conn.getDoInput())
      return null;

    return readStream(is);
  }

  private String readStream(InputStream is) throws IOException {
    try {
      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
      List<String> lines = new ArrayList<>();

      String line;
      while ((line = bufferedReader.readLine()) != null)
        lines.add(line);

      return StringUtils.join("\n", lines);
    } finally {
      is.close();
    }
  }
}
