package org.tron.core.net.messagehandler;

import org.tron.core.exception.P2pException;
import org.tron.core.net.message.CypherMessage;
import org.tron.core.net.peer.PeerConnection;

public interface CypherMsgHandler {

  void processMessage(PeerConnection peer, CypherMessage msg) throws P2pException;

}
