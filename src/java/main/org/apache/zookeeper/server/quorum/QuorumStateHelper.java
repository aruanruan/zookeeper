package org.apache.zookeeper.server.quorum;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.zookeeper.server.quorum.QuorumPeerConfig.ConfigException;

public final class QuorumStateHelper {
	static QuorumStateHelper instance = new QuorumStateHelper();
	public static QuorumStateHelper getInstance(){
		return instance;
	}

	 //aruan 2016/3/20
    private List<QuorumStateListener> stateListeners; 
    public List<QuorumStateListener> getStateListeners(){
    	return this.stateListeners;
    }
    
    public void initialize(String types,  Properties zkProp, QuorumPeerConfig config) throws ConfigException{
    	String [] listeners = types.split(",");
    	for(String listener : listeners){
    		String s = listener.trim();
    		if(s.length() == 0) continue;
    		try{
        		Class<?> type = Class.forName(s);
        		QuorumStateListener instance = (QuorumStateListener) type.newInstance();
        		instance.initialize(zkProp, config);
        		if(stateListeners == null){
        			stateListeners = new ArrayList<QuorumStateListener>();
        		}
        		stateListeners.add(instance);
    		}catch(Exception e){
    			throw new ConfigException(e.getMessage(), e);
    		}
    	}
    }
    
    public void stateChanged(QuorumStateListener.State state, QuorumPeer peer){
    	if(stateListeners != null && !stateListeners.isEmpty()){
    		for(QuorumStateListener listener : this.stateListeners){
    			listener.stateChanged(state, peer);
    		}
    	}
    }
}
