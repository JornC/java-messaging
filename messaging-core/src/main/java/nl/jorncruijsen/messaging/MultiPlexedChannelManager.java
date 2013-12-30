package nl.jorncruijsen.messaging;

import java.util.ArrayList;

import nl.jorncruijsen.messaging.providers.ChannelManager;
import nl.jorncruijsen.messaging.providers.MessageChannel;

@Deprecated
class MultiPlexedChannelManager extends ArrayList<MessageChannel> implements ChannelManager {
  private static final long serialVersionUID = -2116025994024578257L;
}
