package nl.jorncruijsen.messaging.providers;

import nl.jorncruijsen.messaging.listeners.MessageListener;

public abstract class AbstractMessageChannel implements MessageChannel {
  protected MessageService messageService;

  public AbstractMessageChannel(final MessageService messageService) {
    this.messageService = messageService;
  }

  @Override
  public void addMessageListener(final MessageListener handler) {
    messageService.addMessageListener(handler, this);
  }

  @Override
  public void removeMessageListener(final MessageListener listener) {
    messageService.removeMessageListener(listener, this);
  }

  @Override
  public void sendMessage(final String message) {
    messageService.sendMessage(this, message);
  }

  public boolean equals(final AbstractMessageChannel other) {
    return other.getChannelId().equals(getChannelId());
  }
}
