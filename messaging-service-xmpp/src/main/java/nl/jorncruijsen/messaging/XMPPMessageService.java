package nl.jorncruijsen.messaging;

import java.util.Properties;

import nl.jorncruijsen.messaging.providers.AbstractMessageChannel;
import nl.jorncruijsen.messaging.providers.AbstractMessageService;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;

public class XMPPMessageService extends AbstractMessageService<XMPPMessageChannel, XMPPChannelManager> {

  private XMPPBot xmppBot;

  public XMPPMessageService() {
    super(new XMPPChannelManager());
    channelManager.setMessageService(this);
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

    xmppBot = new XMPPBot(connection, channelManager);
  }

  @Override
  public void sendMessage(final AbstractMessageChannel channel, final String message) {
    xmppBot.sendMessage(channel, message);
  }
}
