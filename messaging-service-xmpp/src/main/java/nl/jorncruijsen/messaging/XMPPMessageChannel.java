package nl.jorncruijsen.messaging;

import nl.jorncruijsen.messaging.providers.AbstractMessageChannel;
import nl.jorncruijsen.messaging.providers.MessageService;

import org.jivesoftware.smack.RosterEntry;

public class XMPPMessageChannel extends AbstractMessageChannel {
  private final String email;
  private final String name;

  public XMPPMessageChannel(final MessageService service, final RosterEntry entry) {
    super(service);

    name = entry.getName() == null ? entry.getUser() : entry.getName();
    email = entry.getUser();
  }

  public String getEmail() {
    return email;
  }

  public String getName() {
    return name;
  }

  @Override
  public String getChannelId() {
    return getEmail();
  }
}
