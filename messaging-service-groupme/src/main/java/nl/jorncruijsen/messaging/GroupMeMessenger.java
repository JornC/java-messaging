package nl.jorncruijsen.messaging;


public class GroupMeMessenger {
  private static final int MESSAGE_CHAR_LIMIT = 400;
  private static final String JSON = "{\"bot_id\": \"%s\", \"text\": \"%s\"}";
  private static final String GROUPME_POST_URL = "https://api.groupme.com/v3/bots/post";
  private final URLRetriever urlRetriever;

  public GroupMeMessenger(final URLRetriever urlRetriever) {
    this.urlRetriever = urlRetriever;
  }

  public void sendMessage(String botKey, String text) {
    text = text.replace("\n", "\\n").replace("\r", "\\r").replace(">", "&gt;");

    String[] lines = text.split("\\\\n");

    StringBuilder builder = new StringBuilder(MESSAGE_CHAR_LIMIT);
    for(String line : lines) {
      if(builder.length() + line.length() > 400) {
        urlRetriever.postContent(GROUPME_POST_URL, String.format(JSON, botKey, builder.toString()));
        builder = new StringBuilder(MESSAGE_CHAR_LIMIT);
      }

      builder.append(line + "\\n");
    }

    if(builder.length() > 0) {
      urlRetriever.postContent(GROUPME_POST_URL, String.format(JSON, botKey, builder.toString()));
    }
  }
}