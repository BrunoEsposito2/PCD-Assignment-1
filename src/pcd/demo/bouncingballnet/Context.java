package pcd.demo.bouncingballnet;


import java.util.*;

import pcd.demo.common.*;

/**
 * Context acts as container of the balls.
 *  
 * @author aricci
 */
public class Context {

    private Boundary bounds;
    private ArrayList<BallAgent> balls;
    private Peer leftPeer;
	private Peer rightPeer;
    
    public Context(Peer left, Peer right){
        bounds = new Boundary(-1.0,-1.0,1.0,1.0);
        balls = new ArrayList<BallAgent>();
    		leftPeer = left;
    		rightPeer = right;
    } 

    /**
     * Attach a left peer
     */
    public synchronized void attachLeft(Peer peer){
    		leftPeer = peer;
    }

    /**
     * Attach a right peer
     */
    public synchronized void attachRight(Peer peer){
		rightPeer = peer;
    }
    
    /**
     * Try to send the ball on the right peer
     */
    public synchronized boolean goneOutsideRight(BallAgent agent, P2d pos, V2d v, double speed){
    		if (rightPeer!=null){
    			rightPeer.sendBall(new P2d(-1.0,pos.y),v,speed);
    			balls.remove(agent);
    			return true;
    		} else {
    			return false;
    		}
    }
    
    /**
     * Try to send the ball on the left peer
     */
    public synchronized boolean goneOutsideLeft(BallAgent agent, P2d pos, V2d v, double speed){
		if (leftPeer!=null){
			leftPeer.sendBall(new P2d(1.0,pos.y),v,speed);
			balls.remove(agent);
			return true;
		} else {
			return false;
		}
}

    public synchronized void createNewBall(){
        BallAgent agent = new BallAgent(this);
        balls.add(agent);
        agent.start();
    }
    
    public synchronized void createNewBall(P2d pos, V2d v, double speed){
        BallAgent agent = new BallAgent(this,pos,v,speed);
        balls.add(agent);
        agent.start();
    }
    
    public synchronized void removeBall(){
        	if (balls.size()>0){
        	    BallAgent ball = balls.get(0);
        	    balls.remove(ball);
        	    ball.terminate();
       	}
    }
    
    public synchronized P2d[] getPositions(){
        P2d[] array = new P2d[balls.size()];
        for (int i=0; i<array.length; i++){
            array[i] = balls.get(i).getPos();
        }
        return array;
    }
    
    public  Boundary getBounds(){
        return bounds;
    }
}
