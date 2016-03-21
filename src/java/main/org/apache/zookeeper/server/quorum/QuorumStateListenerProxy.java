package org.apache.zookeeper.server.quorum;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 
 * @author aruan
 * email: aruanruan@vip.sina.com
 * qq: 33224696
 * 2016年3月21日 下午9:26:56
 *
 */
public class QuorumStateListenerProxy implements QuorumStateListener {
	private QuorumStateListener chain;
	private List<QuorumStateListener> proxies;
	
	public QuorumStateListenerProxy(){
		
	}
	
	public QuorumStateListenerProxy(QuorumStateListener chain){
		this.chain = chain;
	}
	
	public QuorumStateListener getChain() {
		return chain;
	}

	public void setChain(QuorumStateListener chain) {
		this.chain = chain;
	}

	public QuorumStateListener add(QuorumStateListener listener) {
		if(listener != null){
			if(proxies == null){
				proxies = new ArrayList<QuorumStateListener>();
			}
			proxies.add(listener);
		}
		return this;
	}
	
	public QuorumStateListener add(QuorumStateListener... listeners) {
		if(listeners != null){
			if(proxies == null){
				proxies = new ArrayList<QuorumStateListener>();
			}
			if(listeners != null && listeners.length > 0){
				for(QuorumStateListener listener : listeners){
					if(listener != null) proxies.add(listener);
				}
			}
		}
		return this;
	}
	
	public QuorumStateListener add(List<QuorumStateListener> listeners) {
		if(listeners != null){
			if(proxies == null){
				proxies = new ArrayList<QuorumStateListener>();
			}
			if(listeners != null && listeners.size() > 0){
				for(QuorumStateListener listener : listeners){
					if(listener != null) proxies.add(listener);
				}
			}
		}
		return this;
	}

	 
	@Override
	public void startup(Properties zkProp, QuorumPeerConfig config) {
		if(proxies != null && !proxies.isEmpty()){
			for(QuorumStateListener listener : proxies){
				listener.startup(zkProp, config);
			}
		}
		if(chain != null){
			chain.startup(zkProp, config);
		}
	}

	@Override
	public void shutdown() {
		if(proxies != null && !proxies.isEmpty()){
			for(QuorumStateListener listener : proxies){
				listener.shutdown();
			}
		}
		if(chain != null){
			chain.shutdown();
		}
	}

	@Override
	public void stateChanged(State state, QuorumPeer peer) {
		if(proxies != null && !proxies.isEmpty()){
			for(QuorumStateListener listener : proxies){
				listener.stateChanged(state, peer);
			}
		}
		if(chain != null){
			chain.stateChanged(state, peer);
		}
	}

}
