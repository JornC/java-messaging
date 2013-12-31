package nl.jorncruijsen.messaging;

import java.util.Properties;

import nl.jorncruijsen.messaging.providers.AbstractMessageService;
import nl.jorncruijsen.messaging.providers.MessageChannel;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.ServiceDiscoveryManager;

public class XMPPMessageService extends AbstractMessageService<XMPPChannelManager> {

  private class XMPPBot implements MessageListener {
    private final XMPPConnection connection;

    private ChatManager manager;

    public XMPPBot(final XMPPConnection connection) {
      this.connection = connection;
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
      if (message.getBody() == null) {
        return;
      }

      final nl.jorncruijsen.messaging.domain.Message javaMsg = new nl.jorncruijsen.messaging.domain.Message(chat.getParticipant(), message.getBody());

      final MessageChannel channel = channelManager.findChannel(chat);

      final Iterable<nl.jorncruijsen.messaging.listeners.MessageListener> listeners = channelManager.getListeners(channel);

      for (final nl.jorncruijsen.messaging.listeners.MessageListener listener : listeners) {
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
        final Message msg = new Message(chat.getParticipant(), Message.Type.chat);
        msg.setBody(message);
        chat.sendMessage(msg);
      } catch (final XMPPException ex) {
        System.out.println("Failed to send message");
        ex.printStackTrace();
      }
    }

    public void sendMessage(final MessageChannel channel, final String message) {
      if (channel instanceof XMPPMessageChannel) {
        final XMPPMessageChannel xmppChannel = (XMPPMessageChannel) channel;

        final Chat chat = manager.createChat(xmppChannel.getEmail(), null);
        sendMessage(chat, message);
      }
    }
  }

  private XMPPBot xmppBot;

  public XMPPMessageService() {
    super(new XMPPChannelManager());
    channelManager.setMessageService(this);
  }

  @Override
  public void addMessageListener(final nl.jorncruijsen.messaging.listeners.MessageListener listener, final MessageChannel messageChannel) {
    channelManager.addMessageListener(messageChannel, listener);
  }

  @Override
  public void init(final Properties properties) {
    /* Check DB connection and crash if it fails, somehow. */
    final ConnectionConfiguration connConfig = new ConnectionConfiguration("talk.google.com", 5222, "gmail.com");
    final XMPPConnection connection = new XMPPConnection(connConfig);

    try {
      connection.connect();
      connection.login(properties.getProperty("xmpp.user"), properties.getProperty("xmpp.pass"), "XMPP-Bot");

      final Presence presence = new Presence(Presence.Type.available);
      connection.sendPacket(presence);
    } catch (final XMPPException ex) {
      System.out.println("Failed to log in as " + connection.getUser());
      ex.printStackTrace();
      return;
    }

    new Thread(new Runnable() {

      @Override
      public void run() {
        xmppBot = new XMPPBot(connection);
      }
    }).run();
  }

  @Override
  public void removeMessageListener(final nl.jorncruijsen.messaging.listeners.MessageListener listener, final MessageChannel messageChannel) {
    channelManager.removeMessageListener(messageChannel, listener);
  }

  @Override
  public void sendMessage(final MessageChannel channel, final String message) {
    xmppBot.sendMessage(channel, message);
  }
}
