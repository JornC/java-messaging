package nl.jorncruijsen.messaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import nl.jorncruijsen.messaging.listeners.MessageListener;
import nl.jorncruijsen.messaging.providers.ChannelManager;
import nl.jorncruijsen.messaging.providers.MessageChannel;
import nl.jorncruijsen.messaging.providers.MessageService;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.RosterEntry;

public class XMPPChannelManager implements ChannelManager {
  private final Map<MessageChannel, ArrayList<MessageListener>> channels = new HashMap<>();

  private MessageService service;

  public void addChannel(final RosterEntry entry) {
    final XMPPMessageChannel channel = new XMPPMessageChannel(service, entry);
    addIfNotExists(channel);
  }

  private void addIfNotExists(final XMPPMessageChannel channel) {
    if (!channels.containsKey(channel)) {
      channels.put(channel, new ArrayList<MessageListener>());
    }
  }

  public void addMessageListener(final MessageChannel messageChannel, final nl.jorncruijsen.messaging.listeners.MessageListener listener) {
    if ((messageChannel instanceof XMPPMessageChannel)) {
      final XMPPMessageChannel xmppChannel = (XMPPMessageChannel) messageChannel;
      addIfNotExists(xmppChannel);

      channels.get(xmppChannel).add(listener);
    }
  }

  public MessageChannel findChannel(final Chat chat) {
    final String email = chat.getParticipant().split("/", 2)[0];

    for (final MessageChannel channel : channels.keySet()) {
      if ((channel instanceof XMPPMessageChannel)) {
        final XMPPMessageChannel xmppChannel = (XMPPMessageChannel) channel;
        if (email.equals(xmppChannel.getEmail())) {
          return channel;
        }
      }
    }

    return null;
  }

  public Iterable<MessageListener> getListeners(final MessageChannel channel) {
    return channels.get(channel);
  }

  @Override
  public Iterator<MessageChannel> iterator() {
    return channels.keySet().iterator();
  }

  public void removeMessageListener(final MessageChannel messageChannel, final nl.jorncruijsen.messaging.listeners.MessageListener listener) {
    if (channels.containsKey(messageChannel)) {
      final ArrayList<MessageListener> arrayList = channels.get(messageChannel);

      while (arrayList.contains(listener)) {
        arrayList.remove(listener);
      }
    }
  }

  public void setMessageService(final MessageService service) {
    this.service = service;
  }
}
