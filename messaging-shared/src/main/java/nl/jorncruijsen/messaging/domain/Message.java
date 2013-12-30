package nl.jorncruijsen.messaging.domain;

public class Message {
  private final String sender;
  private final String text;

  public Message(final String sender, final String text) {
    this.sender = sender;
    this.text = text;
  }

  public String getSender() {
    return sender;
  }

  public String getText() {
    return text;
  }
}
