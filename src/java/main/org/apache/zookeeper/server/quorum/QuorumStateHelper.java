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
    
    static final int TYPE_EXIT = -1;
    static final int TYPE_STARTUP = 1;
    static final int TYPE_SHUTDOWN = 3;
    static final int TYPE_STATECHANGED = 2;
    static class Message{
    	int type;
    	QuorumStateListener.State state;
    	QuorumPeer peer;
    	
    	Message(int type){this.type = type;}
    	Message(QuorumStateListener.State state,
    			QuorumPeer peer){
    		this.type = TYPE_STATECHANGED;
    		this.state = state;
    		this.peer = peer;
    	}
    }
    
    Thread thread;
    private java.util.concurrent.BlockingDeque<Message> messages = new java.util.concurrent.LinkedBlockingDeque<Message>();
    
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
    		Message message = new Message(state, peer);
    		messages.offer(message);
    	}
    }
    
    public void startup(){
    	if(stateListeners != null && !stateListeners.isEmpty()){
    		thread = new Thread(new Runnable(){

				@Override
				public void run() {
					Message message = null;
					while(true){
						try {
							message = messages.take();
						} catch (InterruptedException e) {
						}
						if(message == null) continue;
						if(message.type == TYPE_EXIT)break;
						if(message.type == TYPE_STARTUP){
				    		for(QuorumStateListener listener : stateListeners){
				    			listener.startup();
				    		}
						}else if(message.type == TYPE_SHUTDOWN){
							for(QuorumStateListener listener : stateListeners){
				    			listener.shutdown();
				    		}
						}else if(message.type == TYPE_STATECHANGED){
							for(QuorumStateListener listener : stateListeners){
				    			listener.stateChanged(message.state, message.peer);
				    		}
						}
					}
				}
    			
    		});
    		thread.start();
    		Message message = new Message(TYPE_STARTUP);
    		messages.offer(message);
    	}
    }
    
    public void shutdown(){
    	if(stateListeners != null && !stateListeners.isEmpty()){
    		messages.clear();
    		Message message = new Message(TYPE_SHUTDOWN);
    		messages.offer(message);
    		
    		message = new Message(TYPE_EXIT);
     		messages.offer(message);
     		try {
				thread.join();
			} catch (InterruptedException e) {
			}
     		messages.clear();
    	}
    }
}
