package nl.jorncruijsen.messaging.listeners;

import nl.jorncruijsen.messaging.domain.Message;
import nl.jorncruijsen.messaging.providers.MessageChannel;

public interface MessageListener {
  void handleMessage(MessageChannel channel, Message message);
}
