package nl.jorncruijsen.messaging.providers;

public abstract class AbstractMessageService<CM extends ChannelManager> implements MessageService {
  protected final CM channelManager;

  public AbstractMessageService(final CM channelManager) {
    this.channelManager = channelManager;
  }

  @Override
  public ChannelManager getChannelManager() {
    return channelManager;
  }
}
