package nl.jorncruijsen.messaging.providers;

import nl.jorncruijsen.messaging.listeners.MessageListener;

public abstract class AbstractMessageService implements MessageService {
  private final ChannelManager channelManager;

  public AbstractMessageService(final ChannelManager channelManager) {
    this.channelManager = channelManager;

  }

  @Override
  public void addMessageListener(final MessageListener listener, final MessageChannel messageChannel) {

  }

  public ChannelManager getChannelManager() {
    return channelManager;
  }

  @Override
  public void removeMessageListener(final MessageListener listener, final MessageChannel messageChannel) {

  }

  @Override
  public void sendMessage(final MessageChannel channel, final String message) {
    channel.sendMessage(message);
  }
}
