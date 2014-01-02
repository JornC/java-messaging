package nl.jorncruijsen.messaging.domain;

public class Message {
  private final String senderId;
  private final String sender;
  private final String text;

  public Message(String senderId, final String sender, final String text) {
    this.senderId = senderId;
    this.sender = sender;
    this.text = text;
  }

  public String getSender() {
    return sender;
  }

  public String getText() {
    return text;
  }

  public String getSenderId() {
    return senderId;
  }
}
