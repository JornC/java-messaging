package nl.jorncruijsen.messaging.providers;

import java.util.Properties;

public interface MessageService extends MessageProvider {
  ChannelManager getChannelManager();

  void init(Properties properties);

  void sendMessage(AbstractMessageChannel channel, String message);
}
