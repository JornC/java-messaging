package nl.jorncruijsen.messaging.providers;

import nl.jorncruijsen.messaging.listeners.MessageListener;

interface MessageProvider {

  /**
   * Register a listener for a specific channel on this provider.
   * 
   * @param listener {@link MessageListener} that will handle the messages.
   * @param messageChannel Channel to listen to.
   */
  void addMessageListener(MessageListener listener, AbstractMessageChannel messageChannel);

  /**
   * Remove a listener from a specific channel on this provider.
   * 
   * @param listener {@link MessageListener} to remove.
   * @param messageChannel {@link AbstractMessageChannel} to remove it from.
   */
  void removeMessageListener(MessageListener listener, AbstractMessageChannel messageChannel);
}
