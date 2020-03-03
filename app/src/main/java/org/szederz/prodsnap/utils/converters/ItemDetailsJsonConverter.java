package org.szederz.prodsnap.utils.converters;

import org.szederz.prodsnap.entities.ItemDetails;
import org.szederz.prodsnap.utils.encoding.SimpleJson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.szederz.prodsnap.utils.MapUtils.asFlatMap;

public class ItemDetailsJsonConverter {
  private static SimpleJson json = new SimpleJson();

  public static ItemDetails parsed(String jsonString) {
    if (jsonString == null)
      return null;

    return parseMap(json.parse(jsonString));
  }

  public static String serialized(ItemDetails details) {
    return json.serialize(asFlatMap(
      "id", details.getId(),
      "name", details.getName(),
      "price", String.valueOf(details.getPrice()),
      "currency", "Ft"
    ));
  }

  public static List<ItemDetails> allParsed(String jsonString) {
    if (jsonString == null)
      return null;

    return parseListOfMap(json.parseList(jsonString));

  }

  private static List<ItemDetails> parseListOfMap(List<Map<String, Object>> listOfMap) {
    ArrayList<ItemDetails> itemDetails = new ArrayList<>();

    for (Map<String, Object> map : listOfMap) {
      itemDetails.add(parseMap(map));
    }

    return itemDetails;
  }

  private static ItemDetails parseMap(Map<String, Object> flatMap) {
    return ItemDetails.builder()
      .id((String) flatMap.get("id"))
      .name((String) flatMap.get("name"))
      .price((long) Double.parseDouble(String.valueOf(flatMap.get("price"))), String.valueOf(flatMap.get("currency")))
      .build();
  }
}
