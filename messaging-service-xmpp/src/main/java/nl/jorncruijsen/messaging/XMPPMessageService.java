package nl.jorncruijsen.messaging;

import java.util.Properties;

import nl.jorncruijsen.messaging.providers.AbstractMessageChannel;
import nl.jorncruijsen.messaging.providers.AbstractMessageService;

public class XMPPMessageService extends AbstractMessageService<XMPPMessageChannel, XMPPChannelManager> {
  private XMPPBotConnector xmppConnector;

  public XMPPMessageService() {
    super(new XMPPChannelManager());
    channelManager.setMessageService(this);
  }

  @Override
  public void init(final Properties properties) {
    Object lock = new Object();

    // Initialize the XMPP Connector
    xmppConnector = new XMPPBotConnector(lock, properties, channelManager);

    // Lock the thread until the connector is done initialising
    synchronized(lock) {
      new Thread(xmppConnector).start();

      try {
        lock.wait();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public void sendMessage(final AbstractMessageChannel channel, final String message) {
    xmppConnector.sendMessage(channel, message);
  }
}
