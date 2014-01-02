package nl.jorncruijsen.messaging.providers;

import nl.jorncruijsen.messaging.listeners.MessageListener;

public interface MessageChannel {
  void addMessageListener(MessageListener messageListener);

  void removeMessageListener(MessageListener messageListener);

  void sendMessage(String message);

  String getChannelId();
}
