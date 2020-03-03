package org.szederz.prodsnap.utils.encoding;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.szederz.prodsnap.utils.StringUtils.join;

public class SimpleJson {
  private static ObjectMapper MAPPER = new ObjectMapper();

  public Map<String, String> parsePlainText(String json) {
    Map<String, Object> parsedJson = parse(json);

    HashMap<String, String> retVal = new HashMap<>();
    for (Map.Entry<String, Object> entry : parsedJson.entrySet())
      retVal.put(entry.getKey(), String.valueOf(entry.getValue()));

    return retVal;
  }

  public List<Map<String, Object>> parseList(String json) {
    try {
      return (List<Map<String, Object>>) MAPPER.readValue(json, List.class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public Map<String, Object> parse(String json) {
    try {
      if (json == null || json.isEmpty())
        return new HashMap<>();

      return (Map<String, Object>) MAPPER.readValue(json, Map.class);

    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public String serialize(Map<String, String> map) {
    ArrayList<String> list = new ArrayList<>();
    for (Map.Entry<String, String> entry : map.entrySet()) {
      list.add(String.format("\"%s\":\"%s\"", entry.getKey(), entry.getValue()));
    }
    return "{" +
      join(",", list) +
      "}";
  }
}
