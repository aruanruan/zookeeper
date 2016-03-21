package org.apache.zookeeper.server.quorum;

import java.util.Properties;

public class QuorumStateListenerProxy implements QuorumStateListener {
	private QuorumStateListener chain;
	private QuorumStateListener proxy;
	
	public QuorumStateListener getChain() {
		return chain;
	}

	public void setChain(QuorumStateListener chain) {
		this.chain = chain;
	}

	public QuorumStateListener getProxy() {
		return proxy;
	}

	public void setProxy(QuorumStateListener proxy) {
		this.proxy = proxy;
	}

	@Override
	public void startup(Properties zkProp, QuorumPeerConfig config) {
		if(proxy != null){
			proxy.startup(zkProp, config);
		}
		if(chain != null){
			chain.startup(zkProp, config);
		}
	}

	@Override
	public void shutdown() {
		if(proxy != null){
			proxy.shutdown();
		}
		if(chain != null){
			chain.shutdown();
		}
	}

	@Override
	public void stateChanged(State state, QuorumPeer peer) {
		if(proxy != null){
			proxy.stateChanged(state, peer);
		}
		if(chain != null){
			chain.stateChanged(state, peer);
		}
	}

}
