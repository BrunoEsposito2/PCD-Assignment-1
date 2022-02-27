package pcd.demo.bouncingballnet;

import java.io.*;
import java.net.*;

import pcd.demo.common.P2d;
import pcd.demo.common.V2d;


/**
 * Agents processing incoming requests
 * for attaching peers and migrating balls.
 * 
 * @author aricci
 *
 */
public class BouncingManager extends Thread{

	private boolean stop;
	private DatagramSocket socket;
	private DatagramPacket currentMsg;
	private int port;
	private Context ctx;
	
	public BouncingManager(int port, Context ctx) throws Exception {
		this.port = port;
		stop = false;
		this.ctx = ctx;
		socket = new DatagramSocket(port);
		byte[] msgBuffer = new byte[1024];
		currentMsg = new DatagramPacket(msgBuffer,msgBuffer.length);
	}
	
	public void run(){
		log("Start working (port "+port+").");
		while (!stop) {
			try {
				socket.receive(currentMsg);
				DataInputStream st = new DataInputStream(new ByteArrayInputStream(currentMsg.getData()));
				int code = st.readInt();
                 if (code == 0xCAFE01){
					// attach left
					String addr = st.readUTF();
					int port = st.readInt();
					try {
						Peer peer = new Peer(new InetSocketAddress(addr,port));
						ctx.attachLeft(peer);
					} catch (Exception ex){
						System.err.println("Error in attaching left peer: "+st+":"+port);
					}
				} else if (code == 0xCAFE02){
					// attach right
					String addr = st.readUTF();
					int port = st.readInt();
					try {
						Peer peer = new Peer(new InetSocketAddress(addr,port));
						ctx.attachRight(peer);
					} catch (Exception ex){
						System.err.println("Error in attaching right peer: "+st+":"+port);
					}
				} else if (code == 0xCAFE03){
					// new ball
					double px = st.readDouble();
					double py = st.readDouble();
					double vx = st.readDouble();
					double vy = st.readDouble();
					double speed = st.readDouble();
					P2d pos = new P2d(px,py);
					V2d vel = new V2d(vx,vy);
					log("new ball: "+pos+" "+vel+" "+speed);
					ctx.createNewBall(pos,vel,speed);
				} 
			} catch (Exception ex){
				log("Errore nella comunicazione.");
			}
		}
		log("Shutdown.");
	}

	private void log(String msg){
		System.out.println("[BOUNCING MANAGER] "+msg);
	}
}
