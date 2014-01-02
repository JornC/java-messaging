package nl.jorncruijsen.messaging;

import nl.jorncruijsen.messaging.providers.AbstractChannelManager;

public class GroupMeChannelManager extends AbstractChannelManager<GroupMeMessageChannel> {
  @Override
  protected Class<GroupMeMessageChannel> getChannelType() {
    return GroupMeMessageChannel.class;
  }

  public void addChannel(GroupMeMessageChannel groupMeMessageChannel) {
    addIfNotExists(groupMeMessageChannel);
  }
}
