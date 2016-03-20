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
	public static final int STS_LOOKING_ENTER = 1;
	public static final int STS_LOOKING_LEAVE = 2;
	public static final int STS_OBSERVING_ENTER = 3;
	public static final int STS_OBSERVING_LEAVE = 4;
	public static final int STS_FOLLOWING_ENTER = 5;
	public static final int STS_FOLLOWING_LEAVE = 6;
	public static final int STS_LEADING_ENTER = 7;
	public static final int STS_LEADING_LEAVE = 8;
	public void initialize(Properties zkProp, QuorumPeerConfig config);
	public void stateChanged(int state, QuorumPeer peer);
}
