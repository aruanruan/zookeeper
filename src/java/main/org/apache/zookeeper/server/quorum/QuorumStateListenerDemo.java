package org.apache.zookeeper.server.quorum;

import java.util.Properties;

public class QuorumStateListenerDemo implements QuorumStateListener {

	@Override
	public void initialize(Properties zkProp, QuorumPeerConfig config) {
		// TODO Auto-generated method stub
		System.out.println("initialize....");
	}

	@Override
	public void stateChanged(QuorumStateListener.State state, QuorumPeer peer) {
		System.out.println(state);
	}

}
