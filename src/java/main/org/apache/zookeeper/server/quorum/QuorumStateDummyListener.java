package org.apache.zookeeper.server.quorum;

import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.zookeeper.server.quorum.QuorumPeer.QuorumServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuorumStateDummyListener implements QuorumStateListener {
	private Logger LOG = LoggerFactory.getLogger(QuorumStateDummyListener.class);
	Timer timer;
	long serverId;
	QuorumServer server;
	@Override
	public void initialize(Properties zkProp, QuorumPeerConfig config) {
		LOG.info("initialize....");
		serverId = config.getServerId();
		server = config.getServers().get(serverId);
	}

	@Override
	public void stateChanged(final QuorumStateListener.State state, QuorumPeer peer) {
		if(state == State.LEADING_ENTER
				|| state == State.FOLLOWING_ENTER
				|| state == State.LOOKING_ENTER){
			LOG.info("enter " + state);
			if(timer != null){
				timer.cancel();
				timer = null;
			}
			timer = new Timer();
			timer.schedule(new TimerTask(){
				@Override
				public void run() {
					LOG.info("tick..." + serverId + "->" + server.addr);
				}
				
			}, 1000, 5000);
		}else if(state == State.LEADING_LEAVE
				|| state == State.FOLLOWING_LEAVE
				|| state == State.LOOKING_LEAVE){
			LOG.info("leave " + state);
			if(timer != null){
				timer.cancel();
				timer = null;
			}
		}
	}
	
	@Override
	public void startup() {
		
	}

	@Override
	public void shutdown() {
		
	}

}
