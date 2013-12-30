package nl.jorncruijsen.messaging;

import java.util.ArrayList;
import java.util.Properties;

import nl.jorncruijsen.messaging.listeners.MessageListener;
import nl.jorncruijsen.messaging.providers.ChannelManager;
import nl.jorncruijsen.messaging.providers.MessageChannel;
import nl.jorncruijsen.messaging.providers.MessageService;

/**
 * A {@link MultiPlexedMessageService} channels messages for different services
 * over a single service.
 */
public class MultiPlexedMessageService implements MessageService {
  @SuppressWarnings("deprecation")
  /**
   * TODO Don't cache the channels, iterate over services' channel managers instead of caching them here.
   */
  private final MultiPlexedChannelManager channelManager = new MultiPlexedChannelManager();

  private final ArrayList<MessageService> services = new ArrayList<>();

  @Override
  public void addMessageListener(final MessageListener listener, final MessageChannel messageChannel) {
    messageChannel.addMessageListener(listener);
  }

  public void addMessagingService(final MessageService service) {
    services.add(service);

    for (final MessageChannel channel : service.getChannelManager()) {
      channelManager.add(channel);
    }
  }

  @Override
  public ChannelManager getChannelManager() {
    return channelManager;
  }

  @Override
  public void init(final Properties properties) {
    /** No-op **/
  }

  @Override
  public void removeMessageListener(final MessageListener listener, final MessageChannel messageChannel) {
    messageChannel.removeMessageListener(listener);
  }

  @Override
  public void sendMessage(final MessageChannel channel, final String message) {
    for (final MessageService service : services) {
      service.sendMessage(channel, message);
    }
  }
}
