import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class RegionDistance{

	static BufferedImage image;
	public RegionDistance(){
		
	}
  

  public static void main(String[] args) {
	ImagePanel panel = new ImagePanel();
	panel.setBackground(Color.white);
    JFrame frame = new JFrame();
    frame.getContentPane().add(panel);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(400,400);
    frame.setVisible(true);
    Dimension size = panel.getSize();
    image = new BufferedImage(size.width,size.height, BufferedImage.TYPE_BYTE_GRAY);
    Graphics g = image.createGraphics();
    panel.paint(g);
    int n;
    for(n=0; n<5; n++){
    dilateRegion();
    }
    JFrame frame2 = new JFrame();
    JPanel panel2 = new JPanel();
    frame2.getContentPane().add(panel2);
    JLabel label2 = new JLabel();
    ImageIcon icon2 = new ImageIcon(image);
    label2.setIcon(icon2);
    panel2.add(label2);
    frame2.setSize(400,400);
    frame2.setVisible(true);
    
    
     try
    {
        ImageIO.write(image, "jpg", new File("DistanceCount.jpg"));
    }
    catch(IOException ioe)
    {
        System.out.println("help: " + ioe.getMessage());
    }
    
     System.out.print("After dilating two regions, the closest point found!\n");
     System.out.print("The distance between two regions is: "+n*2 +" pixels");
     
     g.dispose();
     
    
   
  }
  
  public static void dilateRegion(){
	  WritableRaster raster = image.getRaster();
      
      int [][]mask = 
    	  {   {1,1,1},
    		  {1,1,1},
    		  {1,1,1}};
      int w = image.getWidth();
      int h = image.getHeight();
        
      int[] d= new int[1000000];  
        
      for(int j=1;j<h-1;j++){  
          for(int i=1;i<w-1;i++){  
             if(raster.getSample(i, j, 0)!=255){
            	 for(int m=-1; m<2 ; m++){  
                     for(int n=-1;n<2;n++){  
                    	 d[(i+n-1)*w + j+m-1]=0;
                         raster.setSample(i+n-1,j+m-1, 0, 0);
                     }  
                 }  
             }else{
            	 d[i*w + j] = 255;     
             }
             
          }  
      }  
       
      for(int x = 0; x < w; x++){
    	  for(int y = 0; y < h; y++){
              int v = d[x*w+y];
              raster.setSample(x, y, 0, v);
              //System.out.print(x + " " +y + " "+ raster.getSample(x, y, 0)+"\n");
          }
      }
      
  }
}
 class ImagePanel extends JPanel{
	  public void paintComponent(Graphics g) {
		  	super.paintComponent(g);
		    int xpoints[] = {3*60,3*75,3*85,3*90,3*85,3*60,3*54,3*50};
		    int ypoints[] = {3*20,3*26,3*30,3*50,3*60,3*65,3*55,3*40};
		    int npoints = 8;
		    g.fillPolygon(xpoints, ypoints, npoints);
		    
		    int xpoints2[] = {2*15,2*40,2*60,2*85,2*70,2*55,2*40,2*25};
		    int ypoints2[] = {2*100,2*80,2*75,2*100,2*130,2*145,2*135,2*120};
		    int npoints2 = 8;
		    g.fillPolygon(xpoints2, ypoints2, npoints2);
		  }
  }
