package nl.jorncruijsen.messaging;

import nl.jorncruijsen.messaging.providers.AbstractMessageChannel;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.ServiceDiscoveryManager;

public class XMPPBot implements MessageListener {
  private final XMPPConnection connection;

  private ChatManager manager;

  private final XMPPChannelManager channelManager;

  public XMPPBot(final XMPPConnection connection, XMPPChannelManager channelManager) {
    this.connection = connection;
    this.channelManager = channelManager;
    init();
  }

  private void init() {
    /* Set that we are a bot client. */
    ServiceDiscoveryManager.setIdentityType("bot");

    final Roster roster = connection.getRoster();
    manager = connection.getChatManager();

    for (final RosterEntry entry : roster.getEntries()) {
      channelManager.addChannel(entry);
    }

    manager.addChatListener(new ChatManagerListener() {
      @Override
      public void chatCreated(final Chat chat, final boolean createdLocally) {
        chat.addMessageListener(XMPPBot.this);
      }
    });
  }

  @Override
  public void processMessage(final Chat chat, final Message message) {
    String messageText = message.getBody();

    if (messageText == null) {
      return;
    }

    final XMPPMessageChannel channel = channelManager.findChannel(chat);

    String email = channel.getEmail();
    String name = channel.getName();

    final nl.jorncruijsen.messaging.domain.Message javaMsg = new nl.jorncruijsen.messaging.domain.Message(email, name, messageText);

    for (final nl.jorncruijsen.messaging.listeners.MessageListener listener : channelManager.getListeners(channel)) {
      new Thread() {
        @Override
        public void run() {
          listener.handleMessage(channel, javaMsg);
        };
      }.start();
    }
  }

  public void sendMessage(final Chat chat, final String message) {
    try {
      chat.sendMessage(message);
    } catch (final XMPPException ex) {
      System.out.println("Failed to send message");
      ex.printStackTrace();
    }
  }

  public void sendMessage(final AbstractMessageChannel channel, final String message) {
    if (channel instanceof XMPPMessageChannel) {
      final XMPPMessageChannel xmppChannel = (XMPPMessageChannel) channel;

      final Chat chat = manager.createChat(xmppChannel.getEmail(), null);
      sendMessage(chat, message);
    }
  }
}
