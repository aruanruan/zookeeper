package org.apache.zookeeper.server.quorum;

import java.util.Properties;

public class QuorumStateDemo implements QuorumStateListener {

	@Override
	public void initialize(Properties zkProp, QuorumPeerConfig config) {
		// TODO Auto-generated method stub

	}

	@Override
	public void stateChanged(QuorumStateListener.State state, QuorumPeer peer) {
		System.out.println(state);
	}

}
