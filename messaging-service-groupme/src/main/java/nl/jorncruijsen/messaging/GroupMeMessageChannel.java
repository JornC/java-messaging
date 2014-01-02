package nl.jorncruijsen.messaging;

import nl.jorncruijsen.messaging.providers.AbstractMessageChannel;
import nl.jorncruijsen.messaging.providers.MessageService;

public class GroupMeMessageChannel extends AbstractMessageChannel {
  private final String group;
  private final String key;

  public GroupMeMessageChannel(MessageService messageService, String group, String key) {
    super(messageService);
    this.group = group;
    this.key = key;
  }

  @Override
  public String getChannelId() {
    return getGroup();
  }

  public String getKey() {
    return key;
  }

  public String getGroup() {
    return group;
  }
}
