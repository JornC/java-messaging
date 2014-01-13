package nl.jorncruijsen.messaging;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.jorncruijsen.messaging.domain.Message;
import nl.jorncruijsen.messaging.listeners.MessageListener;
import nl.jorncruijsen.messaging.parsers.GroupMeParser;
import nl.jorncruijsen.messaging.providers.AbstractMessageChannel;
import nl.jorncruijsen.messaging.providers.AbstractMessageService;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

public class GroupMeMessageService extends AbstractMessageService<GroupMeMessageChannel, GroupMeChannelManager> {

  private final GroupMeMessenger messenger;
  private String botName;

  public GroupMeMessageService() {
    super(new GroupMeChannelManager());

    URLRetriever urlRetriever = new URLRetriever();
    messenger = new GroupMeMessenger(urlRetriever);
  }

  @Override
  public void init(final Properties properties) {
    String[] groups = properties.getProperty("groupme.groups").split(",");
    String[] keys = properties.getProperty("groupme.keys").split(",");
    botName = properties.getProperty("groupme.botnames");

    assert groups.length == keys.length : "Group/key length not equal.";

    for (int i = 0; i < groups.length; i++) {
      GroupMeMessageChannel channel = new GroupMeMessageChannel(this, groups[i], keys[i]);
      channelManager.addChannel(channel);
    }

    final Server server = new Server(4444);
    server.setHandler(new AbstractHandler() {
      @Override
      public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        handleContent(request.getInputStream());

        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);
      }
    });

    try {
      server.start();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void handleContent(final ServletInputStream str) {
    final Message message = GroupMeParser.parseGroupMeMessage(str);

    if (message == null || message.getSender().equals(botName)) {
      return;
    }

    final GroupMeMessageChannel channel = channelManager.findChannel(message.getSenderId());

    Iterable<MessageListener> listeners = channelManager.getListeners(channel);

    if(listeners == null) {
      return;
    }

    for (final MessageListener listener : listeners) {
      listener.handleMessage(channel, message);
    }
  }

  @Override
  public void sendMessage(AbstractMessageChannel channel, String message) {
    if (channel instanceof GroupMeMessageChannel) {
      final GroupMeMessageChannel groupMeChannel = (GroupMeMessageChannel) channel;

      messenger.sendMessage(groupMeChannel.getKey(), message);
    }
  }
}
