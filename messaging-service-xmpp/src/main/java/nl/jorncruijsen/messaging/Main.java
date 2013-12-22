package nl.jorncruijsen.messaging;

import nl.jorncruijsen.messaging.domain.Message;
import nl.jorncruijsen.messaging.listeners.MessageListener;
import nl.jorncruijsen.messaging.providers.MessageChannel;

public class Main {
  public static void main(final String[] args) {
    final XMPPMessageService service = new XMPPMessageService();
    service.init(null);

    for (final MessageChannel channel : service.getChannelManager()) {
      channel.addMessageListener(new MessageListener() {
        @Override
        public void handleMessage(final MessageChannel channel, final Message message) {
          channel.sendMessage(message.getText());
        }
      });
    }
  }
}
