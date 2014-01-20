package nl.jorncruijsen.messaging;

import java.util.Properties;

import nl.jorncruijsen.messaging.providers.AbstractMessageChannel;

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

public class XMPPBotConnector implements Runnable {
  private final XMPPChannelManager channelManager;
  private ChatManager manager;

  private final Properties properties;
  private XMPPBot bot;
  private final Object lock;

  private class XMPPBot implements MessageListener {

    public XMPPBot(XMPPConnection conn) {
      channelManager.clear();

      final Roster roster = conn.getRoster();
      manager = conn.getChatManager();

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

    public void sendMessage(final Chat chat, final String message) {
      try {
        chat.sendMessage(message);
      } catch (final XMPPException ex) {
        System.out.println("Failed to send message");
        ex.printStackTrace();
      }
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
  }

  public XMPPBotConnector(final Object lock, final Properties properties, XMPPChannelManager channelManager) {
    this.lock = lock;
    this.properties = properties;
    this.channelManager = channelManager;
  }

  @Override
  public void run() {
    // Check DB connection and crash if it fails, somehow.
    final ConnectionConfiguration connConfig = new ConnectionConfiguration("talk.google.com", 5222, "gmail.com");
    final XMPPConnection connection = new XMPPConnection(connConfig);

    for (;;) {
      try {
        connection.connect();
      } catch (final XMPPException ex) {
        System.out.println("Failed to connect to " + connection.getHost());
        ex.printStackTrace();
        continue;
      }

      try {
        connection.login(properties.getProperty("xmpp.user"), properties.getProperty("xmpp.pass"), "XMPP-Bot");
        System.out.println("Logged in as " + connection.getUser());

        final Presence presence = new Presence(Presence.Type.available);
        connection.sendPacket(presence);

      } catch (final XMPPException ex) {
        System.out.println("Failed to log in as " + connection.getUser());
        ex.printStackTrace();
        continue;
      }

      final Presence presence = new Presence(Presence.Type.available);
      connection.sendPacket(presence);

      bot = new XMPPBot(connection);

      // Release the lock
      synchronized(lock) {
        lock.notifyAll();
      }

      // Run forever
      final Object lock = new Object();
      synchronized (lock) {
        try {
          lock.wait();
        } catch (InterruptedException e) {
          // Eat
        }
      }
    }
  }

  public void sendMessage(final AbstractMessageChannel channel, final String message) {
    if (channel instanceof XMPPMessageChannel) {
      final XMPPMessageChannel xmppChannel = (XMPPMessageChannel) channel;

      final Chat chat = manager.createChat(xmppChannel.getEmail(), null);
      bot.sendMessage(chat, message);
    }
  }
}
