package pcd.demo.bouncingballnet;

import java.net.*;

/**
 * Distributed BouncingBalls
 * 
 * Balls controlled by autonomous threads,
 * travelling through the network. 
 * 
 * @author aricci
 * @year 2005
 *
 */
public class BouncingBalls {

    public static void main(String[] args) throws Exception {
        
    		Peer left = null;
    		Peer right = null;

    		int managerPort = 8888;
    		InetAddress local = InetAddress.getLocalHost();
    		
    		if (isOption(args,"-?") || isOption(args,"-h")){
    			System.out.println("Optional arguments: \n -p {local port}\n -l {Left peer IP address}:{Left peer port}\n -r {Right peer IP address}:{Right peer port}\n");
    			System.exit(1);
    		}

    		String sp = getOption(args,"-p");
    		if (sp!=null){
    			managerPort = Integer.parseInt(sp);
    		}
    		
    		String ls = getOption(args,"-l");
    		if (ls!=null){
    			// install a left peer
    			int port = 8888;
    			String addr = ls;
    			int index = ls.indexOf(':');
    			if (index!=-1){
    				port = Integer.parseInt(addr.substring(index+1));
    				addr = addr.substring(0,index); 
    			}
    			System.out.println("Left peer: "+addr+" - port: "+port);
    			left = new Peer(new InetSocketAddress(addr,port));
    			// informs the peer to be attached
    			left.attachRight(new InetSocketAddress(local.getHostName(),managerPort));
    		}
    		
    		String rs = getOption(args,"-r");
    		if (rs!=null){
    			// install a right peer
    			int port = 8888;
    			String addr = rs;
    			int index = rs.indexOf(':');
    			if (index!=-1){
    				port = Integer.parseInt(addr.substring(index+1));
    				addr = addr.substring(0,index); 
    			}
    			System.out.println("Right peer: "+addr+" - port: "+port);
    			right = new Peer(new InetSocketAddress(addr,port));
    			// informs the peer to be attached
    			right.attachLeft(new InetSocketAddress(local.getHostName(),managerPort));
    		}

    		
    		Context ctx = new Context(left,right);
    		
    		BouncingManager manager = new BouncingManager(managerPort,ctx);
    		manager.start();
        
    		Visualiser viewer = new Visualiser(ctx);
    		viewer.start();
      
    		ControlPanel control = new ControlPanel(ctx);
    		control.setVisible(true);
    }

    public static String getOption(String[] args,String prefix){
        for (int i=0; i<args.length; i++)
            if (args[i].equals(prefix)){
                return args[i+1];
            }
        return null;
    }

    public static boolean isOption(String[] args,String prefix){
        for (int i=0; i<args.length; i++)
            if (args[i].equals(prefix)){
                return true;
            }
        return false;
    }
    
}
