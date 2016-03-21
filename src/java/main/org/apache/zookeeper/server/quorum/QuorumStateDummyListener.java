package org.apache.zookeeper.server.quorum;

import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuorumStateDummyListener implements QuorumStateListener {
	private Logger LOG = LoggerFactory.getLogger(QuorumStateDummyListener.class);
	Timer timer;
	@Override
	public void startup(Properties zkProp, QuorumPeerConfig config) {
		LOG.info("initialize....");
	}

	@Override
	public void stateChanged(QuorumStateListener.State state, QuorumPeer peer) {
		if(state == State.LEADING_ENTER){
			timer = new Timer();
			timer.schedule(new TimerTask(){
				@Override
				public void run() {
					LOG.info("LEADING tick...");
				}
				
			}, 1000, 5000);
		}else if(state == State.LEADING_LEAVE){
			if(timer != null){
				timer.cancel();
				timer = null;
			}
		}
	}

	@Override
	public void shutdown() {
		
	}

}
