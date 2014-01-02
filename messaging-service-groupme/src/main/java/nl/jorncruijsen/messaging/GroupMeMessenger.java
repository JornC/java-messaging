package nl.jorncruijsen.messaging;


public class GroupMeMessenger {
  private static final String JSON = "{\"bot_id\": \"%s\", \"text\": \"%s\"}";
  private static final String GROUPME_POST_URL = "https://api.groupme.com/v3/bots/post";
  private final URLRetriever urlRetriever;

  public GroupMeMessenger(final URLRetriever urlRetriever) {
    this.urlRetriever = urlRetriever;
  }

  public void sendMessage(String botKey, String text) {
    text = text.replace("\n", "\\n");
    text = text.replace("\r", "\\r");

    urlRetriever.postContent(GROUPME_POST_URL, String.format(JSON, botKey, text));
  }
}
