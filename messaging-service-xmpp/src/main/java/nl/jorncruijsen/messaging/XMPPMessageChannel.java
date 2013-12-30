package nl.jorncruijsen.messaging;

import nl.jorncruijsen.messaging.providers.MessageChannelImpl;
import nl.jorncruijsen.messaging.providers.MessageService;

import org.jivesoftware.smack.RosterEntry;

public class XMPPMessageChannel extends MessageChannelImpl {
  private final String email;
  private final String name;

  public XMPPMessageChannel(final MessageService service, final RosterEntry entry) {
    super(service);

    name = entry.getName();
    email = entry.getUser();
  }

  @Override
  public boolean equals(final Object other) {
    return other instanceof XMPPMessageChannel ? this.equals((XMPPMessageChannel) other) : super.equals(other);
  }

  public boolean equals(final XMPPMessageChannel other) {
    return other.email.equals(email) && other.getName().equals(getName());
  }

  public String getEmail() {
    return email;
  }

  public String getName() {
    return name;
  }
}
