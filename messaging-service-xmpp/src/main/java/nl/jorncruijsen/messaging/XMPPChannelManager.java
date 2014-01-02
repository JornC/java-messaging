package nl.jorncruijsen.messaging;

import nl.jorncruijsen.messaging.providers.AbstractChannelManager;
import nl.jorncruijsen.messaging.providers.MessageService;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.RosterEntry;

public class XMPPChannelManager extends AbstractChannelManager<XMPPMessageChannel> {
  private MessageService service;

  public void addChannel(final RosterEntry entry) {
    final XMPPMessageChannel channel = new XMPPMessageChannel(service, entry);
    addIfNotExists(channel);
  }

  public void setMessageService(final MessageService service) {
    this.service = service;
  }

  @Override
  protected Class<XMPPMessageChannel> getChannelType() {
    return XMPPMessageChannel.class;
  }

  public XMPPMessageChannel findChannel(Chat chat) {
    return findChannel(chat.getParticipant());
  }
}
