package nl.jorncruijsen.messaging.providers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import nl.jorncruijsen.messaging.listeners.MessageListener;

public abstract class AbstractChannelManager<MC extends AbstractMessageChannel> implements ChannelManager {
  protected final Map<AbstractMessageChannel, ArrayList<MessageListener>> channels = new HashMap<>();

  @Override
  public Iterator<AbstractMessageChannel> iterator() {
    return channels.keySet().iterator();
  }

  protected void addIfNotExists(final AbstractMessageChannel channel) {
    if (!channels.containsKey(channel)) {
      System.out.println("New channel: " + channel.getChannelId());
      channels.put(channel, new ArrayList<MessageListener>());
    }
  }

  @SuppressWarnings("unchecked")
  public MC findChannel(final String channelId) {
    for (final AbstractMessageChannel channel : channels.keySet()) {
      if (channel.getClass().equals(getChannelType())) {
        if (channel.getChannelId().equals(channelId)) {
          return (MC) channel;
        }
      }
    }

    return null;
  }

  public Iterable<MessageListener> getListeners(final AbstractMessageChannel channel) {
    return channels.get(channel);
  }

  protected abstract Class<MC> getChannelType();

  public void removeMessageListener(final AbstractMessageChannel messageChannel, final MessageListener listener) {
    if (channels.containsKey(messageChannel)) {
      final ArrayList<MessageListener> lst = channels.get(messageChannel);

      while (lst.contains(listener)) {
        lst.remove(listener);
      }
    }
  }

  public void addMessageListener(AbstractMessageChannel messageChannel, MessageListener listener) {
    if (messageChannel.getClass().equals(getChannelType())) {
      addIfNotExists(messageChannel);
      channels.get(messageChannel).add(listener);
    }
  }

}
