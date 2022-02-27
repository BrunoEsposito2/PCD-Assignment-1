package pcd.demo.bouncingballnet;

import java.io.*;
import java.net.*;

import pcd.demo.common.*;


/**
 * Class representing a target peer 
 * 
 * @author aricci
 *
 */
public class Peer {
	private InetSocketAddress address;
	private DatagramSocket socket;
	
	public Peer(InetSocketAddress targetAddr) throws Exception {
		address = targetAddr;
		socket = new DatagramSocket();
	}

	/**
	 * Informs the target peer to attach current node on the left
	 */
	public void attachLeft(InetSocketAddress local) {
		try {
			ByteArrayOutputStream bout = new ByteArrayOutputStream(256);
			DataOutputStream out = new DataOutputStream(bout);
			out.writeInt(0xCAFE01);
			out.writeUTF(local.getHostName());
			out.writeInt(local.getPort());
			byte[] barray = bout.toByteArray();
			DatagramPacket msg = new DatagramPacket(barray,barray.length,address);
			socket.send(msg);
		} catch (Exception ex){
			ex.printStackTrace();
			System.err.println("Error in attaching peer.");
		}
	}
	
	/**
	 * Informs the target peer to attach current node on the right
	 */
	public void attachRight(InetSocketAddress local) {
		try {
			ByteArrayOutputStream bout = new ByteArrayOutputStream(256);
			DataOutputStream out = new DataOutputStream(bout);
			out.writeInt(0xCAFE02);
			out.writeUTF(local.getHostName());
			out.writeInt(local.getPort());
			byte[] barray = bout.toByteArray();
			DatagramPacket msg = new DatagramPacket(barray,barray.length,address);
			socket.send(msg);
		} catch (Exception ex){
			ex.printStackTrace();
			System.err.println("Error in attaching peer.");
		}
	}

	/**
	 * Sends a ball to the peer
	 */
	public void sendBall(P2d pos, V2d v, double speed){
		try {
			ByteArrayOutputStream bout = new ByteArrayOutputStream(64);
			DataOutputStream out = new DataOutputStream(bout);
			out.writeInt(0xCAFE03);
			out.writeDouble(pos.x);
			out.writeDouble(pos.y);
			out.writeDouble(v.x);
			out.writeDouble(v.y);
			out.writeDouble(speed);			
			byte[] barray = bout.toByteArray();
			DatagramPacket msg = new DatagramPacket(barray,barray.length,address);
			socket.send(msg);
		} catch (Exception ex){
			ex.printStackTrace();
			System.err.println("Error in sending the ball.");
		}
	}
}
