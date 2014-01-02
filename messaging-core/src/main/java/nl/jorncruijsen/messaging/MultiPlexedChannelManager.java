package nl.jorncruijsen.messaging;

import java.util.ArrayList;

import nl.jorncruijsen.messaging.providers.ChannelManager;
import nl.jorncruijsen.messaging.providers.AbstractMessageChannel;

@Deprecated
class MultiPlexedChannelManager extends ArrayList<AbstractMessageChannel> implements ChannelManager {
  private static final long serialVersionUID = -2116025994024578257L;
}
