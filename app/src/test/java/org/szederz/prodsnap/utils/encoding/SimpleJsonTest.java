package org.szederz.prodsnap.utils.encoding;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.szederz.prodsnap.utils.MapUtils.asFlatMap;

public class SimpleJsonTest {
  private SimpleJson json = new SimpleJson();

  @Test
  public void testJsonSerialization() {
    assertEquals("{}", json.serialize(asFlatMap()));
    assertEquals(
      "{\"key\":\"value\"}",
      json.serialize(asFlatMap(
        "key", "value")));
    assertEquals(
      "{\"key1\":\"value1\",\"key2\":\"value2\"}",
      json.serialize(asFlatMap(
        "key1", "value1",
        "key2", "value2")));
  }

  @Test
  public void testJsonParsing(){
    assertTrue(json.parse(null).isEmpty());
    assertTrue(json.parse("").isEmpty());
    assertEquals("value", json.parse("{\"key\":\"value\"}").get("key"));
    assertEquals("value1", json.parse("{\"key1\":\"value1\"}").get("key1"));
    assertEquals("value2", json.parse("{\"key1\":\"value1\",\"key2\":\"value2\"}").get("key2"));
  }
}