package nl.jorncruijsen.messaging.providers;

import nl.jorncruijsen.messaging.listeners.MessageListener;

public abstract class AbstractMessageService<MC extends AbstractMessageChannel, CM extends AbstractChannelManager<MC>> implements MessageService {
  protected final CM channelManager;

  public AbstractMessageService(final CM channelManager) {
    this.channelManager = channelManager;
  }

  @Override
  public ChannelManager getChannelManager() {
    return channelManager;
  }

  @Override
  public void addMessageListener(final MessageListener listener, final AbstractMessageChannel messageChannel) {
    channelManager.addMessageListener(messageChannel, listener);
  }

  @Override
  public void removeMessageListener(final MessageListener listener, final AbstractMessageChannel messageChannel) {
    channelManager.removeMessageListener(messageChannel, listener);
  }
}
