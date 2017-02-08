import javax.imageio.ImageIO;

import java.awt.image.*;
import java.io.*;


public class CarDetection {
	
	
	public CarDetection(){

		
	}
		
	public static void main(String[] argv) {
		
		int image1_location;
		int image2_location;
		float speed;
		
	      try {
	    	  
	    	  BufferedImage image1=ImageIO.read (new File (argv[0]));
	    	  BufferedImage image2=ImageIO.read (new File (argv[1]));
	    	  image1 = convertToGrey(image1);
	    	  image2 = convertToGrey(image2);
	       
	    	  BufferedImage image_model=ImageIO.read (new File ("model.JPG"));
	    	  image_model = convertToGrey(image_model);
	       
	    	  int[] hist_model = new int[256];
	    	  hist_model = getHistogram(image_model);
	    	  
	    	  int model_width = image_model.getWidth();
	    	  int model_height = image_model.getHeight();
	    	  
	    	  System.out.println("Searching...It takes around 2 minutes....");
	    	  System.out.println();
	    	  image1_location = detectCar(hist_model, model_width, model_height, image1);
	    	  image2_location = detectCar(hist_model, model_width, model_height, image2);
	    	  int distance = Math.abs(image1_location-image2_location);
	    	  System.out.println("Location 1: #" + image1_location + " pixel"+'\n');
	    	  System.out.println("Location 2: #" + image2_location + " pixel"+'\n');
	    	  System.out.println("Distance: " + distance + " pixel"+'\n');
	    	  System.out.println("Total width: " + image1.getWidth() + " pixel"+'\n');
	    	  
	        
	    	  speed = calculateSpeed(distance, image1.getWidth());
	    	  System.out.println("Speed: " + speed + " m/s"+'\n');
	       
	      }
	      catch (Exception e) {
	        System.err.println(e);
	        System.exit(1);
	      }
	    
	}

	private static int[] getHistogram(BufferedImage image) {
		int height = image.getHeight();
        int width = image.getWidth();
        int[] bins = new int[256];
        
        Raster raster = image.getRaster();
        for(int i=0; i < width ; i++)
        {
            for(int j=0; j < height ; j++)
            {
               
            	int hist = raster.getSample(i,j, 0);
				bins[hist]++;
            }
        }
        return bins;
	}

	private static int detectCar(int[] hist_model, int model_width,
			int model_height, BufferedImage image) {
		//int x = 0, y = 0;
		int width = image.getWidth();
		int height = image.getHeight();
		int min = 500000000;
		int min_x = 0;
		int min_y = 0;
		
		int i = 0, j = 0;
		for(i = 0; i < width-model_width; i = i+30)
		{
			for(j = 0; j < height-model_height; j = j+30)
			{	
				float[][] dif_sum = new float[width][height];
				BufferedImage region = image.getSubimage(i, j, model_width, model_height);
				int[] hist_region = new int[256];
				hist_region = getHistogram(region);
				for(int n = 0; n < 255; n++)
				{
					float dif = 0;
					dif = hist_model[n] - hist_region[n];
					dif = Math.round(Math.sqrt(dif*dif));
					dif_sum[i][j] = dif_sum[i][j] + dif;	
					//System.out.println("dif_sum = " + dif_sum[i][j]);
				}
				//System.out.println(dif_sum[i][j]);					
				if (dif_sum[i][j]<min)
				{
					min = (int) dif_sum[i][j];
					min_x = i;
					min_y = j;
					//System.out.println("Min is: "+ min);
				}							
			}	
			
		}
		
		return min_x;
	}

	
	 private static BufferedImage convertToGrey(BufferedImage image) {
	        BufferedImage g = new BufferedImage(
	            image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
	        ColorConvertOp op = new ColorConvertOp(
	            image.getColorModel().getColorSpace(),
	            g.getColorModel().getColorSpace(), null);
	        op.filter(image, g);
	        return g;
	    }
	 
	private static float calculateSpeed(int distance, int width_pixel) {
		float speed = 0;
		float time = (float) 0.152;
		float width_actual = (float) 4;
		//float width_pixel = 3264;
		float distance_actual = 0;
		distance_actual = distance*width_actual/width_pixel;
		speed = distance_actual/time;		
		return speed;
		
	}



}
