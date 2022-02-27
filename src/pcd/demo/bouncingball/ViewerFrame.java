package pcd.demo.bouncingball;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import javax.swing.*;

import pcd.demo.common.*;


public class ViewerFrame extends JFrame {
    
    private VisualiserPanel panel;
    
    public ViewerFrame(Context ctx, int w, int h){
        setTitle("Bouncing Balls");
        setSize(w,h);
        setResizable(false);
        panel = new VisualiserPanel(w,h);
        getContentPane().add(panel);
        addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent ev){
				System.exit(-1);
			}
			public void windowClosed(WindowEvent ev){
				System.exit(-1);
			}
		});
    }
    
    public void updatePosition(P2d[] pos){
        panel.updatePositions(pos);
    }
        
    public static class VisualiserPanel extends JPanel {
        private P2d[] positions;
        private long dx;
        private long dy;
        
        public VisualiserPanel(int w, int h){
            setSize(w,h);
            dx = w/2 - 20;
            dy = h/2 - 20;
        }

        public void paint(Graphics g){
    		Graphics2D g2 = (Graphics2D) g;
    		
    		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
    		          RenderingHints.VALUE_ANTIALIAS_ON);
    		g2.setRenderingHint(RenderingHints.KEY_RENDERING,
    		          RenderingHints.VALUE_RENDER_QUALITY);
    		g2.clearRect(0,0,this.getWidth(),this.getHeight());
            synchronized (this){
	            if (positions!=null){
	                Arrays.stream(positions).forEach( p -> {
	                	int x0 = (int)(dx+p.x*dx);
		                int y0 = (int)(dy-p.y*dy);
		                g2.drawOval(x0,y0,20,20);
		            });
	            }
	            g2.drawString("Balls (Threads): "+positions.length, 2, 20);
	            
            }
            
        }
        
        public void updatePositions(P2d[] pos){
            synchronized(this){
                positions = pos;
            }
            repaint();
        }
    }
}
