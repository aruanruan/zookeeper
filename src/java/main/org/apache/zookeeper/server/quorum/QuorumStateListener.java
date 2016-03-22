package org.apache.zookeeper.server.quorum;

import java.util.Properties;

/**
 * 
 * @author aruan
 * email: aruanruan@vip.sina.com
 * qq: 33224696
 * 2016/3/20 9:35:43
 *
 */
public interface QuorumStateListener {
	public enum State{
		LOOKING_ENTER,
		LOOKING_LEAVE,
		OBSERVING_ENTER,
		OBSERVING_LEAVE,
		FOLLOWING_ENTER,
		FOLLOWING_LEAVE,
		LEADING_ENTER,
		LEADING_LEAVE
	}
	public void initialize(Properties zkProp, QuorumPeerConfig config);
	public void startup();
	public void shutdown();
	public void stateChanged(State state, QuorumPeer peer);
}
