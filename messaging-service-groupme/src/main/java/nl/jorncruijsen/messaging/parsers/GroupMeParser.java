package nl.jorncruijsen.messaging.parsers;

import java.io.IOException;
import java.io.InputStream;

import nl.jorncruijsen.messaging.domain.Message;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;

public class GroupMeParser extends JsonParser {
  public static Message parseGroupMeMessage(final InputStream is) {
    try {
      final JsonNode tree = JsonParser.mapper.readTree(is);

      final String text = tree.get("text").getTextValue();
      final String name = tree.get("name").getTextValue();
      final String group = tree.get("group_id").getTextValue();

      return new Message(group, name, text);
    } catch (final JsonProcessingException e) {
      System.out.println("Error parsing.. ");
    } catch (final IOException e) {
      e.printStackTrace();
    }

    return null;
  }
}