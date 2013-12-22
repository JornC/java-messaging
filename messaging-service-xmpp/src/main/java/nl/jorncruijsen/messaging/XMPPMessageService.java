package nl.jorncruijsen.messaging;

import java.util.Properties;

import nl.jorncruijsen.messaging.providers.AbstractMessageService;

public class XMPPMessageService extends AbstractMessageService {

  public XMPPMessageService() {
    super(new XMPPChannelManager());
  }

  @Override
  public void init(final Properties properties) {

  }



}
