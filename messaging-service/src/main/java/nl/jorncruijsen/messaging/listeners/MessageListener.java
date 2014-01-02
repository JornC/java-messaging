package nl.jorncruijsen.messaging.listeners;

import nl.jorncruijsen.messaging.domain.Message;
import nl.jorncruijsen.messaging.providers.AbstractMessageChannel;

public interface MessageListener {
  void handleMessage(AbstractMessageChannel channel, Message message);
}
