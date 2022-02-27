package pcd.demo.bouncingball;

import java.util.*;

import pcd.demo.common.*;

public class Context {

    private Boundary bounds;
    private ArrayList<BallAgent> balls;
    
    public Context(){
        bounds = new Boundary(-1.0,-1.0,1.0,1.0);
        balls = new ArrayList<BallAgent>();
    } 
    
    public synchronized void createNewBall(){
        BallAgent agent = new BallAgent(this);
        balls.add(agent);
        agent.start();
    }
    
    public synchronized void removeBall(){
        if (balls.size()>0){
            BallAgent ball = (BallAgent)balls.get(0);
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
